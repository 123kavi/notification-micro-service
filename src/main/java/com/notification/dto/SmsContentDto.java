package com.notification.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SmsContentDto implements Serializable {

    private static final long serialVersionUID = 2178793491644748302L;
    private String templateName;
    private String messageTitle;
    private String messageBody;
}
