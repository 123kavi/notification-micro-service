package com.notification.service.impl;

import com.notification.configuration.kafka.producer.JsonKafkaProducer;
import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import com.notification.exception.SystemWarningException;
import com.notification.repository.FeatureFlagRepository;
import com.notification.service.KafkaMessagingService;
import com.notification.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingServiceImpl implements KafkaMessagingService {

    private final FeatureFlagRepository featureFlagRepository;
    private final JsonKafkaProducer kafkaProducer;

    /**
     * @param emailRequestDto : com.cl.notification.dto.request.SendEmailRequestDto
     * @return java.lang.String
     * @throws SystemWarningException
     * Sends HTML Mail with sms-notification & push-notification through kafka message queue
     */
    @Override
    public String sendHtmlMailWithNotifications(SendEmailRequestDto emailRequestDto) throws SystemWarningException {
        validateIsTemplateNameExists(emailRequestDto.getTemplateName());
        if (isExistsByTemplateNameAndActiveStatus(emailRequestDto.getTemplateName())) {
            kafkaProducer.sendHtmlMail(emailRequestDto);
            kafkaProducer.sendSmsNotification(emailRequestDto);
            kafkaProducer.sendPushNotification(emailRequestDto);
            log.info(Utility.MSG_EMAIL_SENT_SUCCESSFULLY);
            return Utility.MSG_EMAIL_SENT_SUCCESSFULLY;
        }
        throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
    }

    /**
     * @param emailRequestDto : com.cl.notification.dto.request.SendFileEmailRequestDto
     * @return java.lang.String
     * @throws SystemWarningException
     * Sends File Mail with sms-notification & push-notification through kafka message queue
     */
    @Override
    public String sendFileMailWithNotifications(SendFileEmailRequestDto emailRequestDto) throws SystemWarningException{
        validateIsTemplateNameExists(emailRequestDto.getTemplateName());
        if (isExistsByTemplateNameAndActiveStatus(emailRequestDto.getTemplateName())) {
            kafkaProducer.sendFileMail(emailRequestDto);
            kafkaProducer.sendSmsNotification(emailRequestDto);
            kafkaProducer.sendPushNotification(emailRequestDto);
            log.info(Utility.MSG_EMAIL_SENT_SUCCESSFULLY);
            return Utility.MSG_EMAIL_SENT_SUCCESSFULLY;
        }
        throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
    }

    /**
     * @param templateName : java.lang.String
     * @return boolean
     * Returns boolean value based on the existence of a particular featureFlag by name and activeStatus
     */
    private boolean isExistsByTemplateNameAndActiveStatus(String templateName) {
        return featureFlagRepository.existsByNameAndStatus(templateName, Utility.ACTIVE_STATUS_TRUE);
    }

    /**
     * @param templateName : java.lang.String
     * Checks whether provided templateName satisfies the system's validation criteria
     */
    private void validateIsTemplateNameExists(String templateName) {
        if (Utility.isNullOrEmpty(templateName)) {
            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
        }
        if (!featureFlagRepository.existsByName(templateName)) {
            throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
        }
    }
}
