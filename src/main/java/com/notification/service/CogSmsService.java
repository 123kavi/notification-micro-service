package com.notification.service;

import com.notification.dto.SmsContentDto;
import com.notification.dto.request.NotificationRequestDto;

public interface CogSmsService {

    Object sendSms(long userId, String templateName, String mobileNo, NotificationRequestDto notification);

    SmsContentDto getMessageContent(String templateName);

    String updateMessageContent(SmsContentDto smsDto, String userName);
}
