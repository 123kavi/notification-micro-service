package com.notification.service;

import com.notification.dto.request.NotificationRequestDto;
import com.notification.service.impl.TwilioSmsServiceImpl;

import com.notification.util.Utility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TwilioSmsServiceImplTest {

    private TwilioSmsServiceImpl twilioSmsService;

    @Autowired
    public void setPlivoSmsService(ApplicationContext context) {
        twilioSmsService = (TwilioSmsServiceImpl) context.getBean(Utility.TWILIO_SMS_SERVICE_QUALIFIER);
    }

    @Test
    void shouldGivesAnUnSupportedOperationExceptionForCurrentVersion() {
        // Prerequisite parameters needed
        long userId = 100;
        String templateName = "order_confirmation_email_to_customer";
        String mobileNo = "+94753937869";

        // Message Content Data
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("<user-name>", "Saman");
        msgContent.put("<vendor-name>", "Lakmal");
        NotificationRequestDto notificationDto = new NotificationRequestDto();
        notificationDto.setContent(msgContent);

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            twilioSmsService.sendSms(userId, templateName, mobileNo, notificationDto);
        });

        String expectedMessage = Utility.EX_UNSUPPORTED_OPERATION;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
