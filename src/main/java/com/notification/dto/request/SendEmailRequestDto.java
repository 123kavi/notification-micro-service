package com.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequestDto implements Serializable {

    private static final long serialVersionUID = 5083293304184517904L;
    private long userId;
    private String templateName;
    private String mobileNo;
    private MailRequestDto mail;
    private NotificationRequestDto notification;
}
