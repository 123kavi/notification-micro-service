
package com.notification.controller;


import com.notification.component.EmailJob;
import com.notification.dto.ScheduleEmailRequestDTO;
import com.notification.entity.ScheduleMailContent;
import com.notification.entity.ScheduleEmailRequest;
import com.notification.entity.ScheduleEmailResponse;
import com.notification.repository.MailContentRepository;
import com.notification.repository.ScheduledEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;



@Slf4j
@RestController
public class EmailJobSchedulerController {
    private final MailContentRepository mailContentRepository;
    private final Scheduler scheduler;

private final ScheduledEmailRepository scheduleEmailRequestRepository;
    public EmailJobSchedulerController(Scheduler scheduler, ScheduledEmailRepository scheduleEmailRequestRepository,
                                       MailContentRepository mailContentRepository) {
        this.scheduler = scheduler;
        this.scheduleEmailRequestRepository = scheduleEmailRequestRepository;
        this.mailContentRepository = mailContentRepository;
    }

    @PostMapping("/scheduleEmail")
    public ResponseEntity<ScheduleEmailResponse> scheduleEmail(@Valid @RequestBody ScheduleEmailRequestDTO scheduleEmailRequestdto) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequestdto.getDateTime(), ZoneOffset.UTC);
            if (dateTime.isBefore(ZonedDateTime.now(ZoneOffset.UTC))) {
                ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false, "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleEmailResponse);
            }
            ScheduleEmailRequest emailRequestEntity = new ScheduleEmailRequest();
            emailRequestEntity.setEmail(scheduleEmailRequestdto.getEmail());
            emailRequestEntity.setSubject(scheduleEmailRequestdto.getSubject());
            emailRequestEntity.setBody(scheduleEmailRequestdto.getBody());
            emailRequestEntity.setDateTime(scheduleEmailRequestdto.getDateTime());

            ScheduleMailContent mailContent1 = new ScheduleMailContent();
            mailContent1.setContent(scheduleEmailRequestdto.getMailContentDto().getContent().toString());
            mailContent1.setTemplateName(scheduleEmailRequestdto.getMailContentDto().getTemplateName());
            mailContent1.setTopBannerAdUrl(scheduleEmailRequestdto.getMailContentDto().getTopBannerAdUrl());
            mailContent1.setSendBy(scheduleEmailRequestdto.getMailContentDto().getReceiverEmail());
            mailContent1.setLastUpdatedDateTime(Timestamp.from(Instant.now()));
            mailContent1.setLastUpdatedUser("clouds of good");

            emailRequestEntity.setMailContent1(mailContent1);
            try {
                emailRequestEntity.setIsActive((scheduleEmailRequestdto.getIsActive()));
                ScheduleEmailRequest savedEmailRequest = scheduleEmailRequestRepository.save(emailRequestEntity);
            }catch (Exception e){
             e.printStackTrace();
            }
            JobDetail jobDetail = buildJobDetail(scheduleEmailRequestdto);
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);
            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            log.error("Error scheduling email", ex);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false, "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }
    private JobDetail buildJobDetail(ScheduleEmailRequestDTO scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());
        jobDataMap.put("abc", scheduleEmailRequest.getMailContentDto());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
