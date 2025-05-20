package com.example.demo.service;

import  com.example.demo.domain.Job;
import com.example.demo.repository.JobRepository;
import com.example.demo.util.SecurityUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailService {
    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;

    }

    public void sendSimpleEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("votranquocbao2806@gmail.com");
        message.setSubject("Test email");
        message.setText("Hello world from Spring Boot!");
        this.mailSender.send(message);
    }
//gửi email dồng bộ
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml){
//        Prepare message ussing a Spring helper
//        hỗ trợ nội dung nâng cao html,file đính kèm
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try{
//            thiết lập nội dung email
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,isMultipart, StandardCharsets.UTF_8.name());
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content,isHtml);
            this.javaMailSender.send(mimeMessage);
        }catch (MailException | MessagingException e){
            System.out.println("Error sending email" +e.getMessage());
        }

    }
    @Async
    public void sendEmailFromTemplateSync(String to,String subject,String templateName,String username,Object value){
        Context context = new Context();
        context.setVariable("name",username);
        context.setVariable("Jobs",value);
        String content = templateEngine.process(templateName,context);
        this.sendEmailSync(to,subject,content,false,true);

    }
}
