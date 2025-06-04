package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.yamaneko.yamaneko_back_end.dto.wishlist.WishListId

@Entity
@Table(name = "wishlist")
class WishList {
  
  @EmbeddedId
  var id: WishListId = WishListId()
  
  var addedAt: String? = null
  var rating: Int? = null
}