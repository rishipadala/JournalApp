package com.rishipadala.journalApp.Entity;

import com.rishipadala.journalApp.enums.Sentiment;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal-entries")
@Data //This annotation Includes @Getters,@Setters,@NoargsConstructor,@AllArgsConstructors,@EqualsHashCode (At the Time of Compiling it writes the code for us).
public class JournalEntry {

    @Id
    public ObjectId id;

    public String title;

    public LocalDateTime dateTime; //FOR DATE & TIME

    public String content;

    private Sentiment sentiment;


}
