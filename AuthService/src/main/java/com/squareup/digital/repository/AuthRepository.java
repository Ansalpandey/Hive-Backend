package com.squareup.digital.repository;

import com.squareup.digital.model.UserModel;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends MongoRepository<UserModel, ObjectId> {

  Optional<UserModel> findByUsername(String username);

  Optional<UserModel> findByEmail(String email);

  boolean existsByUsername(String username);
}
