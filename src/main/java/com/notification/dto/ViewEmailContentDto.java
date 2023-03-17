package com.notification.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ViewEmailContentDto implements Serializable {

    private static final long serialVersionUID = -3489326941646285731L;
    private String templateName;
    private String subject;
    private String content;
}
