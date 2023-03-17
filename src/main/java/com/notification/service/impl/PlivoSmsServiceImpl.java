package com.notification.service.impl;

import com.notification.dto.request.NotificationRequestDto;
import com.notification.entity.MailContent;
import com.notification.entity.SentSmsLog;
import com.notification.exception.SystemRootException;
import com.notification.exception.SystemWarningException;
import com.notification.repository.FeatureFlagRepository;
import com.notification.repository.MailContentRepository;
import com.notification.repository.SentSmsLogRepository;
import com.notification.util.Utility;
import com.plivo.api.Plivo;
import com.plivo.api.exceptions.PlivoRestException;
import com.plivo.api.models.message.Message;
import com.plivo.api.models.message.MessageCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Primary // Defines Plivo as Primary SmsService to Resolve the multiple bean consuming error for multiple SmsService implementations such as PlivoSmsServiceImpl & TwilioSmsServiceImpl
@Service(Utility.PLIVO_SMS_SERVICE_QUALIFIER)
public class PlivoSmsServiceImpl extends CogSmsServiceImpl {

    @Value("${app.plivo.auth.id}")
    private String plivoAuthId;
    @Value("${app.plivo.auth.token}")
    private String plivoAuthToken;
    @Value("${app.plivo.source}")
    private String plivoSender;

    @Autowired
    private MailContentRepository mailContentRepository;
    @Autowired
    private SentSmsLogRepository sentSmsLogRepository;
    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    /**
     * @param mailContentRepository : com.cl.notification.repository.MailContentRepository
     * @param cacheManager : org.springframework.cache.CacheManager
     */
    public PlivoSmsServiceImpl(MailContentRepository mailContentRepository, CacheManager cacheManager) {
        super(mailContentRepository, cacheManager);
    }

    /**
     * @param userId : long
     * @param templateName : java.lang.String
     * @param mobileNo : java.lang.String
     * @param notification : com.cl.notification.dto.request.NotificationRequestDto
     * @return com.plivo.api.models.message.MessageCreateResponse
     * @throws SystemRootException
     * Sends Plivo sms message notification
     */
    @Override
    public MessageCreateResponse sendSms(long userId, String templateName, String mobileNo,
                                         NotificationRequestDto notification) throws SystemRootException {

        validateSmsNotificationDetails(templateName, mobileNo, notification);
        if (isExistsByTemplateNameAndActiveStatus(templateName)) {
            MailContent content = mailContentRepository.findFirstByTemplateName(templateName);
            Plivo.init(plivoAuthId, plivoAuthToken);
            String msg = replaceSmsContent(content.getMsgTitle(), content.getMsgBody(), notification.getContent());
            String smsResponse;
            MessageCreateResponse response;
            try {
                response = Message.creator(plivoSender, mobileNo, msg).create();
                log.info(String.format(Utility.LOG_SMS_NOTIFICATIONS_SUCCESSFULLY_SENT, userId, mobileNo));
            } catch (IOException | PlivoRestException e) {
                smsResponse = e.getMessage().split(Utility.CURLY_BRAZE_END)[0].split(Utility.ERROR_JSON_REGEX)[1];
                saveSentSmsHstData(userId, msg, false, smsResponse);
                throw new SystemRootException(e.getMessage(), e);
            }
            smsResponse = response.getMessage();
            saveSentSmsHstData(userId, msg, true, smsResponse);
            return response;
        }
        throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
    }

    /**
     * @param msgTitle : java.lang.String
     * @param msgBody : java.lang.String
     * @param content : java.util.Map<java.lang.String, java.lang.Object>
     * @return java.lang.String
     * Replaces message content details from the database
     */
    private String replaceSmsContent(String msgTitle, String msgBody, Map<String, Object> content) {
        String msg = Utility.EMPTY_STR;
        if (null != msgTitle && null != msgBody) {
            // combines both msg title and body as one to easy identification
            msg = msgTitle.concat(Utility.DOUBLE_NEW_LINE_BREAK).concat(msgBody);
            // Removes non-editable identifiers before send notification
            msg = msg.replace(Utility.NON_EDITABLE_IDENTIFIER_START, Utility.EMPTY_STR)
                    .replace(Utility.NON_EDITABLE_IDENTIFIER_END, Utility.EMPTY_STR);
            // Required message fields filled with relevant data
            for (Map.Entry<String, Object> param : content.entrySet()) {
                msg = msg.replace(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        return msg;
    }

    /**
     * @param userId : long
     * @param msg : java.lang.String
     * @param smsSentStatus : boolean
     * @param smsResponse : java.lang.String
     * Saves sent sms data as a log for future reference
     */
    private void saveSentSmsHstData(long userId, String msg, boolean smsSentStatus, String smsResponse) {
        SentSmsLog sentSmsLog = new SentSmsLog();
        sentSmsLog.setUserId(userId);
        sentSmsLog.setMessageContent(msg);
        sentSmsLog.setSentStatus(smsSentStatus);
        sentSmsLog.setSmsResponse(smsResponse);
        sentSmsLog.setSentDateTime(Timestamp.valueOf(LocalDateTime.now()));
        sentSmsLogRepository.save(sentSmsLog);
        log.info(String.format(Utility.LOG_SENT_SMS_DETAILS_SUCCESSFULLY_SAVED, userId, smsResponse));
    }

    /**
     * @param templateName : java.lang.String
     * @param mobileNo : java.lang.String
     * @param notification : com.cl.notification.dto.request.NotificationRequestDto
     * Validates whether provided notification details matches the system requirements
     */
    private void validateSmsNotificationDetails(String templateName, String mobileNo,
                                                NotificationRequestDto notification) {
        if (Utility.isNullOrEmpty(templateName)) {
            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
        }
        if (!featureFlagRepository.existsByName(templateName)) {
            throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
        }
        if (Utility.isNullOrEmpty(mobileNo)) {
            throw new SystemWarningException(Utility.EX_MOBILE_NO_REQUIRED);
        }
        if (!Utility.isMobileNoValidatesWithCountryCode(mobileNo)) {
            throw new SystemWarningException(String.format(Utility.EX_INVALID_MOBILE_NUMBER_FORMAT, mobileNo));
        }
        if (null == notification) {
            throw new SystemWarningException(Utility.EX_NOTIFICATION_DETAILS_REQUIRED);
        }
    }

    /**
     * @param templateName : java.lang.String
     * @return boolean
     * Returns boolean value based on the existence of a particular featureFlag by name and activeStatus
     */
    private boolean isExistsByTemplateNameAndActiveStatus(String templateName) {
        return featureFlagRepository.existsByNameAndStatus(templateName, Utility.ACTIVE_STATUS_TRUE);
    }
}