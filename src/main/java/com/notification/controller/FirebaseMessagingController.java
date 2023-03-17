package com.notification.controller;

import com.notification.dto.NotificationDto;
import com.notification.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v5/firebase-messaging")
public class FirebaseMessagingController {
    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping(value = "/save-notification")
    public ResponseEntity<String> saveOrUpdateNotification(@RequestBody NotificationDto notificationDto) {
//                                                    , Authentication authentication) {
//        firebaseMessagingService.saveFirebaseNotificationDetails(notificationDto, authentication.getName()); // TODO - Uncomment after enable security
        firebaseMessagingService.saveOrUpdateFirebaseNotificationDetails(notificationDto, "SYSTEM"); // TODO - Remove after enable security
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/update-fcm-token")
    public ResponseEntity<String> updateFcmRefreshToken(@RequestParam("userId") long userId,
                                                        @RequestParam("oldToken") String oldToken,
                                                        @RequestParam("newToken") String newToken) {
//                                                        , Authentication authentication) {
//        firebaseMessagingService.updateFcmRefreshToken(userId, oldToken, newToken, authentication.getName()); // TODO - Uncomment after enable security
        firebaseMessagingService.updateFcmRefreshToken(userId, oldToken, newToken, "SYSTEM"); // // TODO - Remove after enable security
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/verify-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> verifyToken(@RequestParam("userId") long userId,
                                               @RequestParam("fcmToken") String token) {
        return ResponseEntity.ok(firebaseMessagingService.verifyTokenValidity(userId, token));
    }
}
