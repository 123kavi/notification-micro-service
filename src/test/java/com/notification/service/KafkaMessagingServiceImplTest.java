//package com.cloudofgoods.notification.service;
//
//import com.cloudofgoods.notification.dto.ItemDetailsDto;
//import com.cloudofgoods.notification.dto.request.MailContentDto;
//import com.cloudofgoods.notification.dto.request.NotificationRequestDto;
//import com.cloudofgoods.notification.dto.request.SendEmailRequestDto;
//import com.cloudofgoods.notification.dto.request.SendFileEmailRequestDto;
//import com.cloudofgoods.notification.exception.SystemWarningException;
//import com.cloudofgoods.notification.util.Utility;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class KafkaMessagingServiceImplTest {
//
//    @Autowired
//    private KafkaMessagingService kafkaMessagingService;
//
//    @Test
//    void isHtmlMailSendSuccessfullyWithNotificationsThroughKafkaMessageStreamsForValidData() {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputTemplateName = "order_confirmation_email_to_customer";
//        String inputMobileNo = "+94753937869";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        // Send Email Request Dto initialization
//        SendEmailRequestDto emailRequestDto = new SendEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        assertDoesNotThrow(() -> kafkaMessagingService.sendHtmlMailWithNotifications(emailRequestDto));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"," + Utility.EX_TEMPLATE_NAME_REQUIRED})
//    void isHtmlMailSendFailsForNullTemplateNameProvided(String inputTemplateName, String expectedMessage) {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputMobileNo = "+94753937869";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        // Send Email Request Dto initialization
//        SendEmailRequestDto emailRequestDto = new SendEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            kafkaMessagingService.sendHtmlMailWithNotifications(emailRequestDto);
//        });
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({" ," + Utility.EX_TEMPLATE_NAME_REQUIRED})
//    void isHtmlMailSendFailsForEmptyTemplateNameProvided(String inputTemplateName, String expectedMessage) {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputMobileNo = "+94753937869";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        // Send Email Request Dto initialization
//        SendEmailRequestDto emailRequestDto = new SendEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            kafkaMessagingService.sendHtmlMailWithNotifications(emailRequestDto);
//        });
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"notExistingTemplateName," + Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME})
//    void isHtmlMailSendThrowsNoResultsFoundForTemplateNameException(String inputTemplateName, String expectedMessage) {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputMobileNo = "+94753937869";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        // Send Email Request Dto initialization
//        SendEmailRequestDto emailRequestDto = new SendEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            kafkaMessagingService.sendHtmlMailWithNotifications(emailRequestDto);
//        });
//        String actualMessage = exception.getMessage();
//        System.out.println(actualMessage);
//        assertTrue(actualMessage.contains(String.format(expectedMessage, inputTemplateName)));
//    }
//
//    @Test
//    void isFileMailSendSuccessfullyWithNotificationsThroughKafkaMessageStreamsForValidData() {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputTemplateName = "order_confirmation_email_to_customer";
//        String inputMobileNo = "+94753937869";
//        String fileName = "file.pdf";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        byte[] byteData = {37, 80, 68, 70, 45, 49, 46, 51, 13, 10, 37, -30, -29, -49, -45, 13, 10, 13, 10, 49, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 67, 97, 116, 97, 108, 111, 103, 13, 10, 47, 79, 117, 116, 108, 105, 110, 101, 115, 32, 50, 32, 48, 32, 82, 13, 10, 47, 80, 97, 103, 101, 115, 32, 51, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 50, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 79, 117, 116, 108, 105, 110, 101, 115, 13, 10, 47, 67, 111, 117, 110, 116, 32, 48, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 51, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 13, 10, 47, 67, 111, 117, 110, 116, 32, 50, 13, 10, 47, 75, 105, 100, 115, 32, 91, 32, 52, 32, 48, 32, 82, 32, 54, 32, 48, 32, 82, 32, 93, 32, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 52, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 13, 10, 47, 80, 97, 114, 101, 110, 116, 32, 51, 32, 48, 32, 82, 13, 10, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 13, 10, 47, 70, 111, 110, 116, 32, 60, 60, 13, 10, 47, 70, 49, 32, 57, 32, 48, 32, 82, 32, 13, 10, 62, 62, 13, 10, 47, 80, 114, 111, 99, 83, 101, 116, 32, 56, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 47, 77, 101, 100, 105, 97, 66, 111, 120, 32, 91, 48, 32, 48, 32, 54, 49, 50, 46, 48, 48, 48, 48, 32, 55, 57, 50, 46, 48, 48, 48, 48, 93, 13, 10, 47, 67, 111, 110, 116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 53, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 32, 47, 76, 101, 110, 103, 116, 104, 32, 49, 48, 55, 52, 32, 62, 62, 13, 10, 115, 116, 114, 101, 97, 109, 13, 10, 50, 32, 74, 13, 10, 66, 84, 13, 10, 48, 32, 48, 32, 48, 32, 114, 103, 13, 10, 47, 70, 49, 32, 48, 48, 50, 55, 32, 84, 102, 13, 10, 53, 55, 46, 51, 55, 53, 48, 32, 55, 50, 50, 46, 50, 56, 48, 48, 32, 84, 100, 13, 10, 40, 32, 65, 32, 83, 105, 109, 112, 108, 101, 32, 80, 68, 70, 32, 70, 105, 108, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 56, 56, 46, 54, 48, 56, 48, 32, 84, 100, 13, 10, 40, 32, 84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 115, 109, 97, 108, 108, 32, 100, 101, 109, 111, 110, 115, 116, 114, 97, 116, 105, 111, 110, 32, 46, 112, 100, 102, 32, 102, 105, 108, 101, 32, 45, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 54, 52, 46, 55, 48, 52, 48, 32, 84, 100, 13, 10, 40, 32, 106, 117, 115, 116, 32, 102, 111, 114, 32, 117, 115, 101, 32, 105, 110, 32, 116, 104, 101, 32, 86, 105, 114, 116, 117, 97, 108, 32, 77, 101, 99, 104, 97, 110, 105, 99, 115, 32, 116, 117, 116, 111, 114, 105, 97, 108, 115, 46, 32, 77, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 53, 50, 46, 55, 53, 50, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 50, 56, 46, 56, 52, 56, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 49, 54, 46, 56, 57, 54, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 66, 111, 114, 105, 110, 103, 44, 32, 122, 122, 122, 122, 122, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 48, 52, 46, 57, 52, 52, 48, 32, 84, 100, 13, 10, 40, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 57, 50, 46, 57, 57, 50, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 54, 57, 46, 48, 56, 56, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 53, 55, 46, 49, 51, 54, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 69, 118, 101, 110, 32, 109, 111, 114, 101, 46, 32, 67, 111, 110, 116, 105, 110, 117, 101, 100, 32, 111, 110, 32, 112, 97, 103, 101, 32, 50, 32, 46, 46, 46, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 101, 110, 100, 115, 116, 114, 101, 97, 109, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 54, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 13, 10, 47, 80, 97, 114, 101, 110, 116, 32, 51, 32, 48, 32, 82, 13, 10, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 13, 10, 47, 70, 111, 110, 116, 32, 60, 60, 13, 10, 47, 70, 49, 32, 57, 32, 48, 32, 82, 32, 13, 10, 62, 62, 13, 10, 47, 80, 114, 111, 99, 83, 101, 116, 32, 56, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 47, 77, 101, 100, 105, 97, 66, 111, 120, 32, 91, 48, 32, 48, 32, 54, 49, 50, 46, 48, 48, 48, 48, 32, 55, 57, 50, 46, 48, 48, 48, 48, 93, 13, 10, 47, 67, 111, 110, 116, 101, 110, 116, 115, 32, 55, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 55, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 32, 47, 76, 101, 110, 103, 116, 104, 32, 54, 55, 54, 32, 62, 62, 13, 10, 115, 116, 114, 101, 97, 109, 13, 10, 50, 32, 74, 13, 10, 66, 84, 13, 10, 48, 32, 48, 32, 48, 32, 114, 103, 13, 10, 47, 70, 49, 32, 48, 48, 50, 55, 32, 84, 102, 13, 10, 53, 55, 46, 51, 55, 53, 48, 32, 55, 50, 50, 46, 50, 56, 48, 48, 32, 84, 100, 13, 10, 40, 32, 83, 105, 109, 112, 108, 101, 32, 80, 68, 70, 32, 70, 105, 108, 101, 32, 50, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 56, 56, 46, 54, 48, 56, 48, 32, 84, 100, 13, 10, 40, 32, 46, 46, 46, 99, 111, 110, 116, 105, 110, 117, 101, 100, 32, 102, 114, 111, 109, 32, 112, 97, 103, 101, 32, 49, 46, 32, 89, 101, 116, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 55, 54, 46, 54, 53, 54, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 54, 52, 46, 55, 48, 52, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 79, 104, 44, 32, 104, 111, 119, 32, 98, 111, 114, 105, 110, 103, 32, 116, 121, 112, 105, 110, 103, 32, 116, 104, 105, 115, 32, 115, 116, 117, 102, 102, 46, 32, 66, 117, 116, 32, 110, 111, 116, 32, 97, 115, 32, 98, 111, 114, 105, 110, 103, 32, 97, 115, 32, 119, 97, 116, 99, 104, 105, 110, 103, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 53, 50, 46, 55, 53, 50, 48, 32, 84, 100, 13, 10, 40, 32, 112, 97, 105, 110, 116, 32, 100, 114, 121, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 52, 48, 46, 56, 48, 48, 48, 32, 84, 100, 13, 10, 40, 32, 66, 111, 114, 105, 110, 103, 46, 32, 32, 77, 111, 114, 101, 44, 32, 97, 32, 108, 105, 116, 116, 108, 101, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 84, 104, 101, 32, 101, 110, 100, 44, 32, 97, 110, 100, 32, 106, 117, 115, 116, 32, 97, 115, 32, 119, 101, 108, 108, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 101, 110, 100, 115, 116, 114, 101, 97, 109, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 56, 32, 48, 32, 111, 98, 106, 13, 10, 91, 47, 80, 68, 70, 32, 47, 84, 101, 120, 116, 93, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 57, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 70, 111, 110, 116, 13, 10, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 13, 10, 47, 78, 97, 109, 101, 32, 47, 70, 49, 13, 10, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 72, 101, 108, 118, 101, 116, 105, 99, 97, 13, 10, 47, 69, 110, 99, 111, 100, 105, 110, 103, 32, 47, 87, 105, 110, 65, 110, 115, 105, 69, 110, 99, 111, 100, 105, 110, 103, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 49, 48, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 67, 114, 101, 97, 116, 111, 114, 32, 40, 82, 97, 118, 101, 32, 92, 40, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 110, 101, 118, 114, 111, 110, 97, 46, 99, 111, 109, 47, 114, 97, 118, 101, 92, 41, 41, 13, 10, 47, 80, 114, 111, 100, 117, 99, 101, 114, 32, 40, 78, 101, 118, 114, 111, 110, 97, 32, 68, 101, 115, 105, 103, 110, 115, 41, 13, 10, 47, 67, 114, 101, 97, 116, 105, 111, 110, 68, 97, 116, 101, 32, 40, 68, 58, 50, 48, 48, 54, 48, 51, 48, 49, 48, 55, 50, 56, 50, 54, 41, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 120, 114, 101, 102, 13, 10, 48, 32, 49, 49, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 49, 57, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 57, 51, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 49, 52, 55, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 50, 50, 50, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 51, 57, 48, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 49, 53, 50, 50, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 49, 54, 57, 48, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 52, 50, 51, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 52, 53, 54, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 53, 55, 52, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 13, 10, 116, 114, 97, 105, 108, 101, 114, 13, 10, 60, 60, 13, 10, 47, 83, 105, 122, 101, 32, 49, 49, 13, 10, 47, 82, 111, 111, 116, 32, 49, 32, 48, 32, 82, 13, 10, 47, 73, 110, 102, 111, 32, 49, 48, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 13, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 13, 10, 50, 55, 49, 52, 13, 10, 37, 37, 69, 79, 70, 13, 10};
//        String encodedFileData = Utility.getByteArrayEncodedString(byteData);
//
//        // Send Email Request Dto initialization
//        SendFileEmailRequestDto emailRequestDto = new SendFileEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//        emailRequestDto.setBase64file(encodedFileData);
//        emailRequestDto.setFilename(fileName);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        assertDoesNotThrow(() -> kafkaMessagingService.sendFileMailWithNotifications(emailRequestDto));
//    }
//
//    @ParameterizedTest
//    @CsvSource({" ," + Utility.EX_TEMPLATE_NAME_REQUIRED})
//    void isFileMailSendFailsForEmptyTemplateNameProvided(String inputTemplateName, String expectedMessage) {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputMobileNo = "+94753937869";
//        String fileName = "file.pdf";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        byte[] byteData = {37, 80, 68, 70, 45, 49, 46, 51, 13, 10, 37, -30, -29, -49, -45, 13, 10, 13, 10, 49, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 67, 97, 116, 97, 108, 111, 103, 13, 10, 47, 79, 117, 116, 108, 105, 110, 101, 115, 32, 50, 32, 48, 32, 82, 13, 10, 47, 80, 97, 103, 101, 115, 32, 51, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 50, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 79, 117, 116, 108, 105, 110, 101, 115, 13, 10, 47, 67, 111, 117, 110, 116, 32, 48, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 51, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 13, 10, 47, 67, 111, 117, 110, 116, 32, 50, 13, 10, 47, 75, 105, 100, 115, 32, 91, 32, 52, 32, 48, 32, 82, 32, 54, 32, 48, 32, 82, 32, 93, 32, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 52, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 13, 10, 47, 80, 97, 114, 101, 110, 116, 32, 51, 32, 48, 32, 82, 13, 10, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 13, 10, 47, 70, 111, 110, 116, 32, 60, 60, 13, 10, 47, 70, 49, 32, 57, 32, 48, 32, 82, 32, 13, 10, 62, 62, 13, 10, 47, 80, 114, 111, 99, 83, 101, 116, 32, 56, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 47, 77, 101, 100, 105, 97, 66, 111, 120, 32, 91, 48, 32, 48, 32, 54, 49, 50, 46, 48, 48, 48, 48, 32, 55, 57, 50, 46, 48, 48, 48, 48, 93, 13, 10, 47, 67, 111, 110, 116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 53, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 32, 47, 76, 101, 110, 103, 116, 104, 32, 49, 48, 55, 52, 32, 62, 62, 13, 10, 115, 116, 114, 101, 97, 109, 13, 10, 50, 32, 74, 13, 10, 66, 84, 13, 10, 48, 32, 48, 32, 48, 32, 114, 103, 13, 10, 47, 70, 49, 32, 48, 48, 50, 55, 32, 84, 102, 13, 10, 53, 55, 46, 51, 55, 53, 48, 32, 55, 50, 50, 46, 50, 56, 48, 48, 32, 84, 100, 13, 10, 40, 32, 65, 32, 83, 105, 109, 112, 108, 101, 32, 80, 68, 70, 32, 70, 105, 108, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 56, 56, 46, 54, 48, 56, 48, 32, 84, 100, 13, 10, 40, 32, 84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 115, 109, 97, 108, 108, 32, 100, 101, 109, 111, 110, 115, 116, 114, 97, 116, 105, 111, 110, 32, 46, 112, 100, 102, 32, 102, 105, 108, 101, 32, 45, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 54, 52, 46, 55, 48, 52, 48, 32, 84, 100, 13, 10, 40, 32, 106, 117, 115, 116, 32, 102, 111, 114, 32, 117, 115, 101, 32, 105, 110, 32, 116, 104, 101, 32, 86, 105, 114, 116, 117, 97, 108, 32, 77, 101, 99, 104, 97, 110, 105, 99, 115, 32, 116, 117, 116, 111, 114, 105, 97, 108, 115, 46, 32, 77, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 53, 50, 46, 55, 53, 50, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 50, 56, 46, 56, 52, 56, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 49, 54, 46, 56, 57, 54, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 66, 111, 114, 105, 110, 103, 44, 32, 122, 122, 122, 122, 122, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 48, 52, 46, 57, 52, 52, 48, 32, 84, 100, 13, 10, 40, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 57, 50, 46, 57, 57, 50, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 54, 57, 46, 48, 56, 56, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 53, 55, 46, 49, 51, 54, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 69, 118, 101, 110, 32, 109, 111, 114, 101, 46, 32, 67, 111, 110, 116, 105, 110, 117, 101, 100, 32, 111, 110, 32, 112, 97, 103, 101, 32, 50, 32, 46, 46, 46, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 101, 110, 100, 115, 116, 114, 101, 97, 109, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 54, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 13, 10, 47, 80, 97, 114, 101, 110, 116, 32, 51, 32, 48, 32, 82, 13, 10, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 13, 10, 47, 70, 111, 110, 116, 32, 60, 60, 13, 10, 47, 70, 49, 32, 57, 32, 48, 32, 82, 32, 13, 10, 62, 62, 13, 10, 47, 80, 114, 111, 99, 83, 101, 116, 32, 56, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 47, 77, 101, 100, 105, 97, 66, 111, 120, 32, 91, 48, 32, 48, 32, 54, 49, 50, 46, 48, 48, 48, 48, 32, 55, 57, 50, 46, 48, 48, 48, 48, 93, 13, 10, 47, 67, 111, 110, 116, 101, 110, 116, 115, 32, 55, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 55, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 32, 47, 76, 101, 110, 103, 116, 104, 32, 54, 55, 54, 32, 62, 62, 13, 10, 115, 116, 114, 101, 97, 109, 13, 10, 50, 32, 74, 13, 10, 66, 84, 13, 10, 48, 32, 48, 32, 48, 32, 114, 103, 13, 10, 47, 70, 49, 32, 48, 48, 50, 55, 32, 84, 102, 13, 10, 53, 55, 46, 51, 55, 53, 48, 32, 55, 50, 50, 46, 50, 56, 48, 48, 32, 84, 100, 13, 10, 40, 32, 83, 105, 109, 112, 108, 101, 32, 80, 68, 70, 32, 70, 105, 108, 101, 32, 50, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 56, 56, 46, 54, 48, 56, 48, 32, 84, 100, 13, 10, 40, 32, 46, 46, 46, 99, 111, 110, 116, 105, 110, 117, 101, 100, 32, 102, 114, 111, 109, 32, 112, 97, 103, 101, 32, 49, 46, 32, 89, 101, 116, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 55, 54, 46, 54, 53, 54, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 54, 52, 46, 55, 48, 52, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 79, 104, 44, 32, 104, 111, 119, 32, 98, 111, 114, 105, 110, 103, 32, 116, 121, 112, 105, 110, 103, 32, 116, 104, 105, 115, 32, 115, 116, 117, 102, 102, 46, 32, 66, 117, 116, 32, 110, 111, 116, 32, 97, 115, 32, 98, 111, 114, 105, 110, 103, 32, 97, 115, 32, 119, 97, 116, 99, 104, 105, 110, 103, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 53, 50, 46, 55, 53, 50, 48, 32, 84, 100, 13, 10, 40, 32, 112, 97, 105, 110, 116, 32, 100, 114, 121, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 52, 48, 46, 56, 48, 48, 48, 32, 84, 100, 13, 10, 40, 32, 66, 111, 114, 105, 110, 103, 46, 32, 32, 77, 111, 114, 101, 44, 32, 97, 32, 108, 105, 116, 116, 108, 101, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 84, 104, 101, 32, 101, 110, 100, 44, 32, 97, 110, 100, 32, 106, 117, 115, 116, 32, 97, 115, 32, 119, 101, 108, 108, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 101, 110, 100, 115, 116, 114, 101, 97, 109, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 56, 32, 48, 32, 111, 98, 106, 13, 10, 91, 47, 80, 68, 70, 32, 47, 84, 101, 120, 116, 93, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 57, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 70, 111, 110, 116, 13, 10, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 13, 10, 47, 78, 97, 109, 101, 32, 47, 70, 49, 13, 10, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 72, 101, 108, 118, 101, 116, 105, 99, 97, 13, 10, 47, 69, 110, 99, 111, 100, 105, 110, 103, 32, 47, 87, 105, 110, 65, 110, 115, 105, 69, 110, 99, 111, 100, 105, 110, 103, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 49, 48, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 67, 114, 101, 97, 116, 111, 114, 32, 40, 82, 97, 118, 101, 32, 92, 40, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 110, 101, 118, 114, 111, 110, 97, 46, 99, 111, 109, 47, 114, 97, 118, 101, 92, 41, 41, 13, 10, 47, 80, 114, 111, 100, 117, 99, 101, 114, 32, 40, 78, 101, 118, 114, 111, 110, 97, 32, 68, 101, 115, 105, 103, 110, 115, 41, 13, 10, 47, 67, 114, 101, 97, 116, 105, 111, 110, 68, 97, 116, 101, 32, 40, 68, 58, 50, 48, 48, 54, 48, 51, 48, 49, 48, 55, 50, 56, 50, 54, 41, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 120, 114, 101, 102, 13, 10, 48, 32, 49, 49, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 49, 57, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 57, 51, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 49, 52, 55, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 50, 50, 50, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 51, 57, 48, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 49, 53, 50, 50, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 49, 54, 57, 48, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 52, 50, 51, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 52, 53, 54, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 53, 55, 52, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 13, 10, 116, 114, 97, 105, 108, 101, 114, 13, 10, 60, 60, 13, 10, 47, 83, 105, 122, 101, 32, 49, 49, 13, 10, 47, 82, 111, 111, 116, 32, 49, 32, 48, 32, 82, 13, 10, 47, 73, 110, 102, 111, 32, 49, 48, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 13, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 13, 10, 50, 55, 49, 52, 13, 10, 37, 37, 69, 79, 70, 13, 10};
//        String encodedFileData = Utility.getByteArrayEncodedString(byteData);
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        // Send Email Request Dto initialization
//        SendFileEmailRequestDto emailRequestDto = new SendFileEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//        emailRequestDto.setBase64file(encodedFileData);
//        emailRequestDto.setFilename(fileName);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            kafkaMessagingService.sendFileMailWithNotifications(emailRequestDto);
//        });
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"notExistingTemplateName," + Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME})
//    void isFileMailSendThrowsNoResultsFoundForTemplateNameForNotExistingTemplateNameProvided(String inputTemplateName, String expectedMessage) {
//        // Prerequisite parameter objects needed
//        long inputUserId = 100;
//        String inputMobileNo = "+94753937869";
//        String fileName = "file.pdf";
//
//        // Email content Params
//        Map<String, Object> contentParams = new HashMap<>();
//        contentParams.put("<receiver-name>", "Saman");
//        contentParams.put("<order-no>", "39709");
//        contentParams.put("<delivery-date>", "06/30/2022");
//        contentParams.put("<delivery-location-address>", "San Francisco, CA, USA");
//        contentParams.put("<pick-up-date>", "07/02/2022");
//        contentParams.put("<total-rental-amount>", "$779.00");
//        contentParams.put("<damage-waiver-amount>", "$30.00");
//        contentParams.put("<total-paid-amount>", "$809.00");
//        contentParams.put("<mail-send-by>", "The Cloud of Goods Team");
//
//        // Order Items List
//        List<ItemDetailsDto> ordersList = new ArrayList<>();
//        ItemDetailsDto itemDetailsDto1 = new ItemDetailsDto();
//        itemDetailsDto1.setProductName("Bariatric Electric Chair");
//        itemDetailsDto1.setProductQuantity(1);
//        itemDetailsDto1.setProductUrl("https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993");
//        ordersList.add(itemDetailsDto1);
//
//        ItemDetailsDto itemDetailsDto2 = new ItemDetailsDto();
//        itemDetailsDto2.setProductName("Lightweight Mobility Scooter");
//        itemDetailsDto2.setProductQuantity(2);
//        itemDetailsDto2.setProductUrl("https://m.media-amazon.com/images/I/719IT-ue0VL._AC_SL400_.jpg");
//        ordersList.add(itemDetailsDto2);
//
//        // Email Related Details
//        MailContentDto mailRequestDto = new MailContentDto();
//        mailRequestDto.setReceiverEmail("punsara@incubatelabs.com");
//        mailRequestDto.setTopBannerAdUrl("https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg");
//        mailRequestDto.setContent(contentParams);
//        mailRequestDto.setOrdersList(ordersList);
//
//        // Message Content Data
//        Map<String, Object> msgContent = new HashMap<>();
//        msgContent.put("<user-name>", "Saman");
//        msgContent.put("<vendor-name>", "Lakmal");
//
//        byte[] fileData = {37, 80, 68, 70, 45, 49, 46, 51, 13, 10, 37, -30, -29, -49, -45, 13, 10, 13, 10, 49, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 67, 97, 116, 97, 108, 111, 103, 13, 10, 47, 79, 117, 116, 108, 105, 110, 101, 115, 32, 50, 32, 48, 32, 82, 13, 10, 47, 80, 97, 103, 101, 115, 32, 51, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 50, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 79, 117, 116, 108, 105, 110, 101, 115, 13, 10, 47, 67, 111, 117, 110, 116, 32, 48, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 51, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 13, 10, 47, 67, 111, 117, 110, 116, 32, 50, 13, 10, 47, 75, 105, 100, 115, 32, 91, 32, 52, 32, 48, 32, 82, 32, 54, 32, 48, 32, 82, 32, 93, 32, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 52, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 13, 10, 47, 80, 97, 114, 101, 110, 116, 32, 51, 32, 48, 32, 82, 13, 10, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 13, 10, 47, 70, 111, 110, 116, 32, 60, 60, 13, 10, 47, 70, 49, 32, 57, 32, 48, 32, 82, 32, 13, 10, 62, 62, 13, 10, 47, 80, 114, 111, 99, 83, 101, 116, 32, 56, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 47, 77, 101, 100, 105, 97, 66, 111, 120, 32, 91, 48, 32, 48, 32, 54, 49, 50, 46, 48, 48, 48, 48, 32, 55, 57, 50, 46, 48, 48, 48, 48, 93, 13, 10, 47, 67, 111, 110, 116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 53, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 32, 47, 76, 101, 110, 103, 116, 104, 32, 49, 48, 55, 52, 32, 62, 62, 13, 10, 115, 116, 114, 101, 97, 109, 13, 10, 50, 32, 74, 13, 10, 66, 84, 13, 10, 48, 32, 48, 32, 48, 32, 114, 103, 13, 10, 47, 70, 49, 32, 48, 48, 50, 55, 32, 84, 102, 13, 10, 53, 55, 46, 51, 55, 53, 48, 32, 55, 50, 50, 46, 50, 56, 48, 48, 32, 84, 100, 13, 10, 40, 32, 65, 32, 83, 105, 109, 112, 108, 101, 32, 80, 68, 70, 32, 70, 105, 108, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 56, 56, 46, 54, 48, 56, 48, 32, 84, 100, 13, 10, 40, 32, 84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 115, 109, 97, 108, 108, 32, 100, 101, 109, 111, 110, 115, 116, 114, 97, 116, 105, 111, 110, 32, 46, 112, 100, 102, 32, 102, 105, 108, 101, 32, 45, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 54, 52, 46, 55, 48, 52, 48, 32, 84, 100, 13, 10, 40, 32, 106, 117, 115, 116, 32, 102, 111, 114, 32, 117, 115, 101, 32, 105, 110, 32, 116, 104, 101, 32, 86, 105, 114, 116, 117, 97, 108, 32, 77, 101, 99, 104, 97, 110, 105, 99, 115, 32, 116, 117, 116, 111, 114, 105, 97, 108, 115, 46, 32, 77, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 53, 50, 46, 55, 53, 50, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 50, 56, 46, 56, 52, 56, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 49, 54, 46, 56, 57, 54, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 66, 111, 114, 105, 110, 103, 44, 32, 122, 122, 122, 122, 122, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 48, 52, 46, 57, 52, 52, 48, 32, 84, 100, 13, 10, 40, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 57, 50, 46, 57, 57, 50, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 54, 57, 46, 48, 56, 56, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 53, 53, 55, 46, 49, 51, 54, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 69, 118, 101, 110, 32, 109, 111, 114, 101, 46, 32, 67, 111, 110, 116, 105, 110, 117, 101, 100, 32, 111, 110, 32, 112, 97, 103, 101, 32, 50, 32, 46, 46, 46, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 101, 110, 100, 115, 116, 114, 101, 97, 109, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 54, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 13, 10, 47, 80, 97, 114, 101, 110, 116, 32, 51, 32, 48, 32, 82, 13, 10, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 13, 10, 47, 70, 111, 110, 116, 32, 60, 60, 13, 10, 47, 70, 49, 32, 57, 32, 48, 32, 82, 32, 13, 10, 62, 62, 13, 10, 47, 80, 114, 111, 99, 83, 101, 116, 32, 56, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 47, 77, 101, 100, 105, 97, 66, 111, 120, 32, 91, 48, 32, 48, 32, 54, 49, 50, 46, 48, 48, 48, 48, 32, 55, 57, 50, 46, 48, 48, 48, 48, 93, 13, 10, 47, 67, 111, 110, 116, 101, 110, 116, 115, 32, 55, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 55, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 32, 47, 76, 101, 110, 103, 116, 104, 32, 54, 55, 54, 32, 62, 62, 13, 10, 115, 116, 114, 101, 97, 109, 13, 10, 50, 32, 74, 13, 10, 66, 84, 13, 10, 48, 32, 48, 32, 48, 32, 114, 103, 13, 10, 47, 70, 49, 32, 48, 48, 50, 55, 32, 84, 102, 13, 10, 53, 55, 46, 51, 55, 53, 48, 32, 55, 50, 50, 46, 50, 56, 48, 48, 32, 84, 100, 13, 10, 40, 32, 83, 105, 109, 112, 108, 101, 32, 80, 68, 70, 32, 70, 105, 108, 101, 32, 50, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 56, 56, 46, 54, 48, 56, 48, 32, 84, 100, 13, 10, 40, 32, 46, 46, 46, 99, 111, 110, 116, 105, 110, 117, 101, 100, 32, 102, 114, 111, 109, 32, 112, 97, 103, 101, 32, 49, 46, 32, 89, 101, 116, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 55, 54, 46, 54, 53, 54, 48, 32, 84, 100, 13, 10, 40, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 54, 52, 46, 55, 48, 52, 48, 32, 84, 100, 13, 10, 40, 32, 116, 101, 120, 116, 46, 32, 79, 104, 44, 32, 104, 111, 119, 32, 98, 111, 114, 105, 110, 103, 32, 116, 121, 112, 105, 110, 103, 32, 116, 104, 105, 115, 32, 115, 116, 117, 102, 102, 46, 32, 66, 117, 116, 32, 110, 111, 116, 32, 97, 115, 32, 98, 111, 114, 105, 110, 103, 32, 97, 115, 32, 119, 97, 116, 99, 104, 105, 110, 103, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 53, 50, 46, 55, 53, 50, 48, 32, 84, 100, 13, 10, 40, 32, 112, 97, 105, 110, 116, 32, 100, 114, 121, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 65, 110, 100, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 66, 84, 13, 10, 47, 70, 49, 32, 48, 48, 49, 48, 32, 84, 102, 13, 10, 54, 57, 46, 50, 53, 48, 48, 32, 54, 52, 48, 46, 56, 48, 48, 48, 32, 84, 100, 13, 10, 40, 32, 66, 111, 114, 105, 110, 103, 46, 32, 32, 77, 111, 114, 101, 44, 32, 97, 32, 108, 105, 116, 116, 108, 101, 32, 109, 111, 114, 101, 32, 116, 101, 120, 116, 46, 32, 84, 104, 101, 32, 101, 110, 100, 44, 32, 97, 110, 100, 32, 106, 117, 115, 116, 32, 97, 115, 32, 119, 101, 108, 108, 46, 32, 41, 32, 84, 106, 13, 10, 69, 84, 13, 10, 101, 110, 100, 115, 116, 114, 101, 97, 109, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 56, 32, 48, 32, 111, 98, 106, 13, 10, 91, 47, 80, 68, 70, 32, 47, 84, 101, 120, 116, 93, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 57, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 84, 121, 112, 101, 32, 47, 70, 111, 110, 116, 13, 10, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 13, 10, 47, 78, 97, 109, 101, 32, 47, 70, 49, 13, 10, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 72, 101, 108, 118, 101, 116, 105, 99, 97, 13, 10, 47, 69, 110, 99, 111, 100, 105, 110, 103, 32, 47, 87, 105, 110, 65, 110, 115, 105, 69, 110, 99, 111, 100, 105, 110, 103, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 49, 48, 32, 48, 32, 111, 98, 106, 13, 10, 60, 60, 13, 10, 47, 67, 114, 101, 97, 116, 111, 114, 32, 40, 82, 97, 118, 101, 32, 92, 40, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 110, 101, 118, 114, 111, 110, 97, 46, 99, 111, 109, 47, 114, 97, 118, 101, 92, 41, 41, 13, 10, 47, 80, 114, 111, 100, 117, 99, 101, 114, 32, 40, 78, 101, 118, 114, 111, 110, 97, 32, 68, 101, 115, 105, 103, 110, 115, 41, 13, 10, 47, 67, 114, 101, 97, 116, 105, 111, 110, 68, 97, 116, 101, 32, 40, 68, 58, 50, 48, 48, 54, 48, 51, 48, 49, 48, 55, 50, 56, 50, 54, 41, 13, 10, 62, 62, 13, 10, 101, 110, 100, 111, 98, 106, 13, 10, 13, 10, 120, 114, 101, 102, 13, 10, 48, 32, 49, 49, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 49, 57, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 48, 57, 51, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 49, 52, 55, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 50, 50, 50, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 48, 51, 57, 48, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 49, 53, 50, 50, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 49, 54, 57, 48, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 52, 50, 51, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 52, 53, 54, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 48, 48, 48, 48, 48, 48, 50, 53, 55, 52, 32, 48, 48, 48, 48, 48, 32, 110, 13, 10, 13, 10, 116, 114, 97, 105, 108, 101, 114, 13, 10, 60, 60, 13, 10, 47, 83, 105, 122, 101, 32, 49, 49, 13, 10, 47, 82, 111, 111, 116, 32, 49, 32, 48, 32, 82, 13, 10, 47, 73, 110, 102, 111, 32, 49, 48, 32, 48, 32, 82, 13, 10, 62, 62, 13, 10, 13, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 13, 10, 50, 55, 49, 52, 13, 10, 37, 37, 69, 79, 70, 13, 10};
//        String encodedFileData = Utility.getByteArrayEncodedString(fileData);
//
//        NotificationRequestDto notificationDto = new NotificationRequestDto();
//        notificationDto.setContent(msgContent);
//
//        // Send Email Request Dto initialization
//        SendFileEmailRequestDto emailRequestDto = new SendFileEmailRequestDto();
//        emailRequestDto.setUserId(inputUserId);
//        emailRequestDto.setTemplateName(inputTemplateName);
//        emailRequestDto.setMobileNo(inputMobileNo);
//        emailRequestDto.setBase64file(encodedFileData);
//        emailRequestDto.setFilename(fileName);
//
//        emailRequestDto.setMail(mailRequestDto);
//        emailRequestDto.setNotification(notificationDto);
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            kafkaMessagingService.sendFileMailWithNotifications(emailRequestDto);
//        });
//        String actualMessage = exception.getMessage();
//        System.out.println(actualMessage);
//        assertTrue(actualMessage.contains(String.format(expectedMessage, inputTemplateName)));
//    }
//}
