package com.rishipadala.journalApp.Controller;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Repository.UserRepo;
import com.rishipadala.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;


    @PutMapping
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

    @DeleteMapping
    public ResponseEntity<?> deleteCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepo.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
