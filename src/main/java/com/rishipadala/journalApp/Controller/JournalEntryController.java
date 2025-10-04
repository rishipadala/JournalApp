package com.rishipadala.journalApp.Controller;

import com.rishipadala.journalApp.Entity.JournalEntry;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Service.JournalEntryService;

import com.rishipadala.journalApp.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal Entry APIs", description = "APIs for Managing User Journal Entries - Read, Update, Create & Delete Entries!")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService; //This annotation is used to inject the journalEntryRepository bean (from going through service class) into the controller class.

    @Autowired
    private UserService userService;

    @GetMapping()
    @Operation(summary = "Get all journal entries for the current user")
    public ResponseEntity<?> getAllJournalEntriesofUser(){ // '?' Wild card pattern - With help of this Type we can return any type of class instead JournalEntry we can also return User.class
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder holds the security context (including authentication and user details)
        String userName = authentication.getName();
        User user = userService.findByuserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatusCode.valueOf(200)); //HttpStatus.OK
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    @Operation(summary = "Create a new journal entry for the Authenticated User")
    //@RequestBODY = It allows you to directly map the content of the HTTP request body (often in formats like JSON or XML) to a specified Java object.
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry ){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder holds the security context (including authentication and user details)
            String userName = authentication.getName();
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    //@PathVariable annotation is used to bind /delete/{myid} to the method parameter myid.
    @GetMapping("id/{myid}")
    @Operation(summary = "Get a specific journal entry by its ID")
    public ResponseEntity<?> getJournalEntryByID(@PathVariable ObjectId myid){
        //It says that find the entry by id , if there is no "/{myid}" then return null.

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder holds the security context (including authentication and user details)
        String userName = authentication.getName();

        User user = userService.findByuserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> EntrybyID = journalEntryService.findByID(myid);
            if (EntrybyID.isPresent()){
                return new ResponseEntity<>(EntrybyID.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myid}")
    @Operation(summary = "Delete a specific journal entry by its ID")
    public ResponseEntity<?> deleteEntrybyID(@PathVariable ObjectId myid ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder holds the security context (including authentication and user details)
        String userName = authentication.getName();
        boolean removed =  journalEntryService.deleteBYId(myid,userName);
        if (removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{myid}")
    @Operation(summary = "Update an existing journal entry by its ID")
    public ResponseEntity<?> updateEntrybyID(@PathVariable ObjectId myid ,
                                             @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder holds the security context (including authentication and user details)
        String userName = authentication.getName();
        User user = userService.findByuserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());


        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findByID(myid);
            if (journalEntry.isPresent()){

                JournalEntry oldEntry = journalEntry.get();

                //title set = if new entry/updating entry in PUT Method is not equal to != NULL && new entry is not equal to (!=) EMPTY " " Then set newEntry ka TITLE
                //If condition false , Leave the old entry's TITLE as it is...(set old entry ka TITLE).
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());

                //SAME LOGIC APPLIED ON SETTING CONTENT in New entry...
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry,HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
