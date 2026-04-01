package com.squareup.digital

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserModel, ObjectId> {
    fun findByUsername(username: String): UserModel?
            fun findByEmail(email: String): UserModel?
}
