package org.yamaneko.yamaneko_back_end.service.wishlist

import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.wishlist.WishListId
import org.yamaneko.yamaneko_back_end.entity.WishList
import org.yamaneko.yamaneko_back_end.repository.WishListRepository
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import java.util.*

@Service
class WishListServiceImpl(private val wishListRepository: WishListRepository): WishListService {
  
  private val dateFormatter = DateFormatter()
  
  override fun getWishListByUserId(userId: Long): List<WishList>? {
    val list = wishListRepository.findAllByIdUserId(userId)
    
    return if(list.isNullOrEmpty()) {
      null
    } else {
      list
    }
  }
  
  override fun addToWishList(wishList: WishListId): Boolean {
    val isRecord =
      wishListRepository.findById(wishList)
        .orElse(null)
    
    if(isRecord != null) return false
    
    val record = WishList().apply {
      id = WishListId(wishList.userId, wishList.releaseId)
      addedAt = dateFormatter.dateToString(Date())
    }
    
    return try {
      wishListRepository.save(record)
      true
    } catch(ex: Exception) {
      false
    }
  }
  
  override fun removeFromWishList(wishList: WishListId): Boolean {
    return try {
      wishListRepository.deleteById(wishList)
      true
    } catch(ex: Exception) {
      false
    }
  }
}