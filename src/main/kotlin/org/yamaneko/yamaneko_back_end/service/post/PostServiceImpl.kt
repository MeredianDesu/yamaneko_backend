package org.yamaneko.yamaneko_back_end.service.post

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.post.PostDTO
import org.yamaneko.yamaneko_back_end.entity.Post
import org.yamaneko.yamaneko_back_end.mappers.PostMapper
import org.yamaneko.yamaneko_back_end.repository.PostRepository
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import java.util.*

@Service
class PostServiceImpl(
  private val postRepository: PostRepository, private val userRepository: UserRepository
): PostService {
  
  private val logger = LoggerFactory.getLogger(PostServiceImpl::class.java)
  
  private val mapper = PostMapper()
  
  override fun getPosts(userId: Long): List<PostDTO>? {
    val posts = postRepository.findByUserId(userId)
    
    if(posts.isNullOrEmpty()) {
      return null
    }
    
    val response = posts.map { mapper.toDTO(it) }
    
    return response
  }
  
  override fun createPost(username: String, postText: String): Boolean {
    val dateFormatter = DateFormatter()
    val user = userRepository.findByUsername(username) ?: return false
    
    val post = Post().apply {
      text = postText
      createdAt = dateFormatter.dateToString(Date())
      this.user = user
    }
    
    postRepository.save(post)
    
    return true
  }
  
  override fun deletePost(postId: Long, userId: Long): Boolean {
    val post =
      postRepository.findById(postId)
        .orElse(null) ?: return false
    
    logger.info("POST: User ID: $userId, Post user ID: ${post.user?.id}")
    if(post.user?.id != userId) {
      return false
    }
    
    postRepository.deleteById(postId)
    
    return true
  }
  
}