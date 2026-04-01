package com.squareup.digital.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.annotation.TypeAlias
import java.time.Instant

@Entity(name = "posts")
@TypeAlias("post")
data class PostModel(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: String? = null,
    val content: String,
    val author: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val likes: Int = 0,
    val commentsCount: Int = 0,
    val repostsCount: Int = 0,
)
