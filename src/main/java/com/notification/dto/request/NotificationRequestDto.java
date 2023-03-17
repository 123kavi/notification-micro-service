package com.notification.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class NotificationRequestDto implements Serializable {

    private Map<String, Object> content = new HashMap<>();
}
