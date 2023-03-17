package com.notification.dto;

import com.notification.dto.request.MailRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter


public class ScheduleEmailRequestDTO implements Serializable {

    private String email;
    private String subject;
    private String body;
    private LocalDateTime dateTime;
    private Boolean isActive;
    private MailRequestDto mailContentDto;

}
