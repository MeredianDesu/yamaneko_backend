package org.yamaneko.yamaneko_back_end.dto.wishlist

import jakarta.persistence.Embeddable

@Embeddable
data class WishListId(
  val userId: Long = 0,
  val releaseId: Long = 0,
)
