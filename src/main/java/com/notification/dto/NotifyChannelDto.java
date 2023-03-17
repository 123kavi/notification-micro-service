package com.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifyChannelDto implements Serializable {

    private long userId;
    private String msgTitle;
    private String msgBody;
}
