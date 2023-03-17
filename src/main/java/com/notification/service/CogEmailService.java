package com.notification.service;

import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import com.notification.dto.EmailDto;
import com.notification.dto.ViewEmailContentDto;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface CogEmailService {

    void sendHtmlEmail(SendEmailRequestDto sendEmailRequestDto) throws MessagingException, TemplateException, IOException;

    void sendFileMail(SendFileEmailRequestDto fileEmailRequestDto) throws MessagingException, TemplateException, IOException;

    String updateEmailContent(EmailDto emailDto, String userName) throws RuntimeException;

    ViewEmailContentDto getEmailContent(String templateName);
}
