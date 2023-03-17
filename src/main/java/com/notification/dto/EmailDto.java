package com.notification.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class EmailDto implements Serializable {

    private static final long serialVersionUID = 895738670169705879L;
    private String templateName;
    private String editedEmailContent;
    private String editedEmailSubject;
}
