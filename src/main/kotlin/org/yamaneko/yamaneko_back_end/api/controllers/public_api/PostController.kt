package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.post.PostRequestDTO
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.service.post.PostServiceImpl

@Tag(name = "{ v1 } Posts API")
@RestController
@RequestMapping("api/post/v1")
class PostController(
  private val postService: PostServiceImpl,
  private val userRepository: UserRepository,
) {
  
  val logger: Logger = LoggerFactory.getLogger(this::class.java)
  
  @GetMapping("{id}")
  fun getPosts(@PathVariable id: Long): ResponseEntity<Any> {
    val posts = postService.getPosts(id)
    
    return if(posts.isNullOrEmpty()) {
      ResponseEntity.notFound()
        .build()
    } else {
      ResponseEntity.ok()
        .body(posts)
    }
  }
  
  @PostMapping
  fun createPost(@RequestBody post: PostRequestDTO, authentication: Authentication): ResponseEntity<Any> {
    val userDetails = authentication.principal as UserDetails
    logger.info("Loaded user: ${userDetails.username}")
    
    val isPostCreated = postService.createPost(userDetails.username, post.text)
    
    return if(isPostCreated) {
      ResponseEntity.ok()
        .build()
    } else {
      ResponseEntity.status(HttpStatus.CONFLICT)
        .build()
    }
  }
  
  @DeleteMapping
  fun deletePost(@RequestParam postId: Long, authentication: Authentication): ResponseEntity<Any> {
    val userDetails = authentication.principal as UserDetails
    val user =
      userRepository.findByUsername(userDetails.username) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .build()
    
    logger.info(
      "PostID: $postId, UserID: ${user.id}"
    )
    val result = postService.deletePost(postId, user.id)
    
    return if(result) {
      ResponseEntity.ok()
        .build()
    } else {
      ResponseEntity.status(HttpStatus.FORBIDDEN)
        .build()
    }
  }
  
}