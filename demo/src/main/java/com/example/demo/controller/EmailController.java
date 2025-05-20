package com.example.demo.controller;

import com.example.demo.service.EmailService;
import com.example.demo.service.SubscribersService;
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
    public String sendSimppleEmail(){
//        this.emailService.sendSimpleEmail();
//        this.emailService.sendEmailSync("votranquocbao2806@gmail.com","Hello bao","<h1> <b>Hello </b></h1>",false,true);
//        this.emailService.sendEmailFromTemplateSync("votranquocbao2806@gmail.com","Hello bao","job");
        this.subscribersService.sendSubscribersEmailJobs();
        return "oke";
    }
}
