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
public class FeatureFlagsDto implements Serializable {

    private long featureFlagId;
    private String featureFlagName;
    private String featureFlagDescription;
    private boolean status;

    public boolean getStatus() {
        return status;
    }
}
