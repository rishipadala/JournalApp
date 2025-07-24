package com.rishipadala.journalApp.Service;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
public class UserServiceTests {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;



    @ParameterizedTest
    @Disabled
    @ArgumentsSource(UserArgumentsProvider.class) // 2 Tests
    public void testFindbyUserName(User user){
        assertTrue(userService.saveNewUser(user));
    }


    @Disabled
    @ParameterizedTest
    @CsvSource({
            "Mikey",
            "Draken",
            "Izana"
    })
    public void testFindbyUserName(String name){
         assertNotNull(userRepo.findByuserName(name));   //userRepository will be injected properly here by @SpringBootTest annotation
    }



    @Disabled //To exclude them from runs without deletion.
    @ParameterizedTest //allowing a single test method to run multiple times with varying inputs
    // for multiple parameters:
    @CsvSource({
            "1,2,3",
            "2,2,4",
            "3,3,9"
    })
    public void addtests(int a,int b,int expected){
        assertEquals(expected,a+b);
    }


}
