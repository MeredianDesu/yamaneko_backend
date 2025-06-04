package org.yamaneko.yamaneko_back_end.service.wishlist

import org.yamaneko.yamaneko_back_end.dto.wishlist.WishListId
import org.yamaneko.yamaneko_back_end.entity.WishList

interface WishListService {
  
  fun getWishListByUserId(userId: Long): List<WishList>?
  fun addToWishList(wishList: WishListId): Boolean
  fun removeFromWishList(wishList: WishListId): Boolean
}