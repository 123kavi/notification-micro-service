//package com.cloudofgoods.notification.service.impl;
//
//import com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto;
//import com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto;
//import com.cloudofgoods.notification.entity.EmailScheduler;
//import com.cloudofgoods.notification.entity.Shedlock;
//import com.cloudofgoods.notification.enums.ScheduledStatus;
//import com.cloudofgoods.notification.exception.SystemWarningException;
//import com.cloudofgoods.notification.repository.EmailSchedulerRepository;
//import com.cloudofgoods.notification.repository.ShedLockRepository;
//import com.cloudofgoods.notification.service.EmailSchedulerService;
//import com.cloudofgoods.notification.util.Utility;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
//import net.javacrumbs.shedlock.core.LockConfiguration;
//import net.javacrumbs.shedlock.core.LockProvider;
//import net.javacrumbs.shedlock.core.LockingTaskExecutor;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.time.Duration;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.ScheduledFuture;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class EmailSchedulerServiceImpl implements EmailSchedulerService {
//
//    private final TaskScheduler taskScheduler;
//    private final EmailSchedulerRepository emailSchedulerRepository;
//    private final ShedLockRepository shedLockRepository;
//    private final CacheManager cacheManager;
//    private final LockProvider lockProvider;
//
//    private static final Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
//
//    /**
//     * @param jobId : java.lang.String
//     * @param tasklet : java.lang.Runnable
//     * @param formatDto : com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto
//     * @param isManuallyTrigger : boolean
//     */
//    @Override
//    public void scheduleSingleEmailTemplateGroup(String jobId, Runnable tasklet, SingleEmailTemplateFormatDto formatDto,
//                                                 boolean isManuallyTrigger) {
//        validateEmailGroupData(jobId, formatDto, isManuallyTrigger);
//        String cronExpression = generateCronExp(formatDto.getYear(), formatDto.getMonth(), formatDto.getDayOfMonth(),
//                formatDto.getHour(), formatDto.getMinute(), formatDto.getDayOfTheWeek());
//        log.info("scheduler  cron expression:: "+cronExpression);
//        saveSchedulerDetails(jobId, formatDto.getEmailRequestDtoList().size(), false,
//                formatDto.getMinute(), formatDto.getHour(), formatDto.getDayOfMonth(), formatDto.getMonth(),
//                formatDto.getYear(), formatDto.getDayOfTheWeek(), cronExpression);
//        putScheduledTaskToMap(jobId, tasklet, cronExpression);
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param tasklet : java.lang.Runnable
//     * @param formatDto : com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
//     * @param isManuallyTrigger : boolean
//     */
//    public void scheduleMultiEmailTemplateGroup(String jobId, Runnable tasklet, MultiEmailTemplateFormatDto formatDto,
//                                                boolean isManuallyTrigger) {
//        validateEmailGroupData(jobId, formatDto, isManuallyTrigger);
//        String cronExpression = generateCronExp(formatDto.getYear(), formatDto.getMonth(), formatDto.getDayOfMonth(),
//                formatDto.getHour(), formatDto.getMinute(), formatDto.getDayOfTheWeek());
//        saveSchedulerDetails(jobId, formatDto.getEmailRequestDtoList().size(), true,
//                formatDto.getMinute(), formatDto.getHour(), formatDto.getDayOfMonth(), formatDto.getMonth(),
//                formatDto.getYear(), formatDto.getDayOfTheWeek(), cronExpression);
//        putScheduledTaskToMap(jobId, tasklet, cronExpression);
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param singleEmailTemplateFormatDto : com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto
//     * @param isManuallyTrigger : boolean
//     */
//    private void validateEmailGroupData(String jobId, SingleEmailTemplateFormatDto singleEmailTemplateFormatDto,
//                                        boolean isManuallyTrigger) {
//        if (isManuallyTrigger && emailSchedulerRepository.existsByJobId(jobId)) {
//            throw new SystemWarningException(String.format(Utility.EX_JOB_ID_ALREADY_TAKEN, jobId));
//        }
//        if (null == singleEmailTemplateFormatDto) {
//            throw new SystemWarningException(Utility.EX_REQUEST_DATA_REQUIRED);
//        }
//        if (Utility.isNullOrEmpty(singleEmailTemplateFormatDto.getTemplateName())) {
//            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
//        }
//        if (null == singleEmailTemplateFormatDto.getEmailRequestDtoList()
//                || singleEmailTemplateFormatDto.getEmailRequestDtoList().isEmpty()) {
//            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
//        }
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param multiEmailTemplateFormatDto : com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
//     * @param isManuallyTrigger : boolean
//     */
//    private void validateEmailGroupData(String jobId, MultiEmailTemplateFormatDto multiEmailTemplateFormatDto,
//                                        boolean isManuallyTrigger) {
//        if (isManuallyTrigger && emailSchedulerRepository.existsByJobId(jobId)) {
//            throw new SystemWarningException(String.format(Utility.EX_JOB_ID_ALREADY_TAKEN, jobId));
//        }
//        if (null == multiEmailTemplateFormatDto) {
//            throw new SystemWarningException(Utility.EX_REQUEST_DATA_REQUIRED);
//        }
//        if (null == multiEmailTemplateFormatDto.getEmailRequestDtoList()
//                || multiEmailTemplateFormatDto.getEmailRequestDtoList().isEmpty()) {
//            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
//        }
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param scheduledEmailCount : int
//     * @param isMultipleTemplate : boolean
//     * @param minute : java.lang.Integer
//     * @param hour : java.lang.Integer
//     * @param dayOfMonth : java.lang.Integer
//     * @param month : java.lang.Integer
//     * @param year : java.lang.Integer
//     * @param dayOfWeek : java.lang.Integer
//     * @param cronExpression : java.lang.String
//     */
//    public void saveSchedulerDetails(String jobId, int scheduledEmailCount, boolean isMultipleTemplate, Integer minute,
//                                     Integer hour, Integer dayOfMonth, Integer month, Integer year, Integer dayOfWeek,
//                                     String cronExpression) {
//
//        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
//
//        EmailScheduler emailScheduler = emailSchedulerRepository.findDistinctFirstByJobId(jobId).orElse(new EmailScheduler());
//        emailScheduler.setJobId(jobId);
//        emailScheduler.setMinute(minute);
//        emailScheduler.setHour(hour);
//        emailScheduler.setDayOfMonth(dayOfMonth);
//        emailScheduler.setMonth(month);
//        emailScheduler.setYear(year);
//        emailScheduler.setDayOfWeek(dayOfWeek);
//        emailScheduler.setScheduledStatus(ScheduledStatus.RUNNING);
//        emailScheduler.setScheduledEmailCount(scheduledEmailCount);
//        emailScheduler.setActiveStatus(true);
//        emailScheduler.setMultipleTemplateGroup(isMultipleTemplate);
//        emailScheduler.setCronExp(cronExpression);
//        emailScheduler.setScheduledDatetime(timestamp);
//        emailScheduler.setLastUpdatedDateTime(timestamp);
//        emailSchedulerRepository.save(emailScheduler);
//
//        // Clears Backend Cache
//        evictExistsByJobId(jobId);
//        evictFindFirstByJobId(jobId);
////        evictSaveSchedulerDetails(jobId);
//        evictFindAllByScheduledStatusAndActiveStatus(jobId, emailScheduler.isActiveStatus());
//    }
//
//    @CacheEvict(value = Utility.CACHE_EXISTS_BY_JOB_ID, key = "#jobId")
//    public void evictExistsByJobId(String jobId) {
//        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_EXISTS_BY_JOB_ID)).evict(jobId);
//    }
//
//    @CacheEvict(value = Utility.CACHE_FIND_FIRST_BY_JOB_ID, key = "#jobId")
//    public void evictFindFirstByJobId(String jobId) {
//        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_FIND_FIRST_BY_JOB_ID)).evict(jobId);
//    }
//
//    @CacheEvict(value = Utility.CACHE_FIND_ALL_BY_SCHEDULED_STATUS_AND_ACTIVE_STATUS, key = "#scheduledStatus.concat(#activeStatus)")
//    public void evictFindAllByScheduledStatusAndActiveStatus(String scheduledStatus, boolean activeStatus) {
//        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_FIND_ALL_BY_SCHEDULED_STATUS_AND_ACTIVE_STATUS))
//                .evict(scheduledStatus.concat(String.valueOf(activeStatus)));
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @param tasklet : java.lang.Runnable
//     * @param cronExpression : java.lang.String
//     */
//    private void putScheduledTaskToMap(String jobId, Runnable tasklet, String cronExpression) {
//        addShedLockToSchedule(tasklet, jobId); // Adds a lock for the current schedule to prevent consuming the same schedule again and again by multiple service instances
//        // TimeZone utcTimeZone = Utility.getSystemCurrentUtcTimeZone();
//      //        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, utcTimeZone));
//
//
//
//        TimeZone asiaTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
//        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger("0 50 15 24 2 ?", asiaTimeZone));
//
//        // Add log message before task scheduler is invoked
//        log.info(String.format("Scheduling task '%s' with cron expression '%s' in Asia/Kolkata time zone.", jobId, "0 50 15 24 2 ?"));
//        log.info(String.format(Utility.LOG_SCHEDULED_EMAIL_GROUP, jobId, "0 50 15 24 2 ?"));
//
//        jobsMap.put(jobId, scheduledTask);
//
////        log.info(String.format(Utility.LOG_SCHEDULED_EMAIL_GROUP, jobId, cronExpression));
////        jobsMap.put(jobId, scheduledTask);
//    }
//
//    /**
//     * @param jobId : java.lang.String
//     * @return java.lang.String
//     */
//    public String terminateScheduledEmailGroup(String jobId) {
//        EmailScheduler scheduler = emailSchedulerRepository.findFirstByJobId(jobId);
//        if (null != scheduler) {
//            ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
//            if (null != scheduledTask) {
//                scheduledTask.cancel(true);
//                jobsMap.remove(jobId);
//
//                // Save Data in Database
//                scheduler.setScheduledStatus(ScheduledStatus.CANCELLED);
//                scheduler.setActiveStatus(false);
//                emailSchedulerRepository.save(scheduler);
//
//                Optional<Shedlock> shedlock = shedLockRepository.findById(jobId);
//                // Deletes particular Shed Lock data from shedlock table
//                shedlock.ifPresent(shedLockRepository::delete);
//                return String.format(Utility.MSG_EMAIL_GROUP_SCHEDULE_TERMINATED_SUCCESSFULLY, jobId);
//            }
//        }
//        throw new SystemWarningException(String.format(Utility.EX_JOB_ID_NOT_FOUND, jobId));
//    }
//
//    /**
//     * @param year1 : java.lang.Integer
//     * @param month1 : java.lang.Integer
//     * @param dayOfMonth1 : java.lang.Integer
//     * @param hour1 : java.lang.Integer
//     * @param minute1 : java.lang.Integer
//     * @param dayOfTheWeek1 : java.lang.Integer
//     * @return java.lang.String
//     */
//    private static String generateCronExp(Integer year1, Integer month1, Integer dayOfMonth1, Integer hour1,
//                                          Integer minute1, Integer dayOfTheWeek1) {
//
//        String month = Utility.isNotNullAndAndLessThan(month1, Utility.MAX_CALENDER_MONTH_COUNT, Utility.EX_INVALID_MONTH)
//                ? String.valueOf(month1) : Utility.CRON_EXP_CHAR_ALL; // 1- 12
//        String dayOfMonth = Utility.isNotNullAndAndLessThan(dayOfMonth1, Utility.MAX_CALENDER_DAY_OF_MONTH_COUNT, Utility.EX_INVALID_DAY)
//                ? String.valueOf(dayOfMonth1) : Utility.CRON_EXP_CHAR_ALL; // 1- 31
//
//        String hour = Utility.isNotNullAndInBetween(hour1, Utility.MAX_CALENDER_HOUR_COUNT, Utility.EX_INVALID_HOUR)
//                ? String.valueOf(hour1) : Utility.CRON_EXP_CHAR_ALL; // 0 - 24
//        String minute = Utility.isNotNullAndInBetween(minute1, Utility.MAX_CALENDER_MIN_COUNT, Utility.EX_INVALID_MINUTE)
//                ? String.valueOf(minute1) : Utility.CRON_EXP_CHAR_ALL; // 0 - 59
//        String dayOfTheWeek = Utility.isNotNullAndAndLessThan(dayOfTheWeek1, Utility.MAX_CALENDER_DAY_OF_WEEK_COUNT, Utility.EX_INVALID_WEEK)
//                ? String.valueOf(dayOfTheWeek1) : Utility.CRON_EXP_CHAR_ANY; // 1- 7
//
//        int year = null != year1 ? year1 : LocalDate.now().getYear();
//        if (null != dayOfMonth1 && null != month1) {
//            if (dayOfMonth1 > LocalDate.of(year, month1, 1).lengthOfMonth()) {
//                throw new SystemWarningException(Utility.EX_INVALID_DAY);
//            }
//        }
//        minute = (null != hour1 && 0 == hour1) ? "1" : minute; // Manually force to execute the schedule 1 Minute later for Mid-Night 12 hour schedule
//
//        return String.format(Utility.CRON_EXPRESSION, minute, hour, dayOfMonth, month, dayOfTheWeek);
//    }
//
//    /**
//     * @param tasklet : java.lang.Runnable
//     * @param jobId : java.lang.String
//     */
//    private void addShedLockToSchedule(Runnable tasklet, String jobId) {
//        LockingTaskExecutor executor = new DefaultLockingTaskExecutor(lockProvider);
//        executor.executeWithLock(tasklet, new LockConfiguration(Instant.now(), jobId,
//                Duration.ofDays(getShedLockAtMostForDays()),
//                Duration.ofDays(getShedLockAtLeastForDays())));
//    }
//
//    /**
//     * @return int
//     * Returns ShedLockAtMostForDays by current year
//     */
//    private static int getShedLockAtMostForDays() {
//        LocalDate curDate = LocalDate.now();
//        int curYear = curDate.getYear() % 1000;
//        return 365 * (37 - curYear);
//    }
//
//    /**
//     * @return int
//     * Returns ShedLockAtLeastForDays by current year
//     */
//    private static int getShedLockAtLeastForDays() {
//        LocalDate curDate = LocalDate.now();
//        int curYear = curDate.getYear() % 1000;
//        return 365 * (37 - (curYear + 1));
//    }
//}



package com.notification.service.impl;
import com.notification.dto.request.MultiEmailTemplateFormatDto;
import com.notification.dto.request.SingleEmailTemplateFormatDto;
import com.notification.entity.EmailScheduler;
import com.notification.entity.Shedlock;
import com.notification.enums.ScheduledStatus;
import com.notification.exception.SystemWarningException;
import com.notification.repository.EmailSchedulerRepository;
import com.notification.repository.ShedLockRepository;
import com.notification.service.EmailSchedulerService;
import com.notification.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailSchedulerServiceImpl implements EmailSchedulerService {

    private final TaskScheduler taskScheduler;
    private final EmailSchedulerRepository emailSchedulerRepository;
    private final ShedLockRepository shedLockRepository;
    private final CacheManager cacheManager;
    private final LockProvider lockProvider;

    private static final Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    /**
     * @param jobId : java.lang.String
     * @param tasklet : java.lang.Runnable
     * @param formatDto : com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto
     * @param isManuallyTrigger : boolean
     */
    @Override
    public void scheduleSingleEmailTemplateGroup(String jobId, Runnable tasklet, SingleEmailTemplateFormatDto formatDto,
                                                 boolean isManuallyTrigger) {
        validateEmailGroupData(jobId, formatDto, isManuallyTrigger);
        String cronExpression = generateCronExp(formatDto.getYear(), formatDto.getMonth(), formatDto.getDayOfMonth(),
                formatDto.getHour(), formatDto.getMinute(), formatDto.getDayOfTheWeek());
        saveSchedulerDetails(jobId, formatDto.getEmailRequestDtoList().size(), false,
                formatDto.getMinute(), formatDto.getHour(), formatDto.getDayOfMonth(), formatDto.getMonth(),
                formatDto.getYear(), formatDto.getDayOfTheWeek(), cronExpression);
        putScheduledTaskToMap(jobId, tasklet, cronExpression);
    }

    /**
     * @param jobId : java.lang.String
     * @param tasklet : java.lang.Runnable
     * @param formatDto : com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
     * @param isManuallyTrigger : boolean
     */
    public void scheduleMultiEmailTemplateGroup(String jobId, Runnable tasklet, MultiEmailTemplateFormatDto formatDto,
                                                boolean isManuallyTrigger) {
        validateEmailGroupData(jobId, formatDto, isManuallyTrigger);
        String cronExpression = generateCronExp(formatDto.getYear(), formatDto.getMonth(), formatDto.getDayOfMonth(),
                formatDto.getHour(), formatDto.getMinute(), formatDto.getDayOfTheWeek());
        saveSchedulerDetails(jobId, formatDto.getEmailRequestDtoList().size(), true,
                formatDto.getMinute(), formatDto.getHour(), formatDto.getDayOfMonth(), formatDto.getMonth(),
                formatDto.getYear(), formatDto.getDayOfTheWeek(), cronExpression);
        putScheduledTaskToMap(jobId, tasklet, cronExpression);
    }

    /**
     * @param jobId : java.lang.String
     * @param singleEmailTemplateFormatDto : com.cloudofgoods.notification.dto.request.SingleEmailTemplateFormatDto
     * @param isManuallyTrigger : boolean
     */
    private void validateEmailGroupData(String jobId, SingleEmailTemplateFormatDto singleEmailTemplateFormatDto,
                                        boolean isManuallyTrigger) {
        if (isManuallyTrigger && emailSchedulerRepository.existsByJobId(jobId)) {
            throw new SystemWarningException(String.format(Utility.EX_JOB_ID_ALREADY_TAKEN, jobId));
        }
        if (null == singleEmailTemplateFormatDto) {
            throw new SystemWarningException(Utility.EX_REQUEST_DATA_REQUIRED);
        }
        if (Utility.isNullOrEmpty(singleEmailTemplateFormatDto.getTemplateName())) {
            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
        }
        if (null == singleEmailTemplateFormatDto.getEmailRequestDtoList()
                || singleEmailTemplateFormatDto.getEmailRequestDtoList().isEmpty()) {
            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
        }
    }

    /**
     * @param jobId : java.lang.String
     * @param multiEmailTemplateFormatDto : com.cloudofgoods.notification.dto.request.MultiEmailTemplateFormatDto
     * @param isManuallyTrigger : boolean
     */
    private void validateEmailGroupData(String jobId, MultiEmailTemplateFormatDto multiEmailTemplateFormatDto,
                                        boolean isManuallyTrigger) {
        if (isManuallyTrigger && emailSchedulerRepository.existsByJobId(jobId)) {
            throw new SystemWarningException(String.format(Utility.EX_JOB_ID_ALREADY_TAKEN, jobId));
        }
        if (null == multiEmailTemplateFormatDto) {
            throw new SystemWarningException(Utility.EX_REQUEST_DATA_REQUIRED);
        }
        if (null == multiEmailTemplateFormatDto.getEmailRequestDtoList()
                || multiEmailTemplateFormatDto.getEmailRequestDtoList().isEmpty()) {
            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
        }
    }

    /**
     * @param jobId : java.lang.String
     * @param scheduledEmailCount : int
     * @param isMultipleTemplate : boolean
     * @param minute : java.lang.Integer
     * @param hour : java.lang.Integer
     * @param dayOfMonth : java.lang.Integer
     * @param month : java.lang.Integer
     * @param year : java.lang.Integer
     * @param dayOfWeek : java.lang.Integer
     * @param cronExpression : java.lang.String
     */
    public void saveSchedulerDetails(String jobId, int scheduledEmailCount, boolean isMultipleTemplate, Integer minute,
                                     Integer hour, Integer dayOfMonth, Integer month, Integer year, Integer dayOfWeek,
                                     String cronExpression) {

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        EmailScheduler emailScheduler = emailSchedulerRepository.findDistinctFirstByJobId(jobId).orElse(new EmailScheduler());
        emailScheduler.setJobId(jobId);
        emailScheduler.setMinute(minute);
        emailScheduler.setHour(hour);
        emailScheduler.setDayOfMonth(dayOfMonth);
        emailScheduler.setMonth(month);
        emailScheduler.setYear(year);
        emailScheduler.setDayOfWeek(dayOfWeek);
        emailScheduler.setScheduledStatus(ScheduledStatus.RUNNING);
        emailScheduler.setScheduledEmailCount(scheduledEmailCount);
        emailScheduler.setActiveStatus(true);
        emailScheduler.setMultipleTemplateGroup(isMultipleTemplate);
        emailScheduler.setCronExp(cronExpression);
        emailScheduler.setScheduledDatetime(timestamp);
        emailScheduler.setLastUpdatedDateTime(timestamp);
        emailSchedulerRepository.save(emailScheduler);

        // Clears Backend Cache
        evictExistsByJobId(jobId);
        evictFindFirstByJobId(jobId);
//        evictSaveSchedulerDetails(jobId);
        evictFindAllByScheduledStatusAndActiveStatus(jobId, emailScheduler.isActiveStatus());
    }

    @CacheEvict(value = Utility.CACHE_EXISTS_BY_JOB_ID, key = "#jobId")
    public void evictExistsByJobId(String jobId) {
        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_EXISTS_BY_JOB_ID)).evict(jobId);
    }

    @CacheEvict(value = Utility.CACHE_FIND_FIRST_BY_JOB_ID, key = "#jobId")
    public void evictFindFirstByJobId(String jobId) {
        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_FIND_FIRST_BY_JOB_ID)).evict(jobId);
    }

    @CacheEvict(value = Utility.CACHE_FIND_ALL_BY_SCHEDULED_STATUS_AND_ACTIVE_STATUS, key = "#scheduledStatus.concat(#activeStatus)")
    public void evictFindAllByScheduledStatusAndActiveStatus(String scheduledStatus, boolean activeStatus) {
        Objects.requireNonNull(cacheManager.getCache(Utility.CACHE_FIND_ALL_BY_SCHEDULED_STATUS_AND_ACTIVE_STATUS))
                .evict(scheduledStatus.concat(String.valueOf(activeStatus)));
    }

    /**
     * @param jobId : java.lang.String
     * @param tasklet : java.lang.Runnable
     * @param cronExpression : java.lang.String
     */
    private void putScheduledTaskToMap(String jobId, Runnable tasklet, String cronExpression) {
        addShedLockToSchedule(tasklet, jobId); // Adds a lock for the current schedule to prevent consuming the same schedule again and again by multiple service instances
        TimeZone utcTimeZone = Utility.getSystemCurrentUtcTimeZone();
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, utcTimeZone));
        log.info(String.format(Utility.LOG_SCHEDULED_EMAIL_GROUP, jobId, cronExpression));
        jobsMap.put(jobId, scheduledTask);
    }

    /**
     * @param jobId : java.lang.String
     * @return java.lang.String
     */
    public String terminateScheduledEmailGroup(String jobId) {
        EmailScheduler scheduler = emailSchedulerRepository.findFirstByJobId(jobId);
        if (null != scheduler) {
            ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
            if (null != scheduledTask) {
                scheduledTask.cancel(true);
                jobsMap.remove(jobId);

                // Save Data in Database
                scheduler.setScheduledStatus(ScheduledStatus.CANCELLED);
                scheduler.setActiveStatus(false);
                emailSchedulerRepository.save(scheduler);

                Optional<Shedlock> shedlock = shedLockRepository.findById(jobId);
                // Deletes particular Shed Lock data from shedlock table
                shedlock.ifPresent(shedLockRepository::delete);
                return String.format(Utility.MSG_EMAIL_GROUP_SCHEDULE_TERMINATED_SUCCESSFULLY, jobId);
            }
        }
        throw new SystemWarningException(String.format(Utility.EX_JOB_ID_NOT_FOUND, jobId));
    }

    /**
     * @param year1 : java.lang.Integer
     * @param month1 : java.lang.Integer
     * @param dayOfMonth1 : java.lang.Integer
     * @param hour1 : java.lang.Integer
     * @param minute1 : java.lang.Integer
     * @param dayOfTheWeek1 : java.lang.Integer
     * @return java.lang.String
     */
    private static String generateCronExp(Integer year1, Integer month1, Integer dayOfMonth1, Integer hour1,
                                          Integer minute1, Integer dayOfTheWeek1) {

        String month = Utility.isNotNullAndAndLessThan(month1, Utility.MAX_CALENDER_MONTH_COUNT, Utility.EX_INVALID_MONTH)
                ? String.valueOf(month1) : Utility.CRON_EXP_CHAR_ALL; // 1- 12
        String dayOfMonth = Utility.isNotNullAndAndLessThan(dayOfMonth1, Utility.MAX_CALENDER_DAY_OF_MONTH_COUNT, Utility.EX_INVALID_DAY)
                ? String.valueOf(dayOfMonth1) : Utility.CRON_EXP_CHAR_ALL; // 1- 31

        String hour = Utility.isNotNullAndInBetween(hour1, Utility.MAX_CALENDER_HOUR_COUNT, Utility.EX_INVALID_HOUR)
                ? String.valueOf(hour1) : Utility.CRON_EXP_CHAR_ALL; // 0 - 24
        String minute = Utility.isNotNullAndInBetween(minute1, Utility.MAX_CALENDER_MIN_COUNT, Utility.EX_INVALID_MINUTE)
                ? String.valueOf(minute1) : Utility.CRON_EXP_CHAR_ALL; // 0 - 59
        String dayOfTheWeek = Utility.isNotNullAndAndLessThan(dayOfTheWeek1, Utility.MAX_CALENDER_DAY_OF_WEEK_COUNT, Utility.EX_INVALID_WEEK)
                ? String.valueOf(dayOfTheWeek1) : Utility.CRON_EXP_CHAR_ANY; // 1- 7

        int year = null != year1 ? year1 : LocalDate.now().getYear();
        if (null != dayOfMonth1 && null != month1) {
            if (dayOfMonth1 > LocalDate.of(year, month1, 1).lengthOfMonth()) {
                throw new SystemWarningException(Utility.EX_INVALID_DAY);
            }
        }
        minute = (null != hour1 && 0 == hour1) ? "1" : minute; // Manually force to execute the schedule 1 Minute later for Mid-Night 12 hour schedule

        return String.format(Utility.CRON_EXPRESSION, minute, hour, dayOfMonth, month, dayOfTheWeek);
    }

    /**
     * @param tasklet : java.lang.Runnable
     * @param jobId : java.lang.String
     */
    private void addShedLockToSchedule(Runnable tasklet, String jobId) {
        LockingTaskExecutor executor = new DefaultLockingTaskExecutor(lockProvider);
        executor.executeWithLock(tasklet, new LockConfiguration(Instant.now(), jobId,
                Duration.ofDays(getShedLockAtMostForDays()),
                Duration.ofDays(getShedLockAtLeastForDays())));
    }

    /**
     * @return int
     * Returns ShedLockAtMostForDays by current year
     */
    private static int getShedLockAtMostForDays() {
        LocalDate curDate = LocalDate.now();
        int curYear = curDate.getYear() % 1000;
        return 365 * (37 - curYear);
    }

    /**
     * @return int
     * Returns ShedLockAtLeastForDays by current year
     */
    private static int getShedLockAtLeastForDays() {
        LocalDate curDate = LocalDate.now();
        int curYear = curDate.getYear() % 1000;
        return 365 * (37 - (curYear + 1));
    }
}