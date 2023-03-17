//package com.cloudofgoods.notification.service;
//
//import com.cloudofgoods.notification.dto.NotificationDto;
//import com.cloudofgoods.notification.dto.request.NotificationRequestDto;
//import com.cloudofgoods.notification.exception.SystemWarningException;
//import com.cloudofgoods.notification.util.Utility;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class FirebaseMessagingServiceImplTest {
//
//    @Autowired
//    private FirebaseMessagingService firebaseMessagingService;
//
//    @Test
//    void isSaveOrUpdateFirebaseNotificationDetailsSuccessfully() {
//        long userId = 100;
//        String platform = "WEB"; // WEB, MOBILE
//        String token = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//        String userName = "SYSTEM";
//
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setUserId(userId);
//        notificationDto.setPlatform(platform);
//        notificationDto.setToken(token);
//
//        assertDoesNotThrow(() -> firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, userName));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"," + Utility.EX_ALL_FIELDS_REQUIRED})
//    void isSaveOrUpdateThrowsAllFieldsRequiredExceptionForNotificationDtoNull(NotificationDto notificationDto,
//                                                                              String expectedMessage) {
//        String userName = "SYSTEM";
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, userName);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"," + Utility.EX_FIREBASE_CLIENT_TOKEN_REQUIRED})
//    void isSaveOrUpdateThrowsFirebaseClientTokenRequiredExceptionForTokenNull(String token,
//                                                                              String expectedMessage) {
//        long userId = 100;
//        String platform = "WEB"; // WEB, MOBILE
//        String userName = "SYSTEM";
//
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setUserId(userId);
//        notificationDto.setPlatform(platform);
//        notificationDto.setToken(token);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, userName);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({" ," + Utility.EX_FIREBASE_CLIENT_TOKEN_REQUIRED})
//    void isSaveOrUpdateThrowsFirebaseClientTokenRequiredExceptionForTokenEmpty(String token,
//                                                                               String expectedMessage) {
//        long userId = 100;
//        String platform = "WEB"; // WEB, MOBILE
//        String userName = "SYSTEM";
//
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setUserId(userId);
//        notificationDto.setPlatform(platform);
//        notificationDto.setToken(token);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, userName);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"," + Utility.EX_DEVICE_PLATFORM_REQUIRED})
//    void isSaveOrUpdateThrowsDevicePlatformRequiredExceptionForPlatformNull(String platform,
//                                                                            String expectedMessage) {
//        long userId = 100;
//        String token = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//        String userName = "SYSTEM";
//
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setUserId(userId);
//        notificationDto.setPlatform(platform);
//        notificationDto.setToken(token);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, userName);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({" ," + Utility.EX_DEVICE_PLATFORM_REQUIRED})
//    void isSaveOrUpdateThrowsDevicePlatformRequiredExceptionForPlatformEmpty(String platform,
//                                                                             String expectedMessage) {
//        long userId = 100;
//        String token = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//        String userName = "SYSTEM";
//
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setUserId(userId);
//        notificationDto.setPlatform(platform);
//        notificationDto.setToken(token);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, userName);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"," + Utility.EX_NOTIFICATION_DETAILS_REQUIRED})
//    void isNotifyAllChannelsThrowsNotificationDetailsRequiredExceptionForNullNotificationDto(NotificationRequestDto notifyDto,
//                                                                                             String expectedMessage) {
//        long userId = 100;
//        String templateName = "order_confirmation_email_to_customer";
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.notifyAllChannels(userId, templateName, notifyDto);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"," + Utility.EX_TEMPLATE_NAME_REQUIRED})
//    void isNotifyAllChannelsThrowsTemplateNameRequiredExceptionForTemplateNameNull(String templateName,
//                                                                                   String expectedMessage) {
//        long userId = 100;
//        Map<String, Object> contents = new HashMap<>();
//        contents.put("<user-name>", "Saman");
//        contents.put("<vendor-name>", "Kamal");
//
//        NotificationRequestDto notifyDto = new NotificationRequestDto();
//        notifyDto.setContent(contents);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.notifyAllChannels(userId, templateName, notifyDto);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({" ," + Utility.EX_TEMPLATE_NAME_REQUIRED})
//    void isNotifyAllChannelsThrowsTemplateNameRequiredExceptionForTemplateNameEmpty(String templateName,
//                                                                                    String expectedMessage) {
//        long userId = 100;
//        Map<String, Object> contents = new HashMap<>();
//        contents.put("<user-name>", "Saman");
//        contents.put("<vendor-name>", "Kamal");
//
//        NotificationRequestDto notifyDto = new NotificationRequestDto();
//        notifyDto.setContent(contents);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.notifyAllChannels(userId, templateName, notifyDto);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"notExistingTemplateName," + Utility.EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME})
//    void isNotifyAllChannelsThrowsNoResultsFoundForTemplateNameException(String templateName,
//                                                                         String expectedMessage) {
//        long userId = 100;
//        Map<String, Object> contents = new HashMap<>();
//        contents.put("<user-name>", "Saman");
//        contents.put("<vendor-name>", "Kamal");
//
//        NotificationRequestDto notifyDto = new NotificationRequestDto();
//        notifyDto.setContent(contents);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.notifyAllChannels(userId, templateName, notifyDto);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(String.format(expectedMessage, templateName)));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"order_confirmation_email_to_customer," + Utility.EX_FEATURE_FLAG_STATUS_DISABLED})
//        // Please disable the status from db related to this feature flag
//    void isNotifyAllChannelsThrowsFeatureFlagStatusDisabledExceptionWhenStatusDisabled(String templateName,
//                                                                                       String expectedMessage) {
//        long userId = 100;
//        Map<String, Object> contents = new HashMap<>();
//        contents.put("<user-name>", "Saman");
//        contents.put("<vendor-name>", "Kamal");
//
//        NotificationRequestDto notifyDto = new NotificationRequestDto();
//        notifyDto.setContent(contents);
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.notifyAllChannels(userId, templateName, notifyDto);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(String.format(expectedMessage, templateName)));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"order_confirmation_email_to_customer," + Utility.EX_FEATURE_FLAG_STATUS_DISABLED})
//        // Please again enable the status from db related to this feature flag
//    void isAllChannelsNotifiesSuccessfullyForValidData(String templateName,
//                                                       String expectedMessage) {
//        long userId = 100;
//        Map<String, Object> contents = new HashMap<>();
//        contents.put("<user-name>", "Saman");
//        contents.put("<vendor-name>", "Kamal");
//
//        NotificationRequestDto notifyDto = new NotificationRequestDto();
//        notifyDto.setContent(contents);
//
//        assertDoesNotThrow(() -> firebaseMessagingService.notifyAllChannels(userId, templateName, notifyDto));
//    }
//
//    @Test
//    void isFcmRefreshTokenUpdatedSuccessfullyForValidData() {
//        long userId = 100;
//        String oldToken = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//        String newToken = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//        String userName = "SYSTEM";
//
//        assertDoesNotThrow(() -> firebaseMessagingService.updateFcmRefreshToken(userId, oldToken, newToken, userName));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84," + Utility.EX_FIREBASE_TOKEN_NOT_FOUND})
//        // Input wrong FCM token
//    void isUpdateFcmRefreshTokenThrowsFirebaseTokenNotFoundExceptionForIncorrectOldFcmToken(String oldToken,
//                                                                                            String expectedMessage) {
//        long userId = 100;
//        String newToken = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//        String userName = "SYSTEM";
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.updateFcmRefreshToken(userId, oldToken, newToken, userName);
//        });
//
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    void isVerifyTokenValiditySuccessfulForValidData() {
//        long userId = 100;
//        String oldToken = "cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84mjBucRBXdt3V6PfIr4N-mqHaSAqLfYmDaoiSPb-uPNTt1zAIT2YdobBfSkoVhE6ym2nILgFQrUIlgehAcbwoPTYHUaJnO";
//
//        assertDoesNotThrow(() -> firebaseMessagingService.verifyTokenValidity(userId, oldToken));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"cGFYXltIzZMp7Cphq7ZJ15:APA91bFM5knZ3uhoO9wEKAFNSBT0Wo4DbGM_9K84lNxrz84," + Utility.EX_FIREBASE_TOKEN_NOT_FOUND})
//        // Input wrong FCM token
//    void isVerifyTokenValidityFailsForIncorrectOldFcmToken(String oldToken,
//                                                           String expectedMessage) {
//        long userId = 100;
//
//        Exception exception = assertThrows(SystemWarningException.class, () -> {
//            firebaseMessagingService.verifyTokenValidity(userId, oldToken);
//        });
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//}
