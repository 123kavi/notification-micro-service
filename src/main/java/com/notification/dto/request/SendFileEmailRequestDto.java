package com.notification.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SendFileEmailRequestDto implements Serializable {

    private static final long serialVersionUID = 2545455971647102101L;
    private long userId;
    private String templateName;
    private String mobileNo;
    private MailRequestDto mail;
    private NotificationRequestDto notification;
    private String filename;
    private String base64file;
}
