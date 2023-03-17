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
public class ItemDetailsDto implements Serializable {

    private static final long serialVersionUID = 5652090814085206354L;
    private String productName;
    private int productQuantity;
    private String productUrl;
}
