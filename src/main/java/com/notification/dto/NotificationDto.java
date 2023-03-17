package com.notification.dto;

import com.notification.enums.DevicePlatform;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NotificationDto implements Serializable {

    private long userId;
    private String token;
    private DevicePlatform platform;
}
