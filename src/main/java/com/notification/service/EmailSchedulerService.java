package com.notification.service;

import com.notification.dto.request.MultiEmailTemplateFormatDto;
import com.notification.dto.request.SingleEmailTemplateFormatDto;

public interface EmailSchedulerService {

    void scheduleSingleEmailTemplateGroup(String jobId, Runnable tasklet, SingleEmailTemplateFormatDto singleEmailTemplateFormatDto, boolean isManuallyTrigger);

    void scheduleMultiEmailTemplateGroup(String jobId, Runnable tasklet, MultiEmailTemplateFormatDto multiEmailTemplateFormatDto, boolean isManuallyTrigger);

    String terminateScheduledEmailGroup(String jobId);
}
