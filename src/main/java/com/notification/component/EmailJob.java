//package com.cloudofgoods.notification.component;
//
//import com.cloudofgoods.notification.dto.request.MailRequestDto;
//import com.cloudofgoods.notification.entity.MailContent;
//import com.cloudofgoods.notification.repository.EmailSchedulerLogRepository;
//import com.cloudofgoods.notification.repository.MailContentRepository;
//import com.cloudofgoods.notification.repository.ScheduledEmailRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.springframework.boot.autoconfigure.mail.MailProperties;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//import org.springframework.stereotype.Component;
//
//import javax.mail.internet.MimeMessage;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class EmailJob extends QuartzJobBean {
//
//    private final JavaMailSender mailSender;
//    private final MailProperties mailProperties;
//    private final MailContentRepository mailContentRepository;
//    private final EmailSchedulerLogRepository emailSchedulerLogRepository;
//    private final ScheduledEmailRepository scheduledEmailRepository;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) {
//        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
//        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
//        String subject = jobDataMap.getString("subject");
//        String body = jobDataMap.getString("body");
//        String recipientEmail = jobDataMap.getString("email");
//        MailRequestDto mailRequestDto = (MailRequestDto) jobDataMap.get("abc");
//         sendMail(mailProperties.getUsername(), recipientEmail, subject, body,mailRequestDto);
//    }
//
//    private void sendMail(String fromEmail, String toEmail, String subject, String body, MailRequestDto mailRequestDto) {
//        try {
//
//            log.info("Sending Email to {}", toEmail);
//            MimeMessage message = mailSender.createMimeMessage();
//            String templateName = mailRequestDto.getContent().get("templateName").toString();
//            MailContent firstByTemplateName = mailContentRepository.findByTemplateNameEquals(templateName);
//            String content = firstByTemplateName.getContent();
//            Map<String, Object> map = mailRequestDto.getContent();
//            System.out.println(content);
//            System.out.println("_________________________________________________________________________");
//            for (String key : map.keySet()) {
//                Object value = map.get(key);
//                String replacement = (value != null) ? value.toString() : "";
//                System.out.println(replacement +"  R");
//                System.out.println(key +  "  eee");
//                content = content.replaceAll("" + key + "", replacement);
//                System.out.println(content + " sseeeeeeeeeeeeeee");
//            }
//            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
//            messageHelper.setSubject(subject);
//            messageHelper.setText(content, true);
//            messageHelper.setFrom(fromEmail);
//            messageHelper.setTo(toEmail);
//            mailSender.send(message);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error("Failed to send email to {}", toEmail);
//        }
//
//
//
//
//    }
//}
package com.notification.component;

import com.notification.dto.request.MailRequestDto;
import com.notification.entity.MailContent;
import com.notification.repository.MailContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailJob extends QuartzJobBean {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final MailContentRepository mailContentRepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");
        MailRequestDto mailRequestDto = (MailRequestDto) jobDataMap.get("abc");
        sendMail(mailProperties.getUsername(), recipientEmail, subject, body,mailRequestDto);
    }

    private void sendMail(String fromEmail, String toEmail, String subject, String body, MailRequestDto mailRequestDto) {
        try {
            log.info("Sending Email to {}", toEmail);
            MimeMessage message = mailSender.createMimeMessage();
            String templateName = mailRequestDto.getTemplateName();
            MailContent firstByTemplateName = mailContentRepository.findByTemplateNameEquals(templateName);
            String content = firstByTemplateName.getContent();
            Map<String, Object> map = mailRequestDto.getContent();
            System.out.println(content);
            System.out.println("///////////////////////////////////////////");
            for (String key : map.keySet()) {
                Object value = map.get(key);
                String replacement = (value != null) ? value.toString() : "";
                System.out.println(replacement + " ");
                System.out.println(key + ".................................");
                try {
                    content = content.replaceAll(Pattern.quote(key), Matcher.quoteReplacement(replacement));
                } catch (PatternSyntaxException e) {
                    log.error("Invalid regular expression: {}", e.getMessage());

                }
                System.out.println(content + "--------------------- ");
            }
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            mailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to send email to {}", toEmail);
        }


    }
}
