package com.notification.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MultiEmailTemplateFormatDto implements Serializable {

    private static final long serialVersionUID = 8433948905174953080L;
    private Integer minute;
    private Integer hour;
    private Integer dayOfMonth;
    private Integer month;
    private Integer year;
    private Integer dayOfTheWeek;
    private List<SendEmailRequestDto> emailRequestDtoList = new ArrayList<>();
}