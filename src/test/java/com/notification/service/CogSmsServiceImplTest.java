package com.notification.service;

import com.notification.dto.SmsContentDto;
import com.notification.exception.SystemWarningException;
import com.notification.util.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CogSmsServiceImplTest {

    @Autowired
    private CogSmsService cogSmsService;

    @Test
    void isDisplaysSmsContentSuccessfullyReturnForValidTemplateName() {
        String templateName = "order_confirmation_email_to_customer";
        assertDoesNotThrow(() -> {
            cogSmsService.getMessageContent(templateName);
        });
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_TEMPLATE_NAME_REQUIRED})
    void isDisplaySmsContentFailsForTemplateNameNull(String templateName,
                                                     String expectedMessage) {
        Exception exception = assertThrows(SystemWarningException.class, () -> {
            cogSmsService.getMessageContent(templateName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({" ," + Utility.EX_TEMPLATE_NAME_REQUIRED})
    void isDisplaySmsContentFailsForEmptyTemplateName(String templateName,
                                                      String expectedMessage) {
        Exception exception = assertThrows(SystemWarningException.class, () -> {
            cogSmsService.getMessageContent(templateName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"notExistingTemplateName," + Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME})
    void isDisplaySmsContentFailsWhenNotExistingTemplateNameProvided(String templateName,
                                                                     String expectedMessage) {
        Exception exception = assertThrows(SystemWarningException.class, () -> {
            cogSmsService.getMessageContent(templateName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(String.format(expectedMessage, templateName)));
    }

    @Test
    void isSmsContentUpdatedSuccessfullyForValidData() {
        String templateName = "order_confirmation_email_to_customer";
        String messageTitle = "Congrats <non-editable><username></non-editable>, your order has been accepted";
        String messageBody = "We're delighted that your order has been accepted by <non-editable><vendor-name></non-editable>. They'll be in touch with you to make delivery arrangements. Your order is now confirmed.";
        String userName = "SYSTEM";

        SmsContentDto smsDto = new SmsContentDto();
        smsDto.setTemplateName(templateName);
        smsDto.setMessageTitle(messageTitle);
        smsDto.setMessageBody(messageBody);

        Assertions.assertDoesNotThrow(() -> cogSmsService.updateMessageContent(smsDto, userName));
    }

    @Test
    void isSmsContentUpdateFailedForNoOfNonEditableAttributeCountMismatch() {
        String templateName = "order_confirmation_email_to_customer";
        String messageTitle = "Congrats , your order has been accepted";
        String messageBody = "We're delighted that your order has been accepted by <non-editable><vendor-name></non-editable>. They'll be in touch with you to make delivery arrangements. Your order is now confirmed.";
        String userName = "SYSTEM";

        SmsContentDto smsDto = new SmsContentDto();
        smsDto.setTemplateName(templateName);
        smsDto.setMessageTitle(messageTitle);
        smsDto.setMessageBody(messageBody);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            cogSmsService.updateMessageContent(smsDto, userName);
        });

        String expectedMessage = Utility.EX_MISMATCH_NON_EDITABLE_ATTRIBUTE_COUNT;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isSmsContentUpdateFailedForEditingNonEditableAttributeValuesInsideTags() {
        String templateName = "order_confirmation_email_to_customer";
        String messageTitle = "Congrats <non-editable>k</non-editable>, your order has been accepted";
        String messageBody = "We're delighted that your order has been accepted by <non-editable><vendor-name></non-editable>. They'll be in touch with you to make delivery arrangements. Your order is now confirmed.";
        String userName = "SYSTEM";

        SmsContentDto smsDto = new SmsContentDto();
        smsDto.setTemplateName(templateName);
        smsDto.setMessageTitle(messageTitle);
        smsDto.setMessageBody(messageBody);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            cogSmsService.updateMessageContent(smsDto, userName);
        });

        String expectedMessage = Utility.EX_NON_EDITABLE_ATTRIBUTE_MODIFIED;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
