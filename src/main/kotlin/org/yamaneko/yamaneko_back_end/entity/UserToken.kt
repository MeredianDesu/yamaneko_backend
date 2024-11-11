package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "user_tokens" )
class UserToken {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @ManyToOne
    @JoinColumn( name = "user_id", nullable = false )
    internal var user: User? = null

    @Column( name = "jwt_token", columnDefinition = "varchar(512)" ,nullable = false )
    internal var jwtToken: String = ""

    @Column( name = "created_at" )
    internal var createdAt: String = ""

    @Column( name = "expires_at" )
    internal var expiresAt: String = ""

    @Column( name = "is_revoked", columnDefinition = "BOOLEAN default false" )
    internal var isRevoked: Boolean = false
}