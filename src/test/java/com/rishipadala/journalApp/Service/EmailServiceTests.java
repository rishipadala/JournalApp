package com.rishipadala.journalApp.Service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;



    @Disabled
    @Test
    public void TestMail(){
        emailService.sendMail("draken@yopmail.com",
                "Testing SMTP Java Mail Sender 2",
                "NANACO MONACO!");
    }
}
