package com.notification.controller;

import com.notification.dto.SmsContentDto;
import com.notification.service.impl.CogSmsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v5/message-notification")
public class MessageNotificationController {

    private final CogSmsServiceImpl cogSmsService;

    @GetMapping(value = "/view-message-content", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsContentDto> getMessageContent(@RequestParam(name = "templateName") String templateName) {
        return ResponseEntity.ok(cogSmsService.getMessageContent(templateName));
    }

    @PostMapping("/update-message-content")
    public ResponseEntity<String> updateMessageContent(@RequestBody SmsContentDto smsDto) {
        //, Authentication authentication) {
//        return ResponseEntity.ok(cogSmsService.updateSmsContent(smsDto, "SYSTEM")); // TODO - Uncomment after enable security
        return ResponseEntity.ok(cogSmsService.updateMessageContent(smsDto, "SYSTEM")); // TODO - Remove after enable security
    }
}
