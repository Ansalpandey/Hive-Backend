package com.squareup.digital.service

import com.squareup.digital.dto.CreatePostDto
import com.squareup.digital.models.PostModel
import com.squareup.digital.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    val postRepository: PostRepository
) {
    fun createPost(postDto: CreatePostDto) : String {
        val post = PostModel(
            content = postDto.content,
            author = postDto.author,
        )
        postRepository.save(post)
        return "Post created successfully with:- " + post.id.toString()
    }
}
