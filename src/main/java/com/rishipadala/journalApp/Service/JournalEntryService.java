package com.rishipadala.journalApp.Service;


import com.rishipadala.journalApp.Entity.JournalEntry;
import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Repository.JournalEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    //This method is for GET - to get all entries
    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    //This method is for POST - To save the entry
    @Transactional
    public void saveEntry(JournalEntry journalEntry,  String userName){
        try {
            User user = userService.findByuserName(userName); //we Found the user with his UserNAme
            journalEntry.setDateTime(LocalDateTime.now()); //Java 8 feature - LocalDateTime.now() can set date & time on its own as per the current date & time.
            JournalEntry saved = journalEntryRepo.save(journalEntry); //we saved journal entry
            user.getJournalEntries().add(saved);  //Particular user -->journal Entries of his--> added with saved object
            //If we have Interruption then the jornalEntries cannot be saved in User
            userService.saveUser(user);

            //user.setUserName(null); //inconsistency problem--> This can be resolved by Transactional Ek bhi interrupt then whole method rollback!

        } catch (Exception e) {
            log.error("Error Occured : ", e);
            throw new RuntimeException("An error Occurred while saving the entry : ", e);
        }
    }

    //This method is for PUT - To save the entry
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }



    //This method is for GET for finding the entry by its OBjectID
    public Optional<JournalEntry> findByID(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    //This method is for DELETE - To delete the entry BY its ObjectID
    @Transactional
    public boolean deleteBYId(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByuserName(userName); //we Found the user with his UserNAme
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id)); //User ke isme se bhi journal --> journal entry remove if --> the id is equals.
            if (removed) {
                userService.saveUser(user);
                journalEntryRepo.deleteById(id); //journal entry se delete
            }
        } catch (Exception e) {
            log.error("Error Occured : ",e);
            throw new RuntimeException("An Error occurred while deleting the entry ", e);
        }
        return removed;
    }













}
