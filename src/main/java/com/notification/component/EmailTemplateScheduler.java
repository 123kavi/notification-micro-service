package com.notification.component;

import com.notification.dto.ItemDetailsDto;
import com.notification.dto.request.*;
import com.notification.entity.EmailScheduler;
import com.notification.entity.EmailSchedulerDetail;
import com.notification.entity.EmailSchedulerLogNew;
import com.notification.entity.MailContent;
import com.notification.enums.ScheduledStatus;
import com.notification.exception.SystemRootException;
import com.notification.exception.SystemWarningException;
import com.notification.repository.EmailSchedulerDetailRepository;
import com.notification.repository.EmailSchedulerLogRepository;
import com.notification.repository.EmailSchedulerRepository;
import com.notification.repository.MailContentRepository;
import com.notification.service.KafkaMessagingService;
import com.notification.util.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.notification.dto.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailTemplateScheduler implements Runnable {

    private final KafkaMessagingService messagingService;
    private final MailContentRepository mailContentRepository;
    private final EmailSchedulerRepository emailSchedulerRepository;
    private final EmailSchedulerDetailRepository emailSchedulerDetailRepository;
    private final EmailSchedulerLogRepository emailSchedulerLogRepository;
    private final CacheManager cacheManager;

    private MultiEmailTemplateFormatDto multiEmailTemplateFormatDto;
    private SingleEmailTemplateFormatDto singleEmailTemplateFormatDto;
    private boolean isMultipleEmailTemplate = false;
    private String jobId;

    /**
     * EmailTemplateScheduler Execution Triggering Point
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        try {
            if (isMultipleEmailTemplate) {
                triggerMultipleEmailTemplateScheduler();
            } else {
                triggerSingleEmailTemplateScheduler();
            }
        } catch (JsonProcessingException e) {
            throw new SystemRootException(e.getMessage(), e);
        }
    }

    /**
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     * Triggers Single Email Template Scheduler
     */
    private void triggerSingleEmailTemplateScheduler() throws JsonProcessingException {
        if (null == singleEmailTemplateFormatDto.getEmailRequestDtoList()
                || singleEmailTemplateFormatDto.getEmailRequestDtoList().isEmpty()) {
            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
        }
        log.info(String.format(Utility.LOG_SINGLE_EMAIL_TEMPLATE_SCHEDULER_STARTED,
                jobId, singleEmailTemplateFormatDto.getMinute(), singleEmailTemplateFormatDto.getHour(),
                singleEmailTemplateFormatDto.getDayOfMonth(), singleEmailTemplateFormatDto.getMonth(),
                singleEmailTemplateFormatDto.getDayOfTheWeek(),
                singleEmailTemplateFormatDto.getEmailRequestDtoList().size()));

        EmailScheduler scheduler = emailSchedulerRepository.findFirstByJobId(jobId);
        if (null != scheduler) {
            log.info(String.format(Utility.LOG_SINGLE_EMAIL_TEMPLATE_SCHEDULER_RUNNING, jobId, scheduler.getCronExp()));
            scheduler.setScheduledStatus(ScheduledStatus.RUNNING);
            scheduler.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            emailSchedulerRepository.save(scheduler);

            String message;
            for (SingleEmailTemplateRequestDto scheduleDto : singleEmailTemplateFormatDto.getEmailRequestDtoList()) {
                message = messagingService.sendHtmlMailWithNotifications(new SendEmailRequestDto(
                        scheduleDto.getUserId(),
                        singleEmailTemplateFormatDto.getTemplateName(),
                        scheduleDto.getMobileNo(),
                        scheduleDto.getMail(),
                        scheduleDto.getNotification()));
                saveSchedulerDetails(scheduleDto.getUserId(),
                        singleEmailTemplateFormatDto.getTemplateName(), scheduleDto.getMobileNo(),
                        scheduleDto.getMail().getReceiverEmail(), scheduleDto.getMail().getTopBannerAdUrl(),
                        scheduleDto.getMail().getOrdersList(), scheduleDto.getMail().getContent(),
                        scheduleDto.getNotification().getContent(), scheduler);
                saveEmailSchedulerLogDetails(scheduleDto.getMail(), scheduleDto.getMobileNo(), message);
            }

            scheduler.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            emailSchedulerRepository.save(scheduler);
        } else {
            throw new SystemRootException(String.format(Utility.EX_SINGLE_EMAIL_SCHEDULER_FAILS, jobId));
        }
    }

    /**
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     * Triggers Multi Email Template Scheduler
     */
    private void triggerMultipleEmailTemplateScheduler() throws JsonProcessingException {
        if (null == multiEmailTemplateFormatDto.getEmailRequestDtoList()
                || multiEmailTemplateFormatDto.getEmailRequestDtoList().isEmpty()) {
            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
        }
        log.info(String.format(Utility.LOG_MULTIPLE_EMAIL_TEMPLATE_SCHEDULER_STARTED,
                jobId, multiEmailTemplateFormatDto.getMinute(), multiEmailTemplateFormatDto.getHour(),
                multiEmailTemplateFormatDto.getDayOfMonth(), multiEmailTemplateFormatDto.getMonth(),
                multiEmailTemplateFormatDto.getDayOfTheWeek(),
                multiEmailTemplateFormatDto.getEmailRequestDtoList().size()));

        EmailScheduler emailScheduler = emailSchedulerRepository.findFirstByJobId(jobId);
        if (null != emailScheduler) {
            emailScheduler.setScheduledStatus(ScheduledStatus.RUNNING);
            emailScheduler.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            emailSchedulerRepository.save(emailScheduler);

            String message;
            for (SendEmailRequestDto emailRequestDto : multiEmailTemplateFormatDto.getEmailRequestDtoList()) {
                message = messagingService.sendHtmlMailWithNotifications(emailRequestDto);
                saveSchedulerDetails(emailRequestDto.getUserId(),
                        singleEmailTemplateFormatDto.getTemplateName(), emailRequestDto.getMobileNo(),
                        emailRequestDto.getMail().getReceiverEmail(), emailRequestDto.getMail().getTopBannerAdUrl(),
                        emailRequestDto.getMail().getOrdersList(), emailRequestDto.getMail().getContent(),
                        emailRequestDto.getNotification().getContent(), emailScheduler);
                saveEmailSchedulerLogDetails(emailRequestDto.getMail(), emailRequestDto.getMobileNo(), message);
            }

            emailScheduler.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            emailSchedulerRepository.save(emailScheduler);
        } else {
            throw new SystemRootException(String.format(Utility.EX_MULTI_EMAIL_SCHEDULER_FAILS, jobId));
        }
    }

    /**
     * @param scheduleDto : com.cl.notification.dto.request.MailRequestDto
     * @param mobileNo : java.lang.String
     * @param message : java.lang.String
     */
    private void saveEmailSchedulerLogDetails(MailRequestDto scheduleDto, String mobileNo, String message) {
        MailContent mailContent = mailContentRepository.findFirstByTemplateName(singleEmailTemplateFormatDto.getTemplateName());
        EmailScheduler emailScheduler = emailSchedulerRepository.findFirstByJobId(jobId);

        validateSchedulerLogDetails(mailContent, emailScheduler);
        EmailSchedulerLogNew emailSchedulerLog = new EmailSchedulerLogNew();
        emailSchedulerLog.setEmail(scheduleDto.getReceiverEmail());
        emailSchedulerLog.setMobileNumber(mobileNo);
        emailSchedulerLog.setEmailScheduler(emailScheduler);
      //  emailSchedulerLog.setMailContent(mailContent);
        emailSchedulerLog.setResponse(message);
        emailSchedulerLog.setExecutedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        emailSchedulerLogRepository.save(emailSchedulerLog);
    }

    /**
     * @param userId : java.lang.Long
     * @param templateName : java.lang.String
     * @param mobileNo : java.lang.String
     * @param receiverEmail : java.lang.String
     * @param topBannerUrl : java.lang.String
     * @param orderItemDetails : java.util.List<com.cl.notification.dto.ItemDetailsDto>
     * @param emailContent : java.util.Map<java.lang.String, java.lang.Object>
     * @param messageContent : java.util.Map<java.lang.String, java.lang.Object>
     * @param emailScheduler : com.cl.notification.entity.EmailScheduler
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    private void saveSchedulerDetails(Long userId, String templateName, String mobileNo, String receiverEmail,
                                      String topBannerUrl, List<ItemDetailsDto> orderItemDetails,
                                      Map<String, Object> emailContent, Map<String, Object> messageContent,
                                      EmailScheduler emailScheduler) throws JsonProcessingException {

        EmailSchedulerDetail schedulerDetail = new EmailSchedulerDetail();
        schedulerDetail.setUserId(userId);
        schedulerDetail.setTemplateName(templateName);
        schedulerDetail.setMobileNo(mobileNo);
        schedulerDetail.setReceiverEmail(receiverEmail);
        schedulerDetail.setTopBannerAdUrl(topBannerUrl);
        schedulerDetail.setOrdersList(Utility.convertListIntoJsonString(orderItemDetails));
        schedulerDetail.setEmailContent(Utility.convertMapIntoJsonString(emailContent));
        schedulerDetail.setMessageContent(Utility.convertMapIntoJsonString(messageContent));
        schedulerDetail.setEmailScheduler(emailScheduler);
        schedulerDetail.setInsertedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        emailSchedulerDetailRepository.save(schedulerDetail);
        evictGetMultiSchedulerDetails(Utility.CACHE_GET_MULTI_SCHEDULER_DETAILS, emailScheduler.getJobId());
        evictExtractSingleEmailTemplateData(Utility.CACHE_EXTRACT_SINGLE_EMAIL_TEMPLATE_DATA, emailScheduler.getJobId());
    }

    @CacheEvict(value = Utility.CACHE_GET_MULTI_SCHEDULER_DETAILS, key = "#jobId")
    public void evictGetMultiSchedulerDetails(String extractMultiEmailTemplateFormatData, String jobId) {
        Objects.requireNonNull(cacheManager.getCache(extractMultiEmailTemplateFormatData)).evict(jobId);
    }

    @CacheEvict(value = Utility.CACHE_EXTRACT_SINGLE_EMAIL_TEMPLATE_DATA, key = "#jobId")
    public void evictExtractSingleEmailTemplateData(String extractSingleEmailTemplateData, String jobId) {
        Objects.requireNonNull(cacheManager.getCache(extractSingleEmailTemplateData)).evict(jobId);
    }

    /**
     * @param mailContent : com.cl.notification.entity.MailContent
     * @param emailScheduler : com.cl.notification.entity.EmailScheduler
     * Validates Scheduler Log Details
     */
    private void validateSchedulerLogDetails(MailContent mailContent, EmailScheduler emailScheduler) {
        if (null == mailContent) {
            throw new SystemRootException(Utility.EX_INVALID_TEMPLATE_NAME);
        }
        if (null == emailScheduler) {
            throw new SystemRootException(Utility.EX_JOB_ID_NOT_FOUND);
        }
    }

    /**
     * @param jobId : java.lang.String
     * @param singleEmailTemplateFormatDto : com.cl.notification.dto.request.SingleEmailTemplateFormatDto
     */
    public void setSingleEmailTemplateFormatDto(String jobId, SingleEmailTemplateFormatDto singleEmailTemplateFormatDto) {
        this.setJobId(jobId);
        this.setIsMultipleEmailTemplate(false);
        this.singleEmailTemplateFormatDto = singleEmailTemplateFormatDto;
    }

    /**
     * @param jobId : java.lang.String
     * @param multiEmailTemplateFormatDto : com.cl.notification.dto.request.MultiEmailTemplateFormatDto
     */
    public void setMultiEmailTemplateFormatDto(String jobId, MultiEmailTemplateFormatDto multiEmailTemplateFormatDto) {
        this.setJobId(jobId);
        this.setIsMultipleEmailTemplate(true);
        this.multiEmailTemplateFormatDto = multiEmailTemplateFormatDto;
    }

    /**
     * @param jobId : java.lang.String
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * @param isMultipleEmailTemplate : boolean
     * Setter to modify the state of indicating whether the current scheduler is going to consume
     * a Single/Multi Emails format
     */
    public void setIsMultipleEmailTemplate(boolean isMultipleEmailTemplate) {
        this.isMultipleEmailTemplate = isMultipleEmailTemplate;
    }
}