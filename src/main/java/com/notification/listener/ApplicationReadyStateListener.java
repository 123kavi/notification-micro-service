//package com.cloudofgoods.notification.listener;
//
//import com.cloudofgoods.notification.component.EmailTemplateScheduler;
//import com.cloudofgoods.notification.dto.ItemDetailsDto;
//import com.cloudofgoods.notification.dto.request.*;
//import com.cloudofgoods.notification.entity.EmailScheduler;
//import com.cloudofgoods.notification.entity.EmailSchedulerDetail;
//import com.cloudofgoods.notification.enums.ScheduledStatus;
//import com.cloudofgoods.notification.exception.SystemRootException;
//import com.cloudofgoods.notification.repository.EmailSchedulerDetailRepository;
//import com.cloudofgoods.notification.repository.EmailSchedulerRepository;
//import com.cloudofgoods.notification.service.EmailSchedulerService;
//import com.cloudofgoods.notification.util.Utility;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class ApplicationReadyStateListener implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final EmailSchedulerService taskSchedulingService;
//    private final EmailTemplateScheduler emailTemplateScheduler;
//    private final EmailSchedulerRepository emailSchedulerRepository;
//    private final EmailSchedulerDetailRepository emailSchedulerDetailRepository;
//
//    /**
//     * This event is executed as late as conceivably possible to indicate that
//     * the application is ready to service requests.
//     */
//    @Override
//    public void onApplicationEvent(final ApplicationReadyEvent event) {
//        log.info("===============================================================");
//        log.info("-----Checking for Planned Active Schedules to be Restarted-----");
//        log.info("===============================================================");
//
//        List<EmailScheduler> allActiveSchedules = emailSchedulerRepository.findAllByScheduledStatusAndActiveStatus(
//                ScheduledStatus.RUNNING, Utility.ACTIVE_STATUS_TRUE);
//        if (null != allActiveSchedules && !allActiveSchedules.isEmpty()) {
//            log.info(String.format(Utility.LOG_TOTAL_NO_OF_SCHEDULES_TO_RESTART, allActiveSchedules.size()));
//            try {
//                for (EmailScheduler scheduler : allActiveSchedules) {
//                    log.info(String.format(Utility.LOG_SCHEDULER_RESTARTING, scheduler.getJobId(), scheduler.getCronExp()));
//                    if (scheduler.isMultipleTemplateGroup()) {
//                        reExecuteMultipleEmailTemplateScheduler(scheduler.getJobId(), extractMultiEmailTemplateData(scheduler));
//                    } else {
//                        reExecuteSingleEmailTemplateScheduler(scheduler.getJobId(), extractSingleEmailTemplateData(scheduler));
//                    }
//                    log.info(String.format(Utility.LOG_SCHEDULER_RESTARTED, scheduler.getJobId(), scheduler.getCronExp()));
//                }
//            } catch (JsonProcessingException e) {
//                throw new SystemRootException(e.getMessage(), e);
//            }
//            log.info("=============================================================================");
//            log.info("-All the Active Schedulers with \"Running\" State was Successfully Restarted-");
//            log.info("=============================================================================");
//        } else {
//            log.info("===========================================================================");
//            log.info("----Currently No Planned Active Schedulers were present to be Restarted----");
//            log.info("===========================================================================");
//        }
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param singleTemplateFormatDto : com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto
//     * Restart the existing single email template active schedulers due to server restart or any kind of failure
//     */
//    private void reExecuteSingleEmailTemplateScheduler(String jobId, SingleEmailTemplateFormatDto singleTemplateFormatDto) {
//        emailTemplateScheduler.setSingleEmailTemplateFormatDto(jobId, singleTemplateFormatDto);
//        taskSchedulingService.scheduleSingleEmailTemplateGroup(jobId, emailTemplateScheduler,
//                singleTemplateFormatDto, false);
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param multiTemplateFormatDto : com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
//     * Restart the existing multi email template active schedulers due to server restart or any kind of failure
//     */
//    private void reExecuteMultipleEmailTemplateScheduler(String jobId, MultiEmailTemplateFormatDto multiTemplateFormatDto) {
//        emailTemplateScheduler.setMultiEmailTemplateFormatDto(jobId, multiTemplateFormatDto);
//        taskSchedulingService.scheduleMultiEmailTemplateGroup(jobId, emailTemplateScheduler,
//                multiTemplateFormatDto, false);
//    }
//
//    @Cacheable(key = "#scheduler.jobId", value = Utility.CACHE_EXTRACT_SINGLE_EMAIL_TEMPLATE_DATA)
//    public SingleEmailTemplateFormatDto extractSingleEmailTemplateData(EmailScheduler scheduler) throws JsonProcessingException {
//        SingleEmailTemplateFormatDto singleEmailTemplateDto;
//        singleEmailTemplateDto = new SingleEmailTemplateFormatDto();
//        List<EmailSchedulerDetail> schedulerDetails = emailSchedulerDetailRepository
//                .findAllByEmailScheduler_JobId(scheduler.getJobId());
//        singleEmailTemplateDto.setTemplateName(schedulerDetails.get(0).getTemplateName());
////        singleEmailTemplateDto.setMinute(scheduler.getMinute());
////        singleEmailTemplateDto.setHour(scheduler.getHour());
////        singleEmailTemplateDto.setDayOfMonth(scheduler.getDayOfMonth());
////        singleEmailTemplateDto.setMonth(scheduler.getMonth());
////        singleEmailTemplateDto.setYear(scheduler.getYear());
////        singleEmailTemplateDto.setDayOfTheWeek(scheduler.getDayOfWeek());
//        singleEmailTemplateDto.setEmailRequestDtoList(getSingleSchedulerDetails(schedulerDetails));
//        return singleEmailTemplateDto;
//    }
//
//    /**
//     * @param schedulerDetails : java.util.List<com.cloudofgoods.notification.entity.EmailSchedulerDetail>
//     * @return java.util.List<com.cloudofgoods.notification.dto.request.SingleEmailTemplateRequestDto>
//     * @throws com.fasterxml.jackson.core.JsonProcessingException
//     */
//    private List<SingleEmailTemplateRequestDto> getSingleSchedulerDetails(List<EmailSchedulerDetail> schedulerDetails)
//            throws JsonProcessingException {
//        List<SingleEmailTemplateRequestDto> sendEmailRequestDtoList = new ArrayList<>();
//        for (EmailSchedulerDetail schedulerDetail : schedulerDetails) {
//            SingleEmailTemplateRequestDto emailRequestDto = new SingleEmailTemplateRequestDto();
//            emailRequestDto.setUserId(schedulerDetail.getUserId());
//            emailRequestDto.setMobileNo(schedulerDetail.getMobileNo());
//            emailRequestDto.setMailRequestDto(getMailDto(schedulerDetail.getReceiverEmail(), schedulerDetail.getTopBannerAdUrl(),
//                    schedulerDetail.getEmailContent(), schedulerDetail.getOrdersList()));
//            emailRequestDto.setNotification(getNotificationDto(schedulerDetail.getMessageContent()));
//            sendEmailRequestDtoList.add(emailRequestDto);
//        }
//        return sendEmailRequestDtoList;
//    }
//
//    /**
//     * @param scheduler : com.cloudofgoods.notification.entity.EmailScheduler
//     * @return com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
//     * @throws com.fasterxml.jackson.core.JsonProcessingException
//     */
//    private MultiEmailTemplateFormatDto extractMultiEmailTemplateData(EmailScheduler scheduler) throws JsonProcessingException {
//        MultiEmailTemplateFormatDto multiEmailTemplateDto;
//        multiEmailTemplateDto = new MultiEmailTemplateFormatDto();
//        multiEmailTemplateDto.setMinute(scheduler.getMinute());
//        multiEmailTemplateDto.setHour(scheduler.getHour());
//        multiEmailTemplateDto.setDayOfMonth(scheduler.getDayOfMonth());
//        multiEmailTemplateDto.setMonth(scheduler.getMonth());
//        multiEmailTemplateDto.setYear(scheduler.getYear());
//        multiEmailTemplateDto.setDayOfTheWeek(scheduler.getDayOfWeek());
//        multiEmailTemplateDto.setEmailRequestDtoList(getMultiSchedulerDetails(scheduler.getJobId()));
//        return multiEmailTemplateDto;
//    }
//
//    @Cacheable(key = "#jobId", value = Utility.CACHE_GET_MULTI_SCHEDULER_DETAILS)
//    public List<SendEmailRequestDto> getMultiSchedulerDetails(String jobId)
//            throws JsonProcessingException {
//        List<EmailSchedulerDetail> schedulerDetails = emailSchedulerDetailRepository.findAllByEmailScheduler_JobId(jobId);
//        List<SendEmailRequestDto> sendEmailRequestDtoList = new ArrayList<>();
//        for (EmailSchedulerDetail schedulerDetail : schedulerDetails) {
//            SendEmailRequestDto emailRequestDto = new SendEmailRequestDto();
//            emailRequestDto.setUserId(schedulerDetail.getUserId());
//            emailRequestDto.setTemplateName(schedulerDetail.getTemplateName());
//            emailRequestDto.setMobileNo(schedulerDetail.getMobileNo());
//            emailRequestDto.setMailRequestDto(getMailDto(schedulerDetail.getReceiverEmail(), schedulerDetail.getTopBannerAdUrl(),
//                    schedulerDetail.getEmailContent(), schedulerDetail.getOrdersList()));
//            emailRequestDto.setNotification(getNotificationDto(schedulerDetail.getMessageContent()));
//            sendEmailRequestDtoList.add(emailRequestDto);
//        }
//        return sendEmailRequestDtoList;
//    }
//
//    /**
//     * @param receiverEmail : java.lang.String
//     * @param topBannerAdUrl : java.lang.String
//     * @param mailContent : java.lang.String
//     * @param ordersList : java.lang.String
//     * @return com.cloudofgoods.notification.dto.request.MailRequestDto
//     * @throws com.fasterxml.jackson.core.JsonProcessingException
//     */
//    private MailRequestDto getMailDto(String receiverEmail, String topBannerAdUrl, String mailContent,
//                                      String ordersList) throws JsonProcessingException {
//        MailRequestDto mailRequestDto = new MailRequestDto();
//        mailRequestDto.setReceiverEmail(receiverEmail);
//        mailRequestDto.setTopBannerAdUrl(topBannerAdUrl);
//        mailRequestDto.setContent((Map<String, Object>) Utility.convertJsonStringToMap(mailContent));
//        mailRequestDto.setOrdersList((List<ItemDetailsDto>) Utility.convertJsonStringToList(ordersList));
//        return mailRequestDto;
//    }
//
//    /**
//     * @param content : java.lang.String
//     * @return com.cloudofgoods.notification.dto.request.NotificationRequestDto
//     * @throws com.fasterxml.jackson.core.JsonProcessingException
//     */
//    private NotificationRequestDto getNotificationDto(String content) throws JsonProcessingException {
//        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
//        notificationRequestDto.setContent((Map<String, Object>) Utility.convertJsonStringToMap(content));
//        return notificationRequestDto;
//    }
//}

package com.notification.listener;

import com.notification.component.EmailTemplateScheduler;
import com.notification.dto.ItemDetailsDto;
import com.cloudofgoods.notification.dto.request.*;
import com.notification.entity.EmailScheduler;
import com.notification.entity.EmailSchedulerDetail;
import com.notification.enums.ScheduledStatus;
import com.notification.exception.SystemRootException;
import com.notification.repository.EmailSchedulerDetailRepository;
import com.notification.repository.EmailSchedulerRepository;
import com.notification.service.EmailSchedulerService;
import com.notification.util.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.notification.dto.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationReadyStateListener implements ApplicationListener<ApplicationReadyEvent> {

    private final EmailSchedulerService taskSchedulingService;
    private final EmailTemplateScheduler emailTemplateScheduler;
    private final EmailSchedulerRepository emailSchedulerRepository;
    private final EmailSchedulerDetailRepository emailSchedulerDetailRepository;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("===============================================================");
        log.info("-----Checking for Planned Active Schedules to be Restarted-----");
        log.info("===============================================================");

        List<EmailScheduler> allActiveSchedules = emailSchedulerRepository.findAllByScheduledStatusAndActiveStatus(
                ScheduledStatus.RUNNING, Utility.ACTIVE_STATUS_TRUE);
        if (null != allActiveSchedules && !allActiveSchedules.isEmpty()) {
            log.info(String.format(Utility.LOG_TOTAL_NO_OF_SCHEDULES_TO_RESTART, allActiveSchedules.size()));
            try {
                for (EmailScheduler scheduler : allActiveSchedules) {
                    log.info(String.format(Utility.LOG_SCHEDULER_RESTARTING, scheduler.getJobId(), scheduler.getCronExp()));
                    if (scheduler.isMultipleTemplateGroup()) {
                        reExecuteMultipleEmailTemplateScheduler(scheduler.getJobId(), extractMultiEmailTemplateData(scheduler));
                    } else {
                        reExecuteSingleEmailTemplateScheduler(scheduler.getJobId(), extractSingleEmailTemplateData(scheduler));
                    }
                    log.info(String.format(Utility.LOG_SCHEDULER_RESTARTED, scheduler.getJobId(), scheduler.getCronExp()));
                }
            } catch (JsonProcessingException e) {
                throw new SystemRootException(e.getMessage(), e);
            }
            log.info("=============================================================================");
            log.info("-All the Active Schedulers with \"Running\" State was Successfully Restarted-");
            log.info("=============================================================================");
        } else {
            log.info("===========================================================================");
            log.info("----Currently No Planned Active Schedulers were present to be Restarted----");
            log.info("===========================================================================");
        }
    }

    /**
     * @param jobId : java.lang.String
     * @param singleTemplateFormatDto : com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto
     * Restart the existing single email template active schedulers due to server restart or any kind of failure
     */
    private void reExecuteSingleEmailTemplateScheduler(String jobId, SingleEmailTemplateFormatDto singleTemplateFormatDto) {
        emailTemplateScheduler.setSingleEmailTemplateFormatDto(jobId, singleTemplateFormatDto);
        taskSchedulingService.scheduleSingleEmailTemplateGroup(jobId, emailTemplateScheduler,
                singleTemplateFormatDto, false);
    }

    /**
     * @param jobId : java.lang.String
     * @param multiTemplateFormatDto : com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
     * Restart the existing multi email template active schedulers due to server restart or any kind of failure
     */
    private void reExecuteMultipleEmailTemplateScheduler(String jobId, MultiEmailTemplateFormatDto multiTemplateFormatDto) {
        emailTemplateScheduler.setMultiEmailTemplateFormatDto(jobId, multiTemplateFormatDto);
        taskSchedulingService.scheduleMultiEmailTemplateGroup(jobId, emailTemplateScheduler,
                multiTemplateFormatDto, false);
    }

    @Cacheable(key = "#scheduler.jobId", value = Utility.CACHE_EXTRACT_SINGLE_EMAIL_TEMPLATE_DATA)
    public SingleEmailTemplateFormatDto extractSingleEmailTemplateData(EmailScheduler scheduler) throws JsonProcessingException {
        SingleEmailTemplateFormatDto singleEmailTemplateDto;
        singleEmailTemplateDto = new SingleEmailTemplateFormatDto();
        List<EmailSchedulerDetail> schedulerDetails = emailSchedulerDetailRepository
                .findAllByEmailScheduler_JobId(scheduler.getJobId());
        singleEmailTemplateDto.setTemplateName(schedulerDetails.get(0).getTemplateName());
        singleEmailTemplateDto.setMinute(scheduler.getMinute());
        singleEmailTemplateDto.setHour(scheduler.getHour());
        singleEmailTemplateDto.setDayOfMonth(scheduler.getDayOfMonth());
        singleEmailTemplateDto.setMonth(scheduler.getMonth());
        singleEmailTemplateDto.setYear(scheduler.getYear());
        singleEmailTemplateDto.setDayOfTheWeek(scheduler.getDayOfWeek());
        singleEmailTemplateDto.setEmailRequestDtoList(getSingleSchedulerDetails(schedulerDetails));
        return singleEmailTemplateDto;
    }

    /**
     * @param schedulerDetails : java.util.List<com.cloudofgoods.notification.entity.EmailSchedulerDetail>
     * @return java.util.List<com.cloudofgoods.notification.dto.request.SingleEmailTemplateRequestDto>
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    private List<SingleEmailTemplateRequestDto> getSingleSchedulerDetails(List<EmailSchedulerDetail> schedulerDetails)
            throws JsonProcessingException {
        List<SingleEmailTemplateRequestDto> sendEmailRequestDtoList = new ArrayList<>();
        for (EmailSchedulerDetail schedulerDetail : schedulerDetails) {
            SingleEmailTemplateRequestDto emailRequestDto = new SingleEmailTemplateRequestDto();
            emailRequestDto.setUserId(schedulerDetail.getUserId());
            emailRequestDto.setMobileNo(schedulerDetail.getMobileNo());
            emailRequestDto.setMail(getMailDto(schedulerDetail.getReceiverEmail(), schedulerDetail.getTopBannerAdUrl(),
                    schedulerDetail.getEmailContent(), schedulerDetail.getOrdersList()));
            emailRequestDto.setNotification(getNotificationDto(schedulerDetail.getMessageContent()));
            sendEmailRequestDtoList.add(emailRequestDto);
        }
        return sendEmailRequestDtoList;
    }

    /**
     * @param scheduler : com.cloudofgoods.notification.entity.EmailScheduler
     * @return com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    private MultiEmailTemplateFormatDto extractMultiEmailTemplateData(EmailScheduler scheduler) throws JsonProcessingException {
        MultiEmailTemplateFormatDto multiEmailTemplateDto;
        multiEmailTemplateDto = new MultiEmailTemplateFormatDto();
        multiEmailTemplateDto.setMinute(scheduler.getMinute());
        multiEmailTemplateDto.setHour(scheduler.getHour());
        multiEmailTemplateDto.setDayOfMonth(scheduler.getDayOfMonth());
        multiEmailTemplateDto.setMonth(scheduler.getMonth());
        multiEmailTemplateDto.setYear(scheduler.getYear());
        multiEmailTemplateDto.setDayOfTheWeek(scheduler.getDayOfWeek());
        multiEmailTemplateDto.setEmailRequestDtoList(getMultiSchedulerDetails(scheduler.getJobId()));
        return multiEmailTemplateDto;
    }

    @Cacheable(key = "#jobId", value = Utility.CACHE_GET_MULTI_SCHEDULER_DETAILS)
    public List<SendEmailRequestDto> getMultiSchedulerDetails(String jobId)
            throws JsonProcessingException {
        List<EmailSchedulerDetail> schedulerDetails = emailSchedulerDetailRepository.findAllByEmailScheduler_JobId(jobId);
        List<SendEmailRequestDto> sendEmailRequestDtoList = new ArrayList<>();
        for (EmailSchedulerDetail schedulerDetail : schedulerDetails) {
            SendEmailRequestDto emailRequestDto = new SendEmailRequestDto();
            emailRequestDto.setUserId(schedulerDetail.getUserId());
            emailRequestDto.setTemplateName(schedulerDetail.getTemplateName());
            emailRequestDto.setMobileNo(schedulerDetail.getMobileNo());
            emailRequestDto.setMail(getMailDto(schedulerDetail.getReceiverEmail(), schedulerDetail.getTopBannerAdUrl(),
                    schedulerDetail.getEmailContent(), schedulerDetail.getOrdersList()));
            emailRequestDto.setNotification(getNotificationDto(schedulerDetail.getMessageContent()));
            sendEmailRequestDtoList.add(emailRequestDto);
        }
        return sendEmailRequestDtoList;
    }

    /**
     * @param receiverEmail : java.lang.String
     * @param topBannerAdUrl : java.lang.String
     * @param mailContent : java.lang.String
     * @param ordersList : java.lang.String
     * @return com.cloudofgoods.notification.dto.request.MailRequestDto
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    private MailRequestDto getMailDto(String receiverEmail, String topBannerAdUrl, String mailContent,
                                      String ordersList) throws JsonProcessingException {
        MailRequestDto mailRequestDto = new MailRequestDto();
        mailRequestDto.setReceiverEmail(receiverEmail);
        mailRequestDto.setTopBannerAdUrl(topBannerAdUrl);
        mailRequestDto.setContent((Map<String, Object>) Utility.convertJsonStringToMap(mailContent));
        mailRequestDto.setOrdersList((List<ItemDetailsDto>) Utility.convertJsonStringToList(ordersList));
        return mailRequestDto;
    }

    /**
     * @param content : java.lang.String
     * @return com.cloudofgoods.notification.dto.request.NotificationRequestDto
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    private NotificationRequestDto getNotificationDto(String content) throws JsonProcessingException {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        notificationRequestDto.setContent((Map<String, Object>) Utility.convertJsonStringToMap(content));
        return notificationRequestDto;
    }
}
