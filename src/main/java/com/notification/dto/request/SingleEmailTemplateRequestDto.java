package com.notification.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SingleEmailTemplateRequestDto implements Serializable {

    private static final long serialVersionUID = 5482299160976425404L;
    private long userId;
    private String mobileNo;
    private MailRequestDto mail;
    private NotificationRequestDto notification;
}