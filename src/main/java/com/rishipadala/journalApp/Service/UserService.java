package com.rishipadala.journalApp.Service;



import com.rishipadala.journalApp.Entity.User;

import com.rishipadala.journalApp.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //This method is for GET - to get all entries
    public List<User> getAll(){
        return userRepo.findAll();
    }

    //This method is for POST - To save the entry
    public boolean saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            log.error("Error occurred at {} ",user.getUserName() ,e);
            log.warn("WARNING,This is not The Right Way to do it");
            log.info("The Info about that you are doing it all wrong");
            log.debug("DEBUGGING THE BUGS!");
            log.trace("ITS NOTHING!!!!!!!");
            return false;
        }
    }

    //We don't want to encode the password again for JournalEntryController
    //Therefore, We will save only save user details from UserRepo
    public void saveUser(User user){
        userRepo.save(user);
    }

    //For creating Admin
    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepo.save(user);
    }

    //This method is for GET for finding the entry by its OBjectID
    public Optional<User> findByID(ObjectId id){
        return userRepo.findById(id);
    }

    //This method is for DELETE - To delete the USER BY its ObjectID
    public void deleteBYId(ObjectId id){
        userRepo.deleteById(id);
    }

    public User findByuserName(String userName){
        return userRepo.findByuserName(userName);
    }




}
