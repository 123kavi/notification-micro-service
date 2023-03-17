package com.notification.configuration.kafka.consumer;

import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import com.notification.exception.SystemRootException;
import com.notification.service.CogEmailService;
import com.notification.service.FirebaseMessagingService;
import com.notification.service.impl.CogSmsServiceImpl;
import com.notification.service.impl.PlivoSmsServiceImpl;
import com.notification.service.impl.TwilioSmsServiceImpl;
import com.notification.util.Utility;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class JsonKafkaConsumer {

    @Value("${app.sms.service-name.active}")
    private String activeSmsService;
    @Value("${app.sms.service-name.plivo}")
    private String plivoSmsService;
    @Value("${app.sms.service-name.twilio}")
    private String twilioSmsService;

    private final CogEmailService emailService;
    private final FirebaseMessagingService firebaseMessagingService;
    private CogSmsServiceImpl smsService;

    /**
     * @param context : org.springframework.context.ApplicationContext
     * Determines the current active sms service, and initialize it as the current sms service for system usages
     */
    @Autowired
    public void setSmsService(ApplicationContext context) {
        if (Objects.equals(activeSmsService, plivoSmsService)) { // if plivo is defined as current active sms service
            smsService = (PlivoSmsServiceImpl) context.getBean(Utility.PLIVO_SMS_SERVICE_QUALIFIER);
        } else if (Objects.equals(activeSmsService, twilioSmsService)) { // if twilio is defined as current active sms service
            smsService = (TwilioSmsServiceImpl) context.getBean(Utility.TWILIO_SMS_SERVICE_QUALIFIER);
        } else { // Takes plivo as the default sms service if not an active sms service currently defined
            smsService = (PlivoSmsServiceImpl) context.getBean(Utility.PLIVO_SMS_SERVICE_QUALIFIER);
        }
    }

    /**
     * @param emailRequestDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * Kafka consumer for consuming HTML Mail sending functionality
     */
    @KafkaListener(topics = "${spring.kafka.topic.html.send-html-mail}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeHtmlMailSender(SendEmailRequestDto emailRequestDto) {
        try {
            emailService.sendHtmlEmail(emailRequestDto);
        } catch (MessagingException | TemplateException | IOException e) {
            log.error(e.getMessage(), e);
            throw new SystemRootException(e.getMessage());
        }
    }

    /**
     * @param emailRequestDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * Kafka consumer for consuming HTML Mail Specific SMS sending functionality
     */
    @KafkaListener(topics = "${spring.kafka.topic.html.send_sms_notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeHtmlMailSmsSender(SendEmailRequestDto emailRequestDto) {
        smsService.sendSms(emailRequestDto.getUserId(), emailRequestDto.getTemplateName(),
                emailRequestDto.getMobileNo(), emailRequestDto.getNotification());
    }

    /**
     * @param emailRequestDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * Kafka consumer for consuming HTML Mail Specific Push Notification sending functionality
     */
//    @KafkaListener(topics = "${spring.kafka.topic.html.send_push_notification}", groupId = "${spring.kafka.consumer.group-id}")
//    public void consumeHtmlMailPushNotificationSender(SendEmailRequestDto emailRequestDto) {
//        try {
//            firebaseMessagingService.notifyAllChannels(emailRequestDto.getUserId(),
//                    emailRequestDto.getTemplateName(), emailRequestDto.getNotification());
//        } catch (FirebaseMessagingException e) {
//            throw new SystemRootException(e.getMessage(), e);
//        }
//    }

    /**
     * @param fileEmailRequestDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * Kafka consumer for consuming Mails with file attachment sending functionality
     */
    @KafkaListener(topics = "${spring.kafka.topic.file.send-file-mail}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFileMailSender(SendFileEmailRequestDto fileEmailRequestDto) {
        try {
            emailService.sendFileMail(fileEmailRequestDto);
        } catch (MessagingException | TemplateException | IOException e) {
            throw new SystemRootException(e.getMessage(), e);
        }
    }

    /**
     * @param fileEmailRequestDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * Kafka consumer for consuming File Mail Specific Sms sending functionality
     */
    @KafkaListener(topics = "${spring.kafka.topic.file.send_sms_notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFileMailSmsSender(SendFileEmailRequestDto fileEmailRequestDto) {
        smsService.sendSms(fileEmailRequestDto.getUserId(), fileEmailRequestDto.getTemplateName(),
                fileEmailRequestDto.getMobileNo(), fileEmailRequestDto.getNotification());
    }

    /**
     * @param fileEmailRequestDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * Kafka consumer for consuming File Mail Specific Push Notification sending functionality
     */
//    @KafkaListener(topics = "${spring.kafka.topic.file.send_push_notification}", groupId = "${spring.kafka.consumer.group-id}")
//    public void consumeFileMailPushNotificationSender(SendFileEmailRequestDto fileEmailRequestDto) {
//        try {
//            firebaseMessagingService.notifyAllChannels(fileEmailRequestDto.getUserId(),
//                    fileEmailRequestDto.getTemplateName(), fileEmailRequestDto.getNotification());
//        } catch (FirebaseMessagingException e) {
//            throw new SystemRootException(e.getMessage(), e);
//        }
//    }
}
