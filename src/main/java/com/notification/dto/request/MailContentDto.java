package com.notification.dto.request;

import com.notification.dto.ItemDetailsDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MailContentDto implements Serializable {

    private String receiverEmail;
    private String topBannerAdUrl;
    private  String templateName;
//    private Map<String, Object> content = new HashMap<>();
    private List<ContentDTO> contentDTOS = new ArrayList<>();
    private List<ItemDetailsDto> ordersList = new ArrayList<>();


}
