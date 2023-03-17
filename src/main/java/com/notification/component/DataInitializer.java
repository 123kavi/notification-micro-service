
package com.notification.component;


import com.notification.dto.ScheduleEmailRequestDTO;
import com.notification.dto.request.MailRequestDto;
import com.notification.entity.ScheduleEmailRequest;
import com.notification.entity.ScheduleEmailResponse;
import com.notification.repository.ScheduledEmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {


    private final Scheduler scheduler;

    @Autowired
    private ScheduledEmailRepository scheduleEmailRequestRepository;

    @EventListener(value = ApplicationReadyEvent.class)
    public List<ScheduleEmailResponse> scheduleEmail() {
        try {
//            Instant instant = Instant.now();
//            LocalDateTime now = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

//            ZoneId localZone = ZoneId.systemDefault();
//            Instant instant = Instant.now();
//            LocalDateTime now = LocalDateTime.ofInstant(instant, localZone);

//            Date local = new Date();
//            DateTimeZone zone = DateTimeZone.getDefault();
//            long utc = zone.convertLocalToUTC(local.getTime(), false);
//            System.out.println("UTC: " + new Date(utc));

            String epochTime = String.valueOf(Instant.now().toEpochMilli());
            Instant utcInstant = new Date(Long.parseLong(epochTime)).toInstant();
            ZonedDateTime there = ZonedDateTime.ofInstant(utcInstant, ZoneId.of("UTC"));
            System.out.println(utcInstant);
            LocalDateTime here = there.toLocalDateTime();

            List<ScheduleEmailRequest> scheduleEmailRequests = scheduleEmailRequestRepository.findAllByDateTimeGreaterThanEqualAndIsActiveEquals(here, true);
            List<ScheduleEmailResponse> scheduleEmailResponses = new ArrayList<>();
            for (ScheduleEmailRequest emailRequestEntity : scheduleEmailRequests) {
                ScheduleEmailRequestDTO scheduleEmailRequestDTO = new ScheduleEmailRequestDTO();
                scheduleEmailRequestDTO.setEmail(emailRequestEntity.getEmail());
                scheduleEmailRequestDTO.setSubject(emailRequestEntity.getSubject());
                scheduleEmailRequestDTO.setBody(emailRequestEntity.getBody());
                scheduleEmailRequestDTO.setDateTime(emailRequestEntity.getDateTime());
                scheduleEmailRequestDTO.setIsActive(emailRequestEntity.getIsActive());
                MailRequestDto mailRequestDto = new MailRequestDto();
                mailRequestDto.setReceiverEmail(emailRequestEntity.getMailContent1().getSendBy());
                mailRequestDto.setTopBannerAdUrl(emailRequestEntity.getMailContent1().getTopBannerAdUrl());
                mailRequestDto.setTemplateName(emailRequestEntity.getMailContent1().getTemplateName());

                String input = emailRequestEntity.getMailContent1().getContent();
                Map<String, Object> valuemap = new HashMap<>();

                input = input.substring(1, input.length() - 1);
                String[] pairs = input.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length >= 2) { // check if there are at least 2 elements
                        String key = keyValue[0].substring(1, keyValue[0].length() - 0);
                        String value = keyValue[1];
                        valuemap.put(key, value);}
                }
                System.out.println(mailRequestDto.getContent());
                mailRequestDto.setContent(valuemap);
                scheduleEmailRequestDTO.setMailContentDto(mailRequestDto);
                JobDetail jobDetail = buildJobDetail(scheduleEmailRequestDTO);
                Trigger trigger = buildJobTrigger(jobDetail, emailRequestEntity.getDateTime());
                scheduler.scheduleJob(jobDetail, trigger);
                ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
                scheduleEmailResponses.add(scheduleEmailResponse);
            }
            return scheduleEmailResponses;
        } catch (SchedulerException ex) {
            log.error("Error scheduling email", ex);
            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false, "Error scheduling email. Please try later!");
            return (List<ScheduleEmailResponse>) scheduleEmailResponse;
        }
    }

    private JobDetail buildJobDetail(ScheduleEmailRequestDTO scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());
        jobDataMap.put("abc", scheduleEmailRequest.getMailContentDto());
        return JobBuilder.newJob(EmailJob.class).withIdentity(UUID.randomUUID().toString(), "email-jobs").withDescription("Send Email Job").usingJobData(jobDataMap).storeDurably().build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, LocalDateTime startAt) {
        Instant instant = startAt.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger"
                ).startAt(Date.from(date.toInstant())).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
    }
}
