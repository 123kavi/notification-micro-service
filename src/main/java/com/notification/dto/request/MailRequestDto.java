package com.notification.dto.request;

import com.notification.dto.ItemDetailsDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MailRequestDto implements Serializable {

    private String receiverEmail;
    private String topBannerAdUrl;
    private  String templateName;
    private Map<String, Object> content = new HashMap<>();
    private List<ItemDetailsDto> ordersList = new ArrayList<>();


}
