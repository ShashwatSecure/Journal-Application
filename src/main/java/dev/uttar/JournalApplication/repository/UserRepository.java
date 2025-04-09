package dev.uttar.JournalApplication.repository;

import dev.uttar.JournalApplication.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
   User findByUsername(String userName);
}
