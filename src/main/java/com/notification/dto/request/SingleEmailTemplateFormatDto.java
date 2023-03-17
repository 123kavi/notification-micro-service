package com.notification.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SingleEmailTemplateFormatDto implements Serializable {

    private static final long serialVersionUID = 1454767899757466404L;
    private Integer minute;
    private Integer hour;
    private Integer dayOfMonth;
    private Integer month;
    private Integer year;
    private Integer dayOfTheWeek;

    private String templateName;
    private List<SingleEmailTemplateRequestDto> emailRequestDtoList = new ArrayList<>();

}