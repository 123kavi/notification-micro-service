package com.notification.controller;

import com.notification.component.EmailTemplateScheduler;
import com.notification.dto.request.MultiEmailTemplateFormatDto;
import com.notification.dto.request.SingleEmailTemplateFormatDto;
import com.notification.service.EmailSchedulerService;
import com.notification.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v5/email-scheduling")
public class EmailSchedulingController {
    private final EmailSchedulerService taskSchedulingService;
    private final EmailTemplateScheduler emailTemplateScheduler;

    @PostMapping(path = "/schedule-single-template-emails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> scheduleNewSingleEmailTemplateGroup(@Valid @RequestBody SingleEmailTemplateFormatDto singleTemplateDto,
                                                                      @RequestParam String jobId) {
        emailTemplateScheduler.setSingleEmailTemplateFormatDto(jobId, singleTemplateDto);
        taskSchedulingService.scheduleSingleEmailTemplateGroup(jobId, emailTemplateScheduler, singleTemplateDto, true);
        return ResponseEntity.ok(Utility.MSG_EMAIL_GROUP_SCHEDULED_SUCCESSFULLY);
    }

    @PostMapping(path = "/schedule-multi-template-emails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> scheduleNewMultiEmailTemplateGroup(@RequestBody MultiEmailTemplateFormatDto multiTemplateDto,
                                                                     @RequestParam String jobId) {
        emailTemplateScheduler.setMultiEmailTemplateFormatDto(jobId, multiTemplateDto);
        taskSchedulingService.scheduleMultiEmailTemplateGroup(jobId, emailTemplateScheduler, multiTemplateDto, true);
        return ResponseEntity.ok(Utility.MSG_EMAIL_GROUP_SCHEDULED_SUCCESSFULLY);
    }

    @DeleteMapping(path = "/terminate/{jobId}")
    public ResponseEntity<String> terminateScheduledEmailGroup(@PathVariable String jobId) {
        return ResponseEntity.ok(taskSchedulingService.terminateScheduledEmailGroup(jobId));
    }
}
