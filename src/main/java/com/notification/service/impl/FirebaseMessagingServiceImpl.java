package com.notification.service.impl;

import com.notification.dto.NotificationDto;
import com.notification.dto.request.NotificationRequestDto;
import com.notification.entity.MailContent;
import com.notification.entity.Notification;
import com.notification.entity.SentPushNotificationLog;
import com.notification.enums.ActiveStatus;
import com.notification.exception.SystemWarningException;
import com.notification.repository.FeatureFlagRepository;
import com.notification.repository.MailContentRepository;
import com.notification.repository.NotificationRepository;
import com.notification.repository.SentPushNotificationLogRepository;
import com.notification.service.FirebaseMessagingService;
import com.notification.util.Utility;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.TopicManagementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    @Value(value = "${app.firebase.topic}")
    private String firebaseTopic;

    @Autowired
    @Qualifier(value = "firebaseMessagingClient")
    private FirebaseMessaging firebaseMessaging;
    private final NotificationRepository notificationRepository;
    private final SentPushNotificationLogRepository sentPushNotificationLogRepository;
    private final MailContentRepository mailContentRepository;
    private final FeatureFlagRepository featureFlagRepository;
    private final CacheManager cacheManager;

    /**
     * @param notificationDto : com.cl.notification.dto.NotificationDto
     * @param userName : java.lang.String
     * Saves or updates firebase notification details mapped by particular user
     */
    @Override
    public void saveOrUpdateFirebaseNotificationDetails(NotificationDto notificationDto, String userName) {
        validateNotificationDetails(notificationDto);
        Notification notification = notificationRepository.findById(notificationDto.getUserId()).orElse(new Notification());
        notification.setUserId(notificationDto.getUserId());
        notification.setToken(notificationDto.getToken());
        notification.setPlatform(notificationDto.getPlatform());
        notification.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        notification.setLastUpdatedUser(userName);
        notification.setActiveStatus(ActiveStatus.ACTIVE);
        Notification savedNotification = notificationRepository.save(notification);
        log.info(String.format(Utility.LOG_PUSH_NOTIFICATION_DETAILS_SAVED_SUCCESSFULLY, savedNotification.getUserId(), savedNotification.getPlatform()));
        clearBackendCache(savedNotification.getUserId(), savedNotification.getToken());
    }

    /**
     * @param userId : long
     * @param templateName : java.lang.String
     * @param notifyDto : com.cl.notification.dto.request.NotificationRequestDto
     * @return java.lang.String
     * Notify push notifications to all subscribed channels for a particular user
     */
    @Override
    public String notifyAllChannels(long userId, String templateName, NotificationRequestDto notifyDto) {
        if (null == notifyDto) {
            throw new SystemWarningException(Utility.EX_NOTIFICATION_DETAILS_REQUIRED);
        }
        if (Utility.isNullOrEmpty(templateName)) {
            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
        }
        if (!featureFlagRepository.existsByName(templateName)) {
            throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
        }
        if (isExistsByTemplateNameAndActiveStatus(templateName)) {
            MailContent mailContent = mailContentRepository.findFirstByTemplateName(templateName);
            if (null != mailContent) {
                String msgTitle = replaceParams(mailContent.getMsgTitle(), notifyDto.getContent());
                String msgBody = replaceParams(mailContent.getMsgBody(), notifyDto.getContent());
                return sendPushNotifications(userId, msgTitle, msgBody);
            } else {
                throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
            }
        }
        throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
    }

    /**
     * @param userId : long
     * @param msgTitle : java.lang.String
     * @param msgBody : java.lang.String
     * @return java.lang.String
     * Sends push notifications and saves sent push notification log details in the database
     */
    @Cacheable(key = "#userId", value = Utility.CACHE_VAL_FIND_TOKENS_BY_USER_ID)
    public String sendPushNotifications(long userId, String msgTitle, String msgBody) {
        List<String> regTokens = notificationRepository.findTokensByUserId(userId);
        if (null != regTokens && !regTokens.isEmpty()) {
            TopicManagementResponse response;
            try {
                response = firebaseMessaging.subscribeToTopic(regTokens, firebaseTopic);
            } catch (FirebaseMessagingException e) {
                throw new SystemWarningException(String.format(Utility.EX_UNABLE_TO_SUBSCRIBE_CHANNELS_WITH_REASON, e.getMessage(), userId) + e.getMessage(), e);
            }
            log.info(String.format(Utility.LOG_USER_FIREBASE_FCM_TOKENS_SUBSCRIBED_SUCCESSFULLY,
                    Arrays.toString(regTokens.toArray()), userId, response.getSuccessCount(),
                    response.getFailureCount(), regTokens.size()));
            if (0 < response.getSuccessCount()) {
                Message message = Message.builder().setNotification(com.google.firebase.messaging
                        .Notification.builder()
                        .setTitle(msgTitle)
                        .setBody(msgBody).build()).setTopic(firebaseTopic).build();

                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    throw new SystemWarningException(String.format(Utility.EX_UNABLE_TO_SUBSCRIBE_CHANNELS_WITH_REASON, e.getMessage(), userId) + e.getMessage(), e);
                }
                log.info(String.format(Utility.LOG_FIREBASE_PUSH_NOTIFICATIONS_SUCCESSFULLY_SENT, response.getSuccessCount(), userId));
                saveSentPushNotificationHstData(userId, msgTitle, msgBody, response);
                return String.format(Utility.MSG_SUCCESSFULLY_SENT_NOTIFICATIONS,
                        response.getSuccessCount(), response.getFailureCount());
            }
            String errorMsg = null != response.getErrors() && !response.getErrors().isEmpty()
                    ? response.getErrors().get(0).getReason() : Utility.EMPTY_STR;
            throw new SystemWarningException(String.format(Utility.EX_UNABLE_TO_SUBSCRIBE_CHANNELS_WITH_REASON, errorMsg, userId));
        } else {
            throw new SystemWarningException(Utility.EX_DATA_NOT_FOUND_FOR_USER_ID);
        }
    }

    /**
     * @param userId : long
     * @param oldToken : java.lang.String
     * @param newToken : java.lang.String
     * @param userName : java.lang.String
     * Updates FCM refresh token details
     */
    @Override
    public void updateFcmRefreshToken(long userId, String oldToken, String newToken, String userName) {
        Notification notification = notificationRepository.findFirstByUserIdAndToken(userId, oldToken); // TODO - check for token null
        if (null != notification) {
            notification.setToken(newToken);
            notification.setLastUpdatedUser(userName);
            notification.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            Notification savedNotification = notificationRepository.save(notification);
            log.info(String.format(Utility.LOG_USER_FIREBASE_FCM_TOKENS_SUCCESSFULLY_UPDATED, userId));
            clearBackendCache(savedNotification.getUserId(), savedNotification.getToken());
        } else {
            throw new SystemWarningException(Utility.EX_FIREBASE_TOKEN_NOT_FOUND);
        }
    }

    /**
     * @param userId : long
     * @param token : java.lang.String
     * @return boolean
     * @throws SystemWarningException
     * Verifies an FCM token's validity whether it's still Active or Inactive
     */
    @Cacheable(key = "#token.concat(#userId)", value = Utility.CACHE_VERIFY_TOKEN_VALIDITY)
    @Override
    public boolean verifyTokenValidity(long userId, String token) throws SystemWarningException {
        Notification notification = notificationRepository.findFirstByUserIdAndToken(userId, token); // TODO - check for token null
        if (null != notification) {
            try {
                firebaseMessaging.send(Message.builder().setToken(token).build());
                log.info(String.format(Utility.LOG_USER_REQUESTED_FIREBASE_FCM_REFRESH_TOKEN, userId, token));
                notification.setActiveStatus(ActiveStatus.ACTIVE);
                Notification savedNotification = notificationRepository.save(notification);
                clearBackendCache(savedNotification.getUserId(), savedNotification.getToken());
                return true;
            } catch (FirebaseMessagingException e) {
                if (Utility.NOT_FOUND.equals(e.getErrorCode().name())) {
                    notification.setActiveStatus(ActiveStatus.INACTIVE);
                    Notification savedNotification = notificationRepository.save(notification);
                    log.info(String.format(Utility.LOG_USER_REQUESTED_OLD_FCM_TOKEN_NOT_FOUND, token, userId));
                    clearBackendCache(savedNotification.getUserId(), savedNotification.getToken());
                    return false;
                }
                throw new SystemWarningException(Utility.EX_FIREBASE_TOKEN_VERIFICATION_FAILED);
            }
        } else {
            throw new SystemWarningException(Utility.EX_FIREBASE_TOKEN_NOT_FOUND);
        }
    }

    private void clearBackendCache(long userId, String token) {
        evictFindTokensByUserId(Utility.CACHE_VAL_FIND_TOKENS_BY_USER_ID, userId);
        evictVerifyTokenValidity(Utility.CACHE_VERIFY_TOKEN_VALIDITY, token, userId);
    }

    @CacheEvict(value = Utility.CACHE_VAL_FIND_TOKENS_BY_USER_ID, key = "#userId")
    public void evictFindTokensByUserId(String findTokensByUserId, long userId) {
        Objects.requireNonNull(cacheManager.getCache(findTokensByUserId)).evict(userId);
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_USER_ID, userId, userId, findTokensByUserId));
    }

    @CacheEvict(value = Utility.CACHE_VERIFY_TOKEN_VALIDITY, key = "#token.concat(#userId.toString())")
    public void evictVerifyTokenValidity(String verifyTokenValidity, String token, Long userId) {
        Objects.requireNonNull(cacheManager.getCache(verifyTokenValidity)).evict(token.concat(String.valueOf(userId)));
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_USER_ID, userId, token.concat(userId.toString()), verifyTokenValidity));
    }

    /**
     * @param userId : long
     * @param msgTitle : java.lang.String
     * @param msgBody : java.lang.String
     * @param response : com.google.firebase.messaging.TopicManagementResponse
     * Save already sent push notification data as a log for future reference
     */
    private void saveSentPushNotificationHstData(long userId, String msgTitle, String msgBody, TopicManagementResponse response) {
        SentPushNotificationLog hst = new SentPushNotificationLog();
        hst.setUserId(userId);
        hst.setMessageTitle(msgTitle);
        hst.setMessageBody(msgBody);
        hst.setSuccessCount(response.getSuccessCount());
        hst.setFailureCount(response.getFailureCount());
        hst.setSentDateTime(Timestamp.valueOf(LocalDateTime.now()));
        sentPushNotificationLogRepository.save(hst);
        log.info(String.format(Utility.LOG_SENT_PUSH_NOTIFICATIONS_SUCCESSFULLY_SAVED, userId));
    }

    /**
     * @param notificationDto : com.cl.notification.dto.NotificationDto
     * Validates notification details matches the system requirements
     */
    private void validateNotificationDetails(NotificationDto notificationDto) {
        if (null == notificationDto) {
            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
        }
        if (Utility.isNullOrEmpty(notificationDto.getToken())) {
            throw new SystemWarningException(Utility.EX_FIREBASE_CLIENT_TOKEN_REQUIRED);
        }
        if (Utility.isNullOrEmpty(notificationDto.getPlatform().name())) {
            throw new SystemWarningException(Utility.EX_DEVICE_PLATFORM_REQUIRED);
        }
    }

    /**
     * @param msg : java.lang.String
     * @param content : java.util.Map<java.lang.String, java.lang.Object>
     * @return java.lang.String
     * Replaces messages parameters with relevant data from database
     */
    private String replaceParams(String msg, Map<String, Object> content) {
        if (null != msg) {
            msg = msg.replace(Utility.NON_EDITABLE_IDENTIFIER_START, Utility.EMPTY_STR)
                    .replace(Utility.NON_EDITABLE_IDENTIFIER_END, Utility.EMPTY_STR);
            for (Map.Entry<String, Object> param : content.entrySet()) {
                msg = msg.replace(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        return msg;
    }

    /**
     * @param templateName : java.lang.String
     * @return boolean
     * Returns boolean value based on the existence of a featureFlag by name and activeStatus
     */
    private boolean isExistsByTemplateNameAndActiveStatus(String templateName) {
        return featureFlagRepository.existsByNameAndStatus(templateName, Utility.ACTIVE_STATUS_TRUE);
    }
}