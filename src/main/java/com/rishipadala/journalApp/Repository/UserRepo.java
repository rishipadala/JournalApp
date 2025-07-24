package com.rishipadala.journalApp.Repository;

import com.rishipadala.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, ObjectId> {
    User findByuserName(String userName);

    void deleteByUserName(String name);
}
