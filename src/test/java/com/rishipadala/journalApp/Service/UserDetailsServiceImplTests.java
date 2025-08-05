package com.rishipadala.journalApp.Service;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@Disabled
public class UserDetailsServiceImplTests {

    @InjectMocks //we don't want whole instance of actual class,therefore we Inject mocks init
    private UserDetailsServiceImpl userDetailsService;

    //@Mock //This runs only with @SpringBootTest annotation on main class which leads to start Application Context path too
    //we dont want that therefore , we Use @MockitoBean also
    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void SetUp(){
        MockitoAnnotations.initMocks(this); //Here we are saying that this our class & Initialize the all Mocks present in this class.
    }

    @Disabled
    @Test
    void loadUserByUsernameTests(){
        when(userRepo.findByuserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("Ram").password("wqeq").roles(new ArrayList<>()).build());
        UserDetails user = userDetailsService.loadUserByUsername("Ram");
        Assertions.assertNotNull(user);
    }
}
