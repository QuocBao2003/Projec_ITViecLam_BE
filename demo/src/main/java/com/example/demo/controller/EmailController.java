package com.example.demo.controller;

import com.example.demo.service.EmailService;
import com.example.demo.service.SubscribersService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscribersService subscribersService;
    public EmailController(EmailService emailService, SubscribersService subscribersService) {
        this.emailService = emailService;
        this.subscribersService = subscribersService;
    }

    @GetMapping("/email")
//    @Scheduled(cron = "0 */1 * * * *")
    public ResponseEntity<String> sendSimppleEmail(){
        this.subscribersService.sendSubscribersEmailJobs();
        return ResponseEntity.ok().body("Email sent successfully");
    }
}
