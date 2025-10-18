package com.rishipadala.journalApp.Service;



import com.rishipadala.journalApp.Entity.JournalEntry;
import com.rishipadala.journalApp.Entity.User;

import com.rishipadala.journalApp.Repository.JournalEntryRepo;
import com.rishipadala.journalApp.Repository.UserRepo;
import com.rishipadala.journalApp.dto.UserUpdateforOAuth2_DTO;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        /**
         * Finds a user by their email. If the user doesn't exist, it creates a new one
         * with a random password, as they are authenticating via Google.
         *
         * @param email The email of the user from their Google profile.
         * @return The existing or newly created User entity.
         */
        public User findOrCreateGoogleUser(String email) {
            User user = userRepo.findByuserName(email);

            if (user == null) {
                log.info("Creating new user for Google login with email: {}", email);
                user = new User();
                user.setUserName(email);
                user.setEmail(email);
                // For OAuth2 users, we don't have a password. Set a random, secure one.
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setRoles(Arrays.asList("USER"));
                userRepo.save(user);
            }
            return user;
        }


    public User updateUserSettings(String userName, UserUpdateforOAuth2_DTO userUpdateDTO) {
        User userInDb = findByuserName(userName);
        if (userInDb != null) {
            // Only update the field if it was provided in the request
            if (userUpdateDTO.getSentimentAnalysis() != null) {
                userInDb.setSentimentAnalysis(userUpdateDTO.getSentimentAnalysis());
            }
            // You would add other updatable fields here, e.g., userInDb.setDisplayName(...)

            saveUser(userInDb); // Use saveUser to avoid re-encoding the password
            return userInDb;
        }
        return null; // Or throw a UserNotFoundException
    }

    /**
     * Deletes a user by their username and also deletes all associated journal entries.
     * This operation is transactional to ensure atomicity.
     *
     * @param userName The username (email) of the user to delete.
     * @return true if the user was found and deleted, false otherwise.
     */
    @Transactional // Ensures atomicity: if one-step fails, all steps are rolled back
    public boolean deleteUserAndAssociatedEntries(String userName) {
        User user = userRepo.findByuserName(userName);
        if (user != null) {
            // 1. Delete associated Journal Entries
            // This assumes your User entity has a 'journalEntries' list
            if (user.getJournalEntries() != null && !user.getJournalEntries().isEmpty()) {
                journalEntryRepo.deleteAllById(user.getJournalEntries().stream().map(JournalEntry::getId).toList());
            }

            // 2. Delete the User
            userRepo.delete(user);
            log.info("User {} and their journal entries deleted successfully.", userName);
            return true;
        } else {
            log.warn("User {} not found for deletion.", userName);
            return false;
        }
    }

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
