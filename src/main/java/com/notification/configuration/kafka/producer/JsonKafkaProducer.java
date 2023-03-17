package com.notification.configuration.kafka.producer;

import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class JsonKafkaProducer {

    @Value("${spring.kafka.topic.html.send-html-mail}")
    private String htmlMailTopicName;
    @Value("${spring.kafka.topic.html.send_sms_notification}")
    private String htmlMailSmsNotificationTopicName;
    @Value("${spring.kafka.topic.html.send_push_notification}")
    private String htmlMailPushNotificationTopicName;

    @Value("${spring.kafka.topic.file.send-file-mail}")
    private String fileMailTopicName;
    @Value("${spring.kafka.topic.file.send_sms_notification}")
    private String fileMailSmsNotificationTopicName;
    @Value("${spring.kafka.topic.file.send_push_notification}")
    private String fileMailPushNotificationTopicName;

    private final KafkaTemplate<String, SendEmailRequestDto> kafkaTemplate;

    /**
     * @param kafkaTemplate : org.springframework.kafka.core.KafkaTemplate<String, SendEmailRequestDto>
     * Initializes the Kafka Template for system usage
     */
    public JsonKafkaProducer(KafkaTemplate<String, SendEmailRequestDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * @param data : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * Kafka Producer to Manage HTML Mail sending functionality
     */
    public void sendHtmlMail(SendEmailRequestDto data) {
        Message<SendEmailRequestDto> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, htmlMailTopicName)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * @param data : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * Kafka Producer to Manage HTML Mail specific Sms sending functionality
     */
    public void sendSmsNotification(SendEmailRequestDto data) {
        Message<SendEmailRequestDto> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, htmlMailSmsNotificationTopicName)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * @param data : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * Kafka Producer to Manage HTML Mail specific Push Notification sending functionality
     */
    public void sendPushNotification(SendEmailRequestDto data) {
        Message<SendEmailRequestDto> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, htmlMailPushNotificationTopicName)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * @param data : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * Kafka Producer to Manage HTML Mail with File attachments sending functionality
     */
    public void sendFileMail(SendFileEmailRequestDto data){
        Message<SendFileEmailRequestDto> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, fileMailTopicName)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * @param data : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * Kafka Producer to Manage File Mail Sms sending functionality
     */
    public void sendSmsNotification(SendFileEmailRequestDto data) {
        Message<SendFileEmailRequestDto> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, fileMailSmsNotificationTopicName)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * @param data : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * Kafka Producer to Manage File Mail Push Notification sending functionality
     */
    public void sendPushNotification(SendFileEmailRequestDto data) {
        Message<SendFileEmailRequestDto> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, fileMailPushNotificationTopicName)
                .build();

        kafkaTemplate.send(message);
    }
}
