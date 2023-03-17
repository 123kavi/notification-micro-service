package com.notification.controller;

import com.notification.dto.EmailDto;
import com.notification.dto.ViewEmailContentDto;
import com.notification.service.CogEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v5/manage-mail")
public class ManageMailController {

    private final CogEmailService emailService;

    @GetMapping(value = "/view-email-content", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViewEmailContentDto> getEmailContent(@RequestParam(name = "templateName") String templateName) {
        return ResponseEntity.ok(emailService.getEmailContent(templateName));
    }

    @PostMapping("/update-email-content")
    public ResponseEntity<String> updateEmailContent(@RequestBody EmailDto emailDto) {
                                                    //, Authentication authentication) {
//        return ResponseEntity.ok(emailService.updateEmailContent(emailDto, authentication.getName())); // TODO - Uncomment after enable security
        return ResponseEntity.ok(emailService.updateEmailContent(emailDto, "SYSTEM")); // TODO - Remove after enable security
    }
}
