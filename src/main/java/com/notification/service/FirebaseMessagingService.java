package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.dto.request.NotificationRequestDto;
import com.notification.exception.SystemWarningException;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface FirebaseMessagingService {

    void saveOrUpdateFirebaseNotificationDetails(NotificationDto notificationDto, String userName);

    String notifyAllChannels(long userId, String templateName, NotificationRequestDto notificationRequestDto) throws FirebaseMessagingException;

    void updateFcmRefreshToken(long userId, String oldToken, String newToken, String userName);

    boolean verifyTokenValidity(long userId, String token) throws SystemWarningException;
}
