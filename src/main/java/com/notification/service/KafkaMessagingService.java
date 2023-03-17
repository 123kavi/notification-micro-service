package com.notification.service;

import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import com.notification.exception.SystemWarningException;

public interface KafkaMessagingService {

    String sendHtmlMailWithNotifications(SendEmailRequestDto emailRequestDto) throws SystemWarningException;

    String sendFileMailWithNotifications(SendFileEmailRequestDto emailRequestDto) throws SystemWarningException;
}
