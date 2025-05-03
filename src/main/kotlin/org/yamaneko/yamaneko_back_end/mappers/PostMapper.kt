package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.post.PostDTO
import org.yamaneko.yamaneko_back_end.entity.Post

class PostMapper {
  
  fun toDTO(post: Post): PostDTO {
    return PostDTO(
      id = post.id, text = post.text, createdAt = post.createdAt, userId = post.user?.id
    )
  }
}