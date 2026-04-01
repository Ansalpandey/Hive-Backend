package com.squareup.digital.controller

import com.squareup.digital.dto.CreatePostDto
import com.squareup.digital.service.PostService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(private val postService: PostService) {
  @PostMapping
  fun createPost(@RequestBody post: CreatePostDto): String {
    return postService.createPost(post)
  }
}
