package org.yamaneko.yamaneko_back_end.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yamaneko.yamaneko_back_end.entity.User
import java.security.MessageDigest
import java.util.*


@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    private var secretKey: String = ""
    private val expirationMs: Long = 360_000

    fun generateToken( user: User ): String{
        val claims: Map<String, Any> = mapOf(
            "id" to user.id,
//            "roles" to user.roles
        )

        val key = Keys.hmacShaKeyFor( secretKey.toByteArray() )

        return Jwts.builder()
            .claims( claims )
//            .subject( user.username )
            .issuedAt( Date() )
            .expiration( Date(System.currentTimeMillis() + expirationMs ) )
            .signWith( key )
            .compact()
    }

    fun extractUsername( token: String ): String?{

        return extractClaims( token )?.subject
    }

    fun extractRoles( token: String ): List<String>? {
        val roles = extractClaims( token )?.get( "roles", List::class.java )

        return roles?.filterIsInstance<String>()
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
            println( "Не удалось спарсить токен" )
            null
        }
    }

    fun hashJwtToken( token: String ): String{
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest( token.toByteArray() )

        return Base64.getEncoder().encodeToString( hashBytes )
    }
}