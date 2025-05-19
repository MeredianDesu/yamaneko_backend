package org.yamaneko.yamaneko_back_end.service.post

import org.yamaneko.yamaneko_back_end.dto.post.PostDTO

interface PostService {
  
  fun getPosts(userId: Long): List<PostDTO>?
  fun createPost(username: String, postText: String): Boolean
  fun deletePost(postId: Long, userId: Long): Boolean
}