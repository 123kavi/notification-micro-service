//package com.cloudofgoods.notification.service.impl;
//
//import com.cloudofgoods.notification.dto.*;
//import com.cloudofgoods.notification.dto.request.MailRequestDto;
//import com.cloudofgoods.notification.dto.request.SendEmailRequestDto;
//import com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto;
//import com.cloudofgoods.notification.entity.SentEmailLog;
//import com.cloudofgoods.notification.entity.MailContent;
//import com.cloudofgoods.notification.exception.SystemWarningException;
//import com.cloudofgoods.notification.repository.SentEmailLogRepository;
//import com.cloudofgoods.notification.repository.FeatureFlagRepository;
//import com.cloudofgoods.notification.repository.MailContentRepository;
//import com.cloudofgoods.notification.service.CogEmailService;
//import com.cloudofgoods.notification.util.Utility;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.http.MediaType;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import javax.mail.util.ByteArrayDataSource;
//import java.io.*;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class CogEmailServiceImpl implements CogEmailService {
//
//    private final JavaMailSender mailSender;
//    private final MailContentRepository mailContentRepository;
//    private final SentEmailLogRepository sentEmailLogRepository;
//    private final FeatureFlagRepository featureFlagRepository;
//    private final Configuration configuration;
//    private final CacheManager cacheManager;
//
//    @Value("${spring.mail.username}")
//    private String mailFrom;
//    @Value("${app.mail.template.item-detail-section}")
//    private String itemDetailSectionTemplate;
//    @Value("${app.mail.template.top-banner-section}")
//    private String topBannerAdTemplate;
//
//    /**
//     * @param sendEmailDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
//     * @throws javax.mail.MessagingException
//     * @throws freemarker.template.TemplateException
//     * @throws java.io.IOException
//     * Sends HTML Emails (Without File Attachments) using JavaMailSender
//     */
//    @Override
//    public void sendHtmlEmail(SendEmailRequestDto sendEmailDto) throws MessagingException, TemplateException, IOException {
//
//        validateSendEmailDetails(sendEmailDto);
//        if (isExistsByTemplateNameAndActiveStatus(sendEmailDto.getTemplateName())) {
//            MimeMessage message = mailSender.createMimeMessage();
//            MailContent mailContent = mailContentRepository.findFirstByTemplateName(sendEmailDto.getTemplateName());
//
//            MimeMessageHelper helper = new MimeMessageHelper(message, false, Utility.ENCODING_FORMAT_UTF_8);
//            MailRequestDto mail = sendEmailDto.getMail();
//            helper.setTo(mail.getReceiverEmail());
//            helper.setFrom(mailFrom);
//            String html = processMainHtml(mail.getContent(), mail.getOrdersList(),
//                    mail.getTopBannerAdUrl(), mailContent.getTopBannerAdUrl(),
//                    mailContent.getContent(), sendEmailDto.getTemplateName());
//            helper.setText(html, true);
//            helper.setSubject(mailContent.getSubject());
//            mailSender.send(message);
//            log.info(String.format(Utility.LOG_EMAIL_SENT_SUCCESSFULLY, sendEmailDto.getMail().getReceiverEmail(), sendEmailDto.getUserId()));
//            saveHtmlEmailHst(sendEmailDto, html);
//        } else {
//            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
//        }
//    }
//
//    /**
//     * @param fileEmailDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
//     * @throws javax.mail.MessagingException
//     * @throws freemarker.template.TemplateException
//     * @throws java.io.IOException
//     * Sends File Emails (With File Attachments) using JavaMailSender
//     */
//    @Override
//    public void sendFileMail(SendFileEmailRequestDto fileEmailDto) throws MessagingException, TemplateException, IOException {
//
//        validateSendEmailDetails(fileEmailDto);
//        if (isExistsByTemplateNameAndActiveStatus(fileEmailDto.getTemplateName())) {
//            MimeMessage message = mailSender.createMimeMessage();
//            MailContent mailContent = mailContentRepository.findFirstByTemplateName(fileEmailDto.getTemplateName());
//
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, Utility.ENCODING_FORMAT_UTF_8);
//            MailRequestDto mail = fileEmailDto.getMail();
//            helper.setFrom(mailFrom);
//            helper.setTo(mail.getReceiverEmail());
//            String html = processMainHtml(mail.getContent(), mail.getOrdersList(),
//                    mail.getTopBannerAdUrl(), mailContent.getTopBannerAdUrl(), mailContent.getContent(),
//                    fileEmailDto.getTemplateName());
//            helper.setText(html, true);
//            helper.addAttachment(fileEmailDto.getFilename(), new ByteArrayDataSource(
//                    Utility.getDecodedByteArray(fileEmailDto.getBase64file()), MediaType.APPLICATION_PDF_VALUE));
//            helper.setSubject(mailContent.getSubject());
//            mailSender.send(message);
//            log.info(String.format(Utility.LOG_EMAIL_SENT_SUCCESSFULLY, fileEmailDto.getMail().getReceiverEmail(), fileEmailDto.getUserId()));
//            saveFileEmailHst(fileEmailDto, html);
//        } else {
//            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
//        }
//    }
//
//    /**
//     * @param emailDto : com.cloudofgoods.notification.dto.EmailDto
//     * @param userName : java.lang.String
//     * @return java.lang.String
//     * @throws com.cloudofgoods.notification.exception.SystemWarningException
//     * Updates particular email content details by template name
//     */
//    @Override
//    public String updateEmailContent(EmailDto emailDto, String userName) throws SystemWarningException {
//        MailContent mailContent = validateEmailContent(emailDto.getTemplateName(), emailDto.getEditedEmailContent(), emailDto.getEditedEmailSubject());
//        mailContent.setSubject(emailDto.getEditedEmailSubject());
//        mailContent.setContent(emailDto.getEditedEmailContent());
//        mailContent.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
//        mailContent.setLastUpdatedUser(userName);
//        mailContentRepository.save(mailContent);
//        log.info(String.format(Utility.LOG_EMAIL_CONTENT_UPDATED_SUCCESSFULLY, emailDto.getTemplateName()));
//
//        // Clears all the cache data due to data update
//        evictFirstByTemplate(Utility.CACHE_VAL_FIRST_BY_TEMPLATE_NAME, emailDto.getTemplateName());
//
//        evictSetHtmlTopBannerAdSection(Utility.CACHE_SET_HTML_TOP_BANNER_SECTION,
//                emailDto.getTemplateName().concat(Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN));
//        evictSetItemDetailsSection(Utility.CACHE_SET_ITEM_DETAILS_SECTION,
//                emailDto.getTemplateName().concat(Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN));
//
//        return Utility.MSG_EMAIL_CONTENT_UPDATED_SUCCESSFULLY;
//    }
//
//    /**
//     * @param templateName : java.lang.String
//     * @return com.cloudofgoods.notification.dto.ViewEmailContentDto
//     * Returns particular email content details by template name
//     */
//    @Override
//    public ViewEmailContentDto getEmailContent(String templateName) {
//        if (Utility.isNullOrEmpty(templateName))
//            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
//        MailContent content = mailContentRepository.findFirstByTemplateName(templateName);
//        if (null != content) {
//            ViewEmailContentDto emailContentDto = new ViewEmailContentDto();
//            emailContentDto.setTemplateName(content.getTemplateName());
//            emailContentDto.setSubject(content.getSubject());
//            emailContentDto.setContent(content.getContent());
//            return emailContentDto;
//        }
//        throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
//    }
//
//    @CacheEvict(value = Utility.CACHE_VAL_FIRST_BY_TEMPLATE_NAME, key = "#templateName")
//    public void evictFirstByTemplate(String firstByTemplateName, String templateName) {
//        Objects.requireNonNull(cacheManager.getCache(firstByTemplateName)).evict(templateName);
//        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, firstByTemplateName));
//    }
//
//    @CacheEvict(value = Utility.CACHE_SET_HTML_TOP_BANNER_SECTION, key = "#templateName")
//    public void evictSetHtmlTopBannerAdSection(String setHtmlTopBannerAdSection, String templateName) {
//        Objects.requireNonNull(cacheManager.getCache(setHtmlTopBannerAdSection)).evict(templateName);
//        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, setHtmlTopBannerAdSection));
//    }
//
//    @CacheEvict(value = Utility.CACHE_SET_ITEM_DETAILS_SECTION, key = "#templateName")
//    public void evictSetItemDetailsSection(String setItemDetailsSection, String templateName) {
//        Objects.requireNonNull(cacheManager.getCache(setItemDetailsSection)).evict(templateName);
//        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, setItemDetailsSection));
//    }
//
//    /**
//     * @param templateName : java.lang.String
//     * @param emailContent : java.lang.String
//     * @param emailSubject : java.lang.String
//     * @return com.cloudofgoods.notification.entity.MailContent
//     * @throws com.cloudofgoods.notification.exception.SystemWarningException
//     * Validates requested email content details before get updated
//     */
//    private MailContent validateEmailContent(String templateName, String emailContent, String emailSubject) throws SystemWarningException {
//        MailContent mailContent = mailContentRepository.findFirstByTemplateName(templateName);
//        List<String> expectedNonEditableList;
//        if (null != mailContent) {
//            // Looping through the non-editable tags and retrieve all the occurrences in between <non-editable> and </non-editable> delimiters
//            expectedNonEditableList = Utility.extractNonEditableContents(mailContent.getContent());
//        } else {
//            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
//        }
//
//        // Retrieves all the non-editable occurrences in between defined delimiters <non-editable> and </non-editable> from the updated editedEmailContent
//        List<String> requestedNonEditableList = Utility.extractNonEditableContents(emailContent);
//        // Checks for whether the expected(original) non-editable count mismatches with the updated editedEmailContent
//        if (expectedNonEditableList.size() != requestedNonEditableList.size()) {
//            throw new SystemWarningException(Utility.EX_MISMATCH_NON_EDITABLE_ATTRIBUTE_COUNT);
//        }
//
//        List<String> editedNonEditableTags = expectedNonEditableList.stream()
//                .filter(data -> !requestedNonEditableList.contains(data)).collect(Collectors.toList());
//        if (!editedNonEditableTags.isEmpty()) {
//            throw new SystemWarningException(Utility.EX_NON_EDITABLE_ATTRIBUTE_MODIFIED);
//        }
//
//        // Checks if user violated non-editable condition including for same multiple tags
//        for (String nonEditableData : expectedNonEditableList) {
//            List<String> duplicatedNonEditableTags = requestedNonEditableList.stream().filter(
//                    data -> nonEditableData.equals(data) || nonEditableData.startsWith(data)
//                            || nonEditableData.endsWith(data)).collect(Collectors.toList());
//            for (String data : duplicatedNonEditableTags) {
//                if (!data.equals(nonEditableData)) {
//                    throw new SystemWarningException(Utility.EX_NON_EDITABLE_ATTRIBUTE_MODIFIED);
//                }
//            }
//        }
//
//        // validates email editedEmailSubject not to be null or empty
//        if (null == emailSubject || emailSubject.isBlank()) {
//            throw new SystemWarningException(Utility.EX_INVALID_EMAIL_SUBJECT);
//        }
//        return mailContent;
//    }
//
//    /**
//     * @param content : java.util.Map<java.lang.String, java.lang.Object>
//     * @param html : java.lang.String
//     * @return java.lang.String
//     * Replaces email content non editable parameters with database values
//     */
//    private String replaceMailContentParams(Map<String, Object> content, String html) {
//        if (null != html) {
//            // Replace the non-editable tags Identifier with an empty String
//            html = html.replaceAll(Utility.NON_EDITABLE_IDENTIFIER_START, Utility.EMPTY_STR).
//                    replaceAll(Utility.NON_EDITABLE_IDENTIFIER_END, Utility.EMPTY_STR);
//            // Loop through editedEmailContent map and replaces appropriate field values against the relevant key
//            for (Map.Entry<String, Object> param : content.entrySet()) {
//                html = html.replace(param.getKey(), String.valueOf(param.getValue()));
//            }
//        }
//        return html;
//    }
//
//    /**
//     * @param content : java.util.Map<java.lang.String, java.lang.Object>
//     * @param orderItemsList : java.util.List<om.cloudofgoods.notification.dto.ItemDetailsDto>
//     * @param requestedTopBannerAdUrl : java.lang.String
//     * @param currentTopBannerAdUrl : java.lang.String
//     * @param mailContent : java.lang.String
//     * @param templateName : java.lang.String
//     * @return java.lang.String
//     * @throws java.io.IOException
//     * @throws freemarker.template.TemplateException
//     * Process and fills out the relevant sections of HTML email template
//     */
//    private String processMainHtml(Map<String, Object> content, List<ItemDetailsDto> orderItemsList,
//                                   String requestedTopBannerAdUrl, String currentTopBannerAdUrl,
//                                   String mailContent, String templateName) throws IOException, TemplateException {
//
//        validateObjectContent(content, templateName);
//        String mainHtml = replaceMailContentParams(content, mailContent);
//
//        mainHtml = setHtmlTopBannerAdSection(requestedTopBannerAdUrl, currentTopBannerAdUrl,
//                templateName, Utility.TOP_BANNER_AD_SECTION_CONTENT_PATTERN, mainHtml);
//
//        mainHtml = setItemDetailsSection(orderItemsList, templateName,
//                Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN, mainHtml);
//        return mainHtml;
//    }
//
//    @Cacheable(key = "#templateName.concat(#contentPattern)", value = Utility.CACHE_SET_ITEM_DETAILS_SECTION)
//    public String setItemDetailsSection(List<ItemDetailsDto> orderItemsList, String templateName,
//                                        String contentPattern, String mainHtml) throws IOException, TemplateException {
//        // Checks whether email contains an item details section
//        if (mailContentRepository.existsByTemplateNameAndContentLike(templateName, contentPattern)) {
//            Template itemDetailsTemplate = configuration.getTemplate(itemDetailSectionTemplate);
//            Map<String, Object> productMap = new HashMap<>();
//            productMap.put(Utility.ORDERS_LIST_MAP_KEY, orderItemsList);
//            String itemsListHtml = FreeMarkerTemplateUtils.processTemplateIntoString(itemDetailsTemplate, productMap);
//            mainHtml = mainHtml.replace(Utility.ITEM_DETAILS_SECTION_IDENTIFIER, itemsListHtml);
//        }
//        return mainHtml;
//    }
//
//    @Cacheable(key = "#templateName.concat(#contentPattern)", value = Utility.CACHE_SET_HTML_TOP_BANNER_SECTION)
//    public String setHtmlTopBannerAdSection(String requestedTopBannerAdUrl, String currentTopBannerAdUrl,
//                                            String templateName, String contentPattern,
//                                            String mainHtml) throws IOException, TemplateException {
//        // Checks whether email contains a header banner ads section
//        if (mailContentRepository.existsByTemplateNameAndContentLike(templateName, contentPattern)) {
//            Template topBannerTemplate = configuration.getTemplate(topBannerAdTemplate);
//            Map<String, Object> productMap = new HashMap<>();
//            String adUrl = Utility.isNotNullAndNotEmpty(requestedTopBannerAdUrl) ? requestedTopBannerAdUrl : currentTopBannerAdUrl;
//            productMap.put(Utility.IMAGE_URL_MAP_KEY, adUrl);
//            productMap.put(Utility.IMAGE_URL_IS_NOTNULL_AND_NOT_EMPTY_MAP_KEY, Utility.isNotNullAndNotEmpty(adUrl));
//            String topBannerHtml = FreeMarkerTemplateUtils.processTemplateIntoString(topBannerTemplate, productMap);
//            mainHtml = mainHtml.replace(Utility.TOP_BANNER_AD_SECTION_IDENTIFIER, topBannerHtml);
//        }
//        return mainHtml;
//    }
//
//    /**
//     * @param content : java.util.Map<java.lang.String, java.lang.Object>
//     * @param templateName : java.lang.String
//     * Validates whether email content parameters count matches and other validations satisfies
//     * for the relevant email template
//     */
//    private void validateObjectContent(Map<String, Object> content, String templateName) {
//        List<String> keyMapList = new ArrayList<>(content.keySet());
//        MailContent firstByTemplateName = mailContentRepository.findFirstByTemplateName(templateName);
//
//        String[] expectedNonEditableList;
//        if (null != firstByTemplateName) {
//            // Looping through the non-editable tags and retrieve all the occurrences in between <non-editable> and </non-editable> delimiters
//            expectedNonEditableList = StringUtils.substringsBetween(
//                    firstByTemplateName.getContent(), Utility.NON_EDITABLE_IDENTIFIER_START, Utility.NON_EDITABLE_IDENTIFIER_END);
//        } else {
//            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
//        }
//
//        for (String nonEditableData : expectedNonEditableList) {
//            if (!Utility.ITEM_DETAILS_SECTION_IDENTIFIER.equals(nonEditableData) && !keyMapList.contains(nonEditableData)) {
//                throw new SystemWarningException(Utility.EX_INVALID_NO_OF_CONTENT_DATA_MAP_PROVIDED);
//            }
//        }
//    }
//
//    /**
//     * @param emailRequestDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
//     * @throws com.cloudofgoods.notification.exception.SystemWarningException
//     * Validates Html email send details
//     */
//    private void validateSendEmailDetails(SendEmailRequestDto emailRequestDto) throws SystemWarningException {
//        if (null == emailRequestDto) {
//            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
//        }
//        validateCommonEmailDetails(emailRequestDto.getTemplateName(), emailRequestDto.getMail());
//    }
//
//    /**
//     * @param fileEmailRequestDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
//     * @throws com.cloudofgoods.notification.exception.SystemWarningException
//     * Validates Html file mail send details
//     */
//    private void validateSendEmailDetails(SendFileEmailRequestDto fileEmailRequestDto) throws SystemWarningException {
//        if (null == fileEmailRequestDto) {
//            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
//        }
//        if (Utility.isNullOrEmpty(fileEmailRequestDto.getFilename())) {
//            throw new SystemWarningException(Utility.EX_FILE_NAME_REQUIRED);
//        }
//        if (!fileEmailRequestDto.getFilename().matches(Utility.PDF_FILE_FORMAT_REGEX)) {
//            throw new SystemWarningException(Utility.EX_INVALID_FILE_EXTENSION);
//        }
//        if (null == fileEmailRequestDto.getBase64file() || fileEmailRequestDto.getBase64file().isBlank()) {
//            throw new SystemWarningException(Utility.EX_FILE_DATA_REQUIRED);
//        }
//        validateCommonEmailDetails(fileEmailRequestDto.getTemplateName(), fileEmailRequestDto.getMail());
//    }
//
//    /**
//     * @param templateName : java.lang.String
//     * @param mail : com.cloudofgoods.notification.dto.request.MailRequestDto
//     * Validates email send details common for both html mail and file mail
//     */
//    private void validateCommonEmailDetails(String templateName, MailContentDto mail) {
//        if (Utility.isNullOrEmpty(templateName)) {
//            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
//        }
//        if (!featureFlagRepository.existsByName(templateName)) {
//            throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
//        }
//        if (null == mail) {
//            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
//        }
//    }
//
//    /**
//     * @param sendEmailDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
//     * @param html : java.lang.String
//     * Saves sent html email data as log for future reference
//     */
//    private void saveHtmlEmailHst(SendEmailRequestDto sendEmailDto, String html) {
//        saveEmailHst(html, sendEmailDto.getUserId());
//    }
//
//    /**
//     * @param sendEmailDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
//     * @param html : java.lang.String
//     * Saves sent file email data as log for future reference
//     */
//    private void saveFileEmailHst(SendFileEmailRequestDto sendEmailDto, String html) {
//        saveEmailHst(html, sendEmailDto.getUserId());
//    }
//
//    /**
//     * @param html : java.lang.String
//     * @param userId : long
//     * Saves sent email hst data
//     */
//    private void saveEmailHst(String html, long userId) {
//        SentEmailLog sentEmailLog = new SentEmailLog();
//        sentEmailLog.setUserId(userId);
//        sentEmailLog.setEmailContent(html);
//        sentEmailLog.setSentDateTime(Timestamp.valueOf(LocalDateTime.now()));
//        sentEmailLogRepository.save(sentEmailLog);
//        log.info(String.format(Utility.LOG_SENT_EMAIL_DETAILS_SAVED_SUCCESSFULLY, userId));
//    }
//
//    /**
//     * @param templateName : java.lang.String
//     * @return boolean
//     * Returns boolean value based on existence of a feature flag by its name and activeStatus
//     */
//    private boolean isExistsByTemplateNameAndActiveStatus(String templateName) {
//        return featureFlagRepository.existsByNameAndStatus(templateName, Utility.ACTIVE_STATUS_TRUE);
//    }
//}
package com.notification.service.impl;

import com.cloudofgoods.notification.dto.*;
import com.notification.dto.request.MailRequestDto;
import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import com.notification.entity.SentEmailLog;
import com.notification.entity.MailContent;
import com.notification.exception.SystemWarningException;
import com.notification.repository.SentEmailLogRepository;
import com.notification.repository.FeatureFlagRepository;
import com.notification.repository.MailContentRepository;
import com.notification.service.CogEmailService;
import com.notification.util.Utility;
import com.notification.dto.EmailDto;
import com.notification.dto.ItemDetailsDto;
import com.notification.dto.ViewEmailContentDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CogEmailServiceImpl implements CogEmailService {

    private final JavaMailSender mailSender;
    private final MailContentRepository mailContentRepository;
    private final SentEmailLogRepository sentEmailLogRepository;
    private final FeatureFlagRepository featureFlagRepository;
    private final Configuration configuration;
    private final CacheManager cacheManager;

    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${app.mail.template.item-detail-section}")
    private String itemDetailSectionTemplate;
    @Value("${app.mail.template.top-banner-section}")
    private String topBannerAdTemplate;

    /**
     * @param sendEmailDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * @throws javax.mail.MessagingException
     * @throws freemarker.template.TemplateException
     * @throws java.io.IOException
     * Sends HTML Emails (Without File Attachments) using JavaMailSender
     */
    @Override
    public void sendHtmlEmail(SendEmailRequestDto sendEmailDto) throws MessagingException, TemplateException, IOException {

        validateSendEmailDetails(sendEmailDto);
        if (isExistsByTemplateNameAndActiveStatus(sendEmailDto.getTemplateName())) {
            MimeMessage message = mailSender.createMimeMessage();
            MailContent mailContent = mailContentRepository.findFirstByTemplateName(sendEmailDto.getTemplateName());

            MimeMessageHelper helper = new MimeMessageHelper(message, false, Utility.ENCODING_FORMAT_UTF_8);
            MailRequestDto mail = sendEmailDto.getMail();
            helper.setTo(mail.getReceiverEmail());
            helper.setFrom(mailFrom);
            String html = processMainHtml(mail.getContent(), mail.getOrdersList(),
                    mail.getTopBannerAdUrl(), mailContent.getTopBannerAdUrl(),
                    mailContent.getContent(), sendEmailDto.getTemplateName());
            helper.setText(html, true);
            helper.setSubject(mailContent.getSubject());
            mailSender.send(message);
            log.info(String.format(Utility.LOG_EMAIL_SENT_SUCCESSFULLY, sendEmailDto.getMail().getReceiverEmail(), sendEmailDto.getUserId()));
            saveHtmlEmailHst(sendEmailDto, html);
        } else {
            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
        }
    }

    /**
     * @param fileEmailDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * @throws javax.mail.MessagingException
     * @throws freemarker.template.TemplateException
     * @throws java.io.IOException
     * Sends File Emails (With File Attachments) using JavaMailSender
     */
    @Override
    public void sendFileMail(SendFileEmailRequestDto fileEmailDto) throws MessagingException, TemplateException, IOException {

        validateSendEmailDetails(fileEmailDto);
        if (isExistsByTemplateNameAndActiveStatus(fileEmailDto.getTemplateName())) {
            MimeMessage message = mailSender.createMimeMessage();
            MailContent mailContent = mailContentRepository.findFirstByTemplateName(fileEmailDto.getTemplateName());

            MimeMessageHelper helper = new MimeMessageHelper(message, true, Utility.ENCODING_FORMAT_UTF_8);
            MailRequestDto mail = fileEmailDto.getMail();
            helper.setFrom(mailFrom);
            helper.setTo(mail.getReceiverEmail());
            String html = processMainHtml(mail.getContent(), mail.getOrdersList(),
                    mail.getTopBannerAdUrl(), mailContent.getTopBannerAdUrl(), mailContent.getContent(),
                    fileEmailDto.getTemplateName());
            helper.setText(html, true);
            helper.addAttachment(fileEmailDto.getFilename(), new ByteArrayDataSource(
                    Utility.getDecodedByteArray(fileEmailDto.getBase64file()), MediaType.APPLICATION_PDF_VALUE));
            helper.setSubject(mailContent.getSubject());
            mailSender.send(message);
            log.info(String.format(Utility.LOG_EMAIL_SENT_SUCCESSFULLY, fileEmailDto.getMail().getReceiverEmail(), fileEmailDto.getUserId()));
            saveFileEmailHst(fileEmailDto, html);
        } else {
            throw new SystemWarningException(Utility.EX_FEATURE_FLAG_STATUS_DISABLED);
        }
    }

    /**
     * @param emailDto : com.cloudofgoods.notification.dto.EmailDto
     * @param userName : java.lang.String
     * @return java.lang.String
     * @throws SystemWarningException
     * Updates particular email content details by template name
     */
    @Override
    public String updateEmailContent(EmailDto emailDto, String userName) throws SystemWarningException {
        MailContent mailContent = validateEmailContent(emailDto.getTemplateName(), emailDto.getEditedEmailContent(), emailDto.getEditedEmailSubject());
        mailContent.setSubject(emailDto.getEditedEmailSubject());
        mailContent.setContent(emailDto.getEditedEmailContent());
        mailContent.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        mailContent.setLastUpdatedUser(userName);
        mailContentRepository.save(mailContent);
        log.info(String.format(Utility.LOG_EMAIL_CONTENT_UPDATED_SUCCESSFULLY, emailDto.getTemplateName()));

        // Clears all the cache data due to data update
        evictFirstByTemplate(Utility.CACHE_VAL_FIRST_BY_TEMPLATE_NAME, emailDto.getTemplateName());

        evictSetHtmlTopBannerAdSection(Utility.CACHE_SET_HTML_TOP_BANNER_SECTION,
                emailDto.getTemplateName().concat(Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN));
        evictSetItemDetailsSection(Utility.CACHE_SET_ITEM_DETAILS_SECTION,
                emailDto.getTemplateName().concat(Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN));

        return Utility.MSG_EMAIL_CONTENT_UPDATED_SUCCESSFULLY;
    }

    /**
     * @param templateName : java.lang.String
     * @return com.cloudofgoods.notification.dto.ViewEmailContentDto
     * Returns particular email content details by template name
     */
    @Override
    public ViewEmailContentDto getEmailContent(String templateName) {
        if (Utility.isNullOrEmpty(templateName))
            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
        MailContent content = mailContentRepository.findFirstByTemplateName(templateName);
        if (null != content) {
            ViewEmailContentDto emailContentDto = new ViewEmailContentDto();
            emailContentDto.setTemplateName(content.getTemplateName());
            emailContentDto.setSubject(content.getSubject());
            emailContentDto.setContent(content.getContent());
            return emailContentDto;
        }
        throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
    }

    @CacheEvict(value = Utility.CACHE_VAL_FIRST_BY_TEMPLATE_NAME, key = "#templateName")
    public void evictFirstByTemplate(String firstByTemplateName, String templateName) {
        Objects.requireNonNull(cacheManager.getCache(firstByTemplateName)).evict(templateName);
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, firstByTemplateName));
    }

    @CacheEvict(value = Utility.CACHE_SET_HTML_TOP_BANNER_SECTION, key = "#templateName")
    public void evictSetHtmlTopBannerAdSection(String setHtmlTopBannerAdSection, String templateName) {
        Objects.requireNonNull(cacheManager.getCache(setHtmlTopBannerAdSection)).evict(templateName);
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, setHtmlTopBannerAdSection));
    }

    @CacheEvict(value = Utility.CACHE_SET_ITEM_DETAILS_SECTION, key = "#templateName")
    public void evictSetItemDetailsSection(String setItemDetailsSection, String templateName) {
        Objects.requireNonNull(cacheManager.getCache(setItemDetailsSection)).evict(templateName);
        log.info(String.format(Utility.LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME, templateName, templateName, setItemDetailsSection));
    }

    /**
     * @param templateName : java.lang.String
     * @param emailContent : java.lang.String
     * @param emailSubject : java.lang.String
     * @return com.cloudofgoods.notification.entity.MailContent
     * @throws SystemWarningException
     * Validates requested email content details before get updated
     */
    private MailContent validateEmailContent(String templateName, String emailContent, String emailSubject) throws SystemWarningException {
        MailContent mailContent = mailContentRepository.findFirstByTemplateName(templateName);
        List<String> expectedNonEditableList;
        if (null != mailContent) {
            // Looping through the non-editable tags and retrieve all the occurrences in between <non-editable> and </non-editable> delimiters
            expectedNonEditableList = Utility.extractNonEditableContents(mailContent.getContent());
        } else {
            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
        }

        // Retrieves all the non-editable occurrences in between defined delimiters <non-editable> and </non-editable> from the updated editedEmailContent
        List<String> requestedNonEditableList = Utility.extractNonEditableContents(emailContent);
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

        // validates email editedEmailSubject not to be null or empty
        if (null == emailSubject || emailSubject.isBlank()) {
            throw new SystemWarningException(Utility.EX_INVALID_EMAIL_SUBJECT);
        }
        return mailContent;
    }

    /**
     * @param content : java.util.Map<java.lang.String, java.lang.Object>
     * @param html : java.lang.String
     * @return java.lang.String
     * Replaces email content non editable parameters with database values
     */
    private String replaceMailContentParams(Map<String, Object> content, String html) {
        if (null != html) {
            // Replace the non-editable tags Identifier with an empty String
            html = html.replaceAll(Utility.NON_EDITABLE_IDENTIFIER_START, Utility.EMPTY_STR).
                    replaceAll(Utility.NON_EDITABLE_IDENTIFIER_END, Utility.EMPTY_STR);
            // Loop through editedEmailContent map and replaces appropriate field values against the relevant key
            for (Map.Entry<String, Object> param : content.entrySet()) {
                html = html.replace(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        return html;
    }

    /**
     * @param content : java.util.Map<java.lang.String, java.lang.Object>
     * @param orderItemsList : java.util.List<om.cloudofgoods.notification.dto.ItemDetailsDto>
     * @param requestedTopBannerAdUrl : java.lang.String
     * @param currentTopBannerAdUrl : java.lang.String
     * @param mailContent : java.lang.String
     * @param templateName : java.lang.String
     * @return java.lang.String
     * @throws java.io.IOException
     * @throws freemarker.template.TemplateException
     * Process and fills out the relevant sections of HTML email template
     */
    private String processMainHtml(Map<String, Object> content, List<ItemDetailsDto> orderItemsList,
                                   String requestedTopBannerAdUrl, String currentTopBannerAdUrl,
                                   String mailContent, String templateName) throws IOException, TemplateException {

        validateObjectContent(content, templateName);
        String mainHtml = replaceMailContentParams(content, mailContent);

        mainHtml = setHtmlTopBannerAdSection(requestedTopBannerAdUrl, currentTopBannerAdUrl,
                templateName, Utility.TOP_BANNER_AD_SECTION_CONTENT_PATTERN, mainHtml);

        mainHtml = setItemDetailsSection(orderItemsList, templateName,
                Utility.ITEM_DETAILS_SECTION_CONTENT_PATTERN, mainHtml);
        return mainHtml;
    }

    @Cacheable(key = "#templateName.concat(#contentPattern)", value = Utility.CACHE_SET_ITEM_DETAILS_SECTION)
    public String setItemDetailsSection(List<ItemDetailsDto> orderItemsList, String templateName,
                                        String contentPattern, String mainHtml) throws IOException, TemplateException {
        // Checks whether email contains an item details section
        if (mailContentRepository.existsByTemplateNameAndContentLike(templateName, contentPattern)) {
            Template itemDetailsTemplate = configuration.getTemplate(itemDetailSectionTemplate);
            Map<String, Object> productMap = new HashMap<>();
            productMap.put(Utility.ORDERS_LIST_MAP_KEY, orderItemsList);
            String itemsListHtml = FreeMarkerTemplateUtils.processTemplateIntoString(itemDetailsTemplate, productMap);
            mainHtml = mainHtml.replace(Utility.ITEM_DETAILS_SECTION_IDENTIFIER, itemsListHtml);
        }
        return mainHtml;
    }

    @Cacheable(key = "#templateName.concat(#contentPattern)", value = Utility.CACHE_SET_HTML_TOP_BANNER_SECTION)
    public String setHtmlTopBannerAdSection(String requestedTopBannerAdUrl, String currentTopBannerAdUrl,
                                            String templateName, String contentPattern,
                                            String mainHtml) throws IOException, TemplateException {
        // Checks whether email contains a header banner ads section
        if (mailContentRepository.existsByTemplateNameAndContentLike(templateName, contentPattern)) {
            Template topBannerTemplate = configuration.getTemplate(topBannerAdTemplate);
            Map<String, Object> productMap = new HashMap<>();
            String adUrl = Utility.isNotNullAndNotEmpty(requestedTopBannerAdUrl) ? requestedTopBannerAdUrl : currentTopBannerAdUrl;
            productMap.put(Utility.IMAGE_URL_MAP_KEY, adUrl);
            productMap.put(Utility.IMAGE_URL_IS_NOTNULL_AND_NOT_EMPTY_MAP_KEY, Utility.isNotNullAndNotEmpty(adUrl));
            String topBannerHtml = FreeMarkerTemplateUtils.processTemplateIntoString(topBannerTemplate, productMap);
            mainHtml = mainHtml.replace(Utility.TOP_BANNER_AD_SECTION_IDENTIFIER, topBannerHtml);
        }
        return mainHtml;
    }

    /**
     * @param content : java.util.Map<java.lang.String, java.lang.Object>
     * @param templateName : java.lang.String
     * Validates whether email content parameters count matches and other validations satisfies
     * for the relevant email template
     */
    private void validateObjectContent(Map<String, Object> content, String templateName) {
        List<String> keyMapList = new ArrayList<>(content.keySet());
        MailContent firstByTemplateName = mailContentRepository.findFirstByTemplateName(templateName);

        String[] expectedNonEditableList;
        if (null != firstByTemplateName) {
            // Looping through the non-editable tags and retrieve all the occurrences in between <non-editable> and </non-editable> delimiters
            expectedNonEditableList = StringUtils.substringsBetween(
                    firstByTemplateName.getContent(), Utility.NON_EDITABLE_IDENTIFIER_START, Utility.NON_EDITABLE_IDENTIFIER_END);
        } else {
            throw new SystemWarningException(Utility.EX_INVALID_TEMPLATE_NAME);
        }

        for (String nonEditableData : expectedNonEditableList) {
            if (!Utility.ITEM_DETAILS_SECTION_IDENTIFIER.equals(nonEditableData) && !keyMapList.contains(nonEditableData)) {
                throw new SystemWarningException(Utility.EX_INVALID_NO_OF_CONTENT_DATA_MAP_PROVIDED);
            }
        }
    }

    /**
     * @param emailRequestDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * @throws SystemWarningException
     * Validates Html email send details
     */
    private void validateSendEmailDetails(SendEmailRequestDto emailRequestDto) throws SystemWarningException {
        if (null == emailRequestDto) {
            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
        }
        validateCommonEmailDetails(emailRequestDto.getTemplateName(), emailRequestDto.getMail());
    }

    /**
     * @param fileEmailRequestDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * @throws SystemWarningException
     * Validates Html file mail send details
     */
    private void validateSendEmailDetails(SendFileEmailRequestDto fileEmailRequestDto) throws SystemWarningException {
        if (null == fileEmailRequestDto) {
            throw new SystemWarningException(Utility.EX_ALL_FIELDS_REQUIRED);
        }
        if (Utility.isNullOrEmpty(fileEmailRequestDto.getFilename())) {
            throw new SystemWarningException(Utility.EX_FILE_NAME_REQUIRED);
        }
        if (!fileEmailRequestDto.getFilename().matches(Utility.PDF_FILE_FORMAT_REGEX)) {
            throw new SystemWarningException(Utility.EX_INVALID_FILE_EXTENSION);
        }
        if (null == fileEmailRequestDto.getBase64file() || fileEmailRequestDto.getBase64file().isBlank()) {
            throw new SystemWarningException(Utility.EX_FILE_DATA_REQUIRED);
        }
        validateCommonEmailDetails(fileEmailRequestDto.getTemplateName(), fileEmailRequestDto.getMail());
    }

    /**
     * @param templateName : java.lang.String
     * @param mail : com.cloudofgoods.notification.dto.request.MailRequestDto
     * Validates email send details common for both html mail and file mail
     */
    private void validateCommonEmailDetails(String templateName, MailRequestDto mail) {
        if (Utility.isNullOrEmpty(templateName)) {
            throw new SystemWarningException(Utility.EX_TEMPLATE_NAME_REQUIRED);
        }
        if (!featureFlagRepository.existsByName(templateName)) {
            throw new SystemWarningException(String.format(Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME, templateName));
        }
        if (null == mail) {
            throw new SystemWarningException(Utility.EX_MAIL_DETAILS_REQUIRED);
        }
    }

    /**
     * @param sendEmailDto : com.cloudofgoods.notification.dto.request.SendEmailRequestDto
     * @param html : java.lang.String
     * Saves sent html email data as log for future reference
     */
    private void saveHtmlEmailHst(SendEmailRequestDto sendEmailDto, String html) {
        saveEmailHst(html, sendEmailDto.getUserId());
    }

    /**
     * @param sendEmailDto : com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto
     * @param html : java.lang.String
     * Saves sent file email data as log for future reference
     */
    private void saveFileEmailHst(SendFileEmailRequestDto sendEmailDto, String html) {
        saveEmailHst(html, sendEmailDto.getUserId());
    }

    /**
     * @param html : java.lang.String
     * @param userId : long
     * Saves sent email hst data
     */
    private void saveEmailHst(String html, long userId) {
        SentEmailLog sentEmailLog = new SentEmailLog();
        sentEmailLog.setUserId(userId);
        sentEmailLog.setEmailContent(html);
        sentEmailLog.setSentDateTime(Timestamp.valueOf(LocalDateTime.now()));
        sentEmailLogRepository.save(sentEmailLog);
        log.info(String.format(Utility.LOG_SENT_EMAIL_DETAILS_SAVED_SUCCESSFULLY, userId));
    }

    /**
     * @param templateName : java.lang.String
     * @return boolean
     * Returns boolean value based on existence of a feature flag by its name and activeStatus
     */
    private boolean isExistsByTemplateNameAndActiveStatus(String templateName) {
        return featureFlagRepository.existsByNameAndStatus(templateName, Utility.ACTIVE_STATUS_TRUE);
    }
}

