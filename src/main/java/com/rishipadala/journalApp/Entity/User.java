package com.rishipadala.journalApp.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true) //Username should be unique
    @NonNull
    private String userName;

    @NonNull
    private String password;

    @DBRef //The user document stores only references (ObjectIds) to journal entry documents rather than embedding full entries.
    private List<JournalEntry> journalEntries = new ArrayList<>();

    private List<String> roles;
}
