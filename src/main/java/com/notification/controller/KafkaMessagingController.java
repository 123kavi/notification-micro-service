package com.notification.controller;

import com.notification.dto.request.SendEmailRequestDto;
import com.notification.dto.request.SendFileEmailRequestDto;
import com.notification.service.KafkaMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v5/kafka-mail-sender")
public class KafkaMessagingController {

    private final KafkaMessagingService messagingService;

    @PostMapping("/send-html-mail")
    public ResponseEntity<String> sendHtmlMail(@RequestBody SendEmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(messagingService.sendHtmlMailWithNotifications(emailRequestDto));
    }

    @PostMapping("/send-file-mail")
    public ResponseEntity<String> sendFileMail(@RequestBody SendFileEmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(messagingService.sendFileMailWithNotifications(emailRequestDto));
    }
}
