package com.notification.service;

import com.notification.dto.request.NotificationRequestDto;
import com.notification.exception.SystemWarningException;
import com.notification.service.impl.PlivoSmsServiceImpl;
import com.notification.util.Utility;
import com.plivo.api.models.message.MessageCreateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlivoSmsServiceImplTest {

    private PlivoSmsServiceImpl plivoSmsService;

    @Autowired
    public void setPlivoSmsService(ApplicationContext context) {
        plivoSmsService = (PlivoSmsServiceImpl) context.getBean(Utility.PLIVO_SMS_SERVICE_QUALIFIER);
    }

    @Test
    void isPlivoSmsSendsSuccessfullyForValidInputData() {
        long userId = 100;
        String templateName = "order_confirmation_email_to_customer";
        String mobileNo = "+94753937869";

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        MessageCreateResponse messageCreateResponse = plivoSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        assertEquals("message(s) queued", messageCreateResponse.getMessage());
    }

    @Test
    void isPlivoSmsSedFailsAndGivesInvalidMobileNoFormatExceptionForInValidMobileNoFormats() {
        long userId = 100;
        String templateName = "order_confirmation_email_to_customer";
        String mobileNo = "0753937869"; // should provide mobile no with the country code. Eg:- +94753937869

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            plivoSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        });

        String expectedMessage = String.format(Utility.EX_INVALID_MOBILE_NUMBER_FORMAT, mobileNo);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_TEMPLATE_NAME_REQUIRED})
    void isPlivoSmsThrowsTemplateNameRequiredExceptionForNullTemplateName(String templateName,
                                                                          String templateNameNullExpectedMessage) {
        long userId = 100;
        String mobileNo = "+94753937869";

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            plivoSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        });

        String templateNameNullActualMessage = exception.getMessage();
        assertTrue(templateNameNullActualMessage.contains(templateNameNullExpectedMessage));
    }

    @ParameterizedTest
    @CsvSource({" ," + Utility.EX_TEMPLATE_NAME_REQUIRED})
    void isPlivoSmsThrowsTemplateNameRequiredExceptionForEmptyTemplateName(String templateName,
                                                                          String templateNameEmptyExpectedMessage) {
        long userId = 100;
        String mobileNo = "+94753937869";

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            plivoSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        });

        String templateNameNullActualMessage = exception.getMessage();
        assertTrue(templateNameNullActualMessage.contains(templateNameEmptyExpectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_MOBILE_NO_REQUIRED})
    void isPlivoSmsThrowsMobileNumberRequiredExceptionForNullMobileNumber(String mobileNo,
                                                                          String mobileNoNullExpectedMessage) {
        long userId = 100;
        String templateName = "order_confirmation_email_to_customer";

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            plivoSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        });

        String templateNameNullActualMessage = exception.getMessage();
        assertTrue(templateNameNullActualMessage.contains(mobileNoNullExpectedMessage));
    }

    @ParameterizedTest
    @CsvSource({" ," + Utility.EX_MOBILE_NO_REQUIRED})
    void isPlivoSmsThrowsMobileNumberRequiredExceptionForEmptyMobileNumber(String mobileNo,
                                                                           String mobileNoEmptyExpectedMessage) {
        long userId = 100;
        String templateName = "order_confirmation_email_to_customer";

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            plivoSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        });

        String templateNameNullActualMessage = exception.getMessage();
        assertTrue(templateNameNullActualMessage.contains(mobileNoEmptyExpectedMessage));
    }

    @Test
    void isPlivoSmsThrowsNotificationDetailsRequiredExceptionForNullNotificationDto() {
        long userId = 100;
        String templateName = "order_confirmation_email_to_customer";
        String mobileNo = "+94753937869";

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            plivoSmsService.sendSms(userId, templateName, mobileNo, null);
        });

        String templateNameNullActualMessage = exception.getMessage();
        assertTrue(templateNameNullActualMessage.contains(Utility.EX_NOTIFICATION_DETAILS_REQUIRED));
    }
}
