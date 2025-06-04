package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.wishlist.WishListId
import org.yamaneko.yamaneko_back_end.dto.wishlist.WishListRequestDTO
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService
import org.yamaneko.yamaneko_back_end.service.wishlist.WishListService

@Tag(name = "{ v1 } WishList API")
@RestController
@RequestMapping("/api/wishlist/v1")
class WishListController(
  private val wishListService: WishListService,
  private val userService: UserService, private val userRepository: UserRepository,
) {
  
  
  @GetMapping("{userId}")
  fun getWishList(@PathVariable userId: Long): ResponseEntity<Any> {
    val response = wishListService.getWishListByUserId(userId)
    
    return if(response == null) {
      ResponseEntity.notFound()
        .build()
    } else {
      ResponseEntity.ok()
        .body(response)
    }
  }
  
  @PostMapping("")
  fun addToWishList(
    @RequestBody wishlistRequest: WishListRequestDTO, authentication: Authentication
  ): ResponseEntity<Any> {
    val userDetails = authentication.principal as UserDetails
    val user = userRepository.findByUsername(userDetails.username)
    
    if(user?.id != wishlistRequest.id.userId) return ResponseEntity.status(HttpStatus.FORBIDDEN)
      .build()
    
    val result = wishListService.addToWishList(wishlistRequest.id)
    
    return if(result) {
      ResponseEntity.ok()
        .build()
    } else {
      ResponseEntity.badRequest()
        .body("Release already exists or bad request")
    }
  }
  
  @DeleteMapping("")
  fun removeFromWishlist(
    @RequestBody wishlistRequest: WishListId, authentication: Authentication
  ): ResponseEntity<Any> {
    val userDetails = authentication.principal as UserDetails
    val user = userRepository.findByUsername(userDetails.username)
    
    if(user?.id != wishlistRequest.userId) return ResponseEntity.status(HttpStatus.FORBIDDEN)
      .build()
    
    val result = wishListService.removeFromWishList(wishlistRequest)
    
    return if(result) {
      ResponseEntity.ok()
        .build()
    } else {
      ResponseEntity.notFound()
        .build()
    }
  }
}