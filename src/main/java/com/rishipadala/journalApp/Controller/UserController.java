package com.rishipadala.journalApp.Controller;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Repository.UserRepo;
import com.rishipadala.journalApp.Service.UserService;
import com.rishipadala.journalApp.Service.WeatherService;
import com.rishipadala.journalApp.api.response.WeatherResponse;
import com.rishipadala.journalApp.dto.UserUpdateforOAuth2_DTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@Tag(name = "USER APIs", description = "Read , Update & Delete User")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WeatherService weatherService;


    @PutMapping
    @Operation(summary = "Update the authenticated user's details")
    public ResponseEntity<?> updatebyuserName(@RequestBody User user ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder holds the security context (including authentication and user details)
        String userName = authentication.getName();
        User userInDB = userService.findByuserName(userName);
        if(userInDB != null){
            userInDB.setUserName(user.getUserName());
            userInDB.setPassword(user.getPassword());
            userService.saveNewUser(userInDB);
            return new ResponseEntity<>(userInDB,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping
    @Operation(summary = "Get a Greeting message and current weather for the authenticated user")
    public ResponseEntity<?> greetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greetings = "";
        if (weatherResponse!=null && weatherResponse.getCurrent() != null){
            WeatherResponse.Current current = weatherResponse.getCurrent(); //Making the object of class Current from WeatherResponse POJO
            greetings = ",Today's Weather is : "+
                    "Temperature : " + current.getTemperature() +"°C, \n" +
                    "Air Quality Feels Like:" + current.getFeelsLike() + "\n" +
                    "Description: " + String.join(",",current.getWeatherDescriptions());

            //Takes the list of weather descriptions from current.getWeatherDescriptions()
            // and joins them into a single string, separated by a comma and a space.
            // For example, if the list is ["Sunny", "Clear"]
        }
        return new ResponseEntity<>("HI! " + authentication.getName() + greetings ,HttpStatus.OK);
    }

    @PutMapping("/settings")
    @Operation(summary = "Update settings for the authenticated user")
    public ResponseEntity<?> updateUserSettings(@RequestBody UserUpdateforOAuth2_DTO userUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName(); // This is the user's email for an OAuth2 user

        User updatedUser = userService.updateUserSettings(userName, userUpdateDTO);

        if (updatedUser != null) {
            // You might want to return a UserDTO here instead of the full User entity to hide the password
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    @Operation(summary = "Delete the authenticated user's account and all associated journal entries")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Use annotation for cleaner code
    public void deleteCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean deleted = userService.deleteUserAndAssociatedEntries(userName);
        if (!deleted) {
            // Optional: Log or handle the case where the user wasn't found
            log.warn("Attempted to delete user {}, but user was not found.", userName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
