package org.yamaneko.yamaneko_back_end.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yamaneko.yamaneko_back_end.entity.User
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") val secretKey: String
) {
    private val expirationMs: Long = 360_000

    @Autowired
    private lateinit var dateFormatter: DateFormatter

    fun generateToken( user: User ): String{
        val claims: Map<String, Any> = mapOf(
            "id" to user.id,
            "roles" to user.roles
        )

        val key = Keys.hmacShaKeyFor( secretKey.toByteArray() )

        return Jwts.builder()
            .claims( claims )
            .issuedAt( Date() )
            .expiration( Date(System.currentTimeMillis() + expirationMs ) )
            .signWith( key )
            .compact()
    }

    fun extractUserId( token: String ): String? {
        val claims = extractClaims( token )

        return claims?.get("id", Integer::class.java )?.toString()
    }

    fun extractExpiration( token: String ): Date? {

        return extractClaims( token )?.expiration
    }

    fun extractCreation( token: String ): Date?{

        return extractClaims( token )?.issuedAt
    }

    private fun extractClaims( token: String ): Claims?{
        val key = Keys.hmacShaKeyFor( secretKey.toByteArray() )

        return try{
            Jwts.parser()
                .verifyWith( key )
                .build()
                .parseSignedClaims( token )
                .payload
        } catch ( e: Exception ){
            println( "Cannot parse token: $e" )
            return null
        }
    }

    fun validateToken( token: String, id: String ): Boolean {

        return id == extractUserId( token )/* && !isTokenExpired( token )*/
    }

    fun generateRefreshToken(): Map<String, String>{
        val token = UUID.randomUUID().toString()
        val issuedAt = dateFormatter.dateToString( Date() )
        val expirationDate = dateFormatter.dateToString( Date( System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 ) )

        return mapOf(
            "token" to token,
            "issuedAt" to issuedAt,
            "expirationDate" to expirationDate
        )
    }
}