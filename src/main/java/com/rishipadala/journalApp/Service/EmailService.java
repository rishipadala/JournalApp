package com.rishipadala.journalApp.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.sender.email}")
    private String senderEmail;

    public void sendMail(String to, String Subject, String body){
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setFrom(senderEmail);
            mail.setSubject(Subject);
            mail.setText(body);
            javaMailSender.send(mail);
        }
        catch (Exception e){
            log.error("Error while Sending Email: ", e);
        }
    }
}
