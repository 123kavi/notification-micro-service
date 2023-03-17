package com.notification.service.impl;

import com.notification.dto.SmsContentDto;
import com.notification.entity.MailContent;
import com.notification.exception.SystemWarningException;
import com.notification.repository.MailContentRepository;
import com.notification.service.CogSmsService;
import com.notification.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public abstract class CogSmsServiceImpl implements CogSmsService {

    private final MailContentRepository mailContentRepository;
    private final CacheManager cacheManager;

    /**
     * @param templateName : java.lang.String
     * @return com.cloudofgoods.notification.dto.SmsContentDto
     * Returns relevant message content details by template name
     */
    @Override
    public SmsContentDto getMessageContent(String templateName) {
        if (Utility.isNullOrEmpty(templateName))
            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
        MailContent content = mailContentRepository.findFirstByTemplateName(templateName);
        if (null != content) {
            SmsContentDto smsContentDto = new SmsContentDto();
            smsContentDto.setTemplateName(content.getTemplateName());
            smsContentDto.setMessageTitle(content.getMsgTitle());
            smsContentDto.setMessageBody(content.getMsgBody());
            return smsContentDto;
        }
        throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
    }

    /**
     * @param smsDto : com.cloudofgoods.notification.dto.SmsContentDto
     * @param userName : java.lang.String
     * @return java.lang.String
     * Updates particular message content details by template name
     */
    @Override
    public String updateMessageContent(SmsContentDto smsDto, String userName) {
        MailContent mailContent = validateMessageContent(smsDto, smsDto.getMessageBody());
        mailContent.setMsgTitle(smsDto.getMessageTitle());
        mailContent.setMsgBody(smsDto.getMessageBody());
        mailContent.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        mailContent.setLastUpdatedUser(userName);
        mailContentRepository.save(mailContent);
        log.info(String.format(Utility.LOG_SMS_CONTENT_UPDATED_SUCCESSFULLY, smsDto.getTemplateName()));

        // Clears all the cache data due to data update
        evictFirstByTemplate(Utility.CACHE_VAL_FIRST_BY_TEMPLATE_NAME, smsDto.getTemplateName());
        evictByTemplateNameAndContentLike(Utility.CACHE_VAL_EXISTS_BY_TEMPLATE_NAME_AND_CONTENT_LIKE,
                smsDto.getTemplateName().concat(Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN));

        return Utility.MSG_MESSAGE_CONTENT_UPDATED_SUCCESSFULLY;
    }

    @CacheEvict(value = Utility.CACHE_VAL_FIRST_BY_TEMPLATE_NAME, key = "#templateName")
    public void evictFirstByTemplate(String firstByTemplateName, String templateName) {
        Objects.requireNonNull(cacheManager.getCache(firstByTemplateName)).evict(templateName);
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, firstByTemplateName));
    }

    @CacheEvict(value = Utility.CACHE_VAL_EXISTS_BY_TEMPLATE_NAME_AND_CONTENT_LIKE, key = "#templateName")
    public void evictByTemplateNameAndContentLike(String existsByTemplateNameAndContentLike, String templateName) {
        Objects.requireNonNull(cacheManager.getCache(existsByTemplateNameAndContentLike)).evict(templateName);
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, existsByTemplateNameAndContentLike));
    }

    /**
     * @param smsDto : com.cloudofgoods.notification.dto.SmsContentDto
     * @param messageBody : java.lang.String
     * @return com.cloudofgoods.notification.entity.MailContent
     * @throws SystemWarningException
     * Validates message content details
     */
    private MailContent validateMessageContent(SmsContentDto smsDto, String messageBody) throws SystemWarningException {
        if (null == smsDto) {
            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
        }
        // validates edited message title not to be null or empty
        if (null == smsDto.getMessageTitle() || smsDto.getMessageTitle().isBlank()) {
            throw new SystemWarningException(Utility.EX_INVALID_MESSAGE_TITLE);
        }
        // validates edited message body not to be null or empty
        if (null == messageBody || messageBody.isBlank()) {
            throw new SystemWarningException(Utility.EX_INVALID_MESSAGE_BODY);
        }
        String actualMessageContent = smsDto.getMessageTitle().concat(messageBody);
        List<String> expectedNonEditableList;
        MailContent mailContent = mailContentRepository.findFirstByTemplateName(smsDto.getTemplateName());
        if (null != mailContent) {
            String expectedMessageContent = mailContent.getMsgTitle().concat(mailContent.getMsgBody());
            // Looping through the non-editable tags and retrieve all the occurrences in between <non-editable> and </non-editable> delimiters
            expectedNonEditableList = Utility.extractNonEditableContents(expectedMessageContent);
        } else {
            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
        }

        // Retrieves all the non-editable occurrences in between defined delimiters <non-editable> and </non-editable> from the updated SMS
        List<String> requestedNonEditableList = Utility.extractNonEditableContents(actualMessageContent);
        // Checks for whether the expected(original) non-editable count mismatches with the updated editedEmailContent
        if (expectedNonEditableList.size() != requestedNonEditableList.size()) {
            throw new SystemWarningException(Utility.EX_MISMATCH_NON_EDITABLE_ATTRIBUTE_COUNT);
        }

        List<String> editedNonEditableTags = expectedNonEditableList.stream()
                .filter(data -> !requestedNonEditableList.contains(data)).collect(Collectors.toList());
        if (!editedNonEditableTags.isEmpty()) {
            throw new SystemWarningException(Utility.EX_NON_EDITABLE_ATTRIBUTE_MODIFIED);
        }

        // Checks if user violated non-editable condition including for same multiple tags
        for (String nonEditableData : expectedNonEditableList) {
            List<String> duplicatedNonEditableTags = requestedNonEditableList.stream().filter(
                    data -> nonEditableData.equals(data) || nonEditableData.startsWith(data)
                            || nonEditableData.endsWith(data)).collect(Collectors.toList());
            for (String data : duplicatedNonEditableTags) {
                if (!data.equals(nonEditableData)) {
                    throw new SystemWarningException(Utility.EX_NON_EDITABLE_ATTRIBUTE_MODIFIED);
                }
            }
        }
        return mailContent;
    }
}
