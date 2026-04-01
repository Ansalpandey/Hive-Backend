package com.squareup.digital.repository

import com.squareup.digital.models.PostModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<PostModel, String> {

}
