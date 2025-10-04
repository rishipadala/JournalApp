package com.rishipadala.journalApp.Controller;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "ADMIN APIs", description = "Admins can View the other Users details & entries with help of these APIs")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    @Operation(summary = "Get a list of all users in the system (Admin only)")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAll();
        if ( all!= null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "Create a new user with ADMIN privileges (Admin only)")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        userService.saveAdmin(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
