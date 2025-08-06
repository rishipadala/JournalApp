package com.rishipadala.journalApp.Scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTests {

    @Autowired
    private UserScheduler userScheduler;

    @Disabled
    @Test
    public void fetchUsersAndSendSentiMailTEST(){
        userScheduler.fetchUsersAndSendSentiMail();
    }
}
