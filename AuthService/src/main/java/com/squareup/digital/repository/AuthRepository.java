package com.squareup.digital.repository;

import com.squareup.digital.model.UserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<UserModel, ObjectId> {

    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

    boolean existsByUsername(String username); // ✅ optimized check
}
