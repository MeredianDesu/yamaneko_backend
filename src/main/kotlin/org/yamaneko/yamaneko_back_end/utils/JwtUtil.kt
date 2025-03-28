package org.yamaneko.yamaneko_back_end.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yamaneko.yamaneko_back_end.customExceptions.JwtExpirationException
import org.yamaneko.yamaneko_back_end.customExceptions.JwtSignatureException
import org.yamaneko.yamaneko_back_end.entity.User
import java.util.*

@Component
class JwtUtil(
  @Value("\${jwt.secret}") val secretKey: String
) {
  
  // Первое число - минуты
  private val expirationMs: Long = 5 * 60 * 1000
  
  @Autowired
  private lateinit var dateFormatter: DateFormatter
  
  private val logger: Logger = LoggerFactory.getLogger(JwtUtil::class.java)
  
  private fun getEncodedKey(): ByteArray {
    return Base64.getEncoder().encode(secretKey.toByteArray()) // Кодируем ключ в Base64
  }
  
  fun generateToken(user: User): String {
    val claims: Map<String, Any> = mapOf(
      "id" to user.id, "roles" to user.roles
    )
    
    val key = Keys.hmacShaKeyFor(getEncodedKey())
    
    return Jwts.builder().claims(claims).subject(user.username).issuedAt(Date())
      .expiration(Date(System.currentTimeMillis() + expirationMs)).signWith(key).compact()
  }
  
  fun extractUserId(token: String): String? {
    val claims = extractClaims(token) ?: return null
    
    return claims["id"]?.toString()
  }
  
  fun extractExpiration(token: String): Date? {
    
    return extractClaims(token)?.expiration
  }
  
  fun extractCreation(token: String): Date? {
    
    return extractClaims(token)?.issuedAt
  }
  
  private fun extractClaims(token: String): Claims? {
    val key = Keys.hmacShaKeyFor(getEncodedKey())
    
    return try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
    } catch(e: Exception) {
      logger.error("JWT parsing error: {}", e.message)
      if(e.message?.contains("JWT signature does not match") == true) {
        throw JwtSignatureException("Invalid JWT signature.")
      }
      if(e.message?.contains("JWT expired") == true) {
        throw JwtExpirationException("JWT expired.")
      }
      null
    }
  }
  
  fun validateToken(token: String, id: String): Boolean {
    
    return id == extractUserId(token) && ! isTokenExpired(token)
  }
  
  private fun isTokenExpired(token: String): Boolean {
    
    return extractExpiration(token)?.before(Date()) ?: true
  }
  
  fun generateRefreshToken(): Map<String, String> {
    val token = UUID.randomUUID().toString()
    val issuedAt = dateFormatter.dateToString(Date())
    val expirationDate = dateFormatter.dateToString(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
    
    return mapOf(
      "token" to token, "issuedAt" to issuedAt, "expirationDate" to expirationDate
    )
  }
}