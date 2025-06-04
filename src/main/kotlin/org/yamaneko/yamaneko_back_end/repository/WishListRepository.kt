package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yamaneko.yamaneko_back_end.dto.wishlist.WishListId
import org.yamaneko.yamaneko_back_end.entity.WishList

interface WishListRepository: JpaRepository<WishList, WishListId> {
  
  fun findAllByIdUserId(userId: Long): List<WishList>?
}
