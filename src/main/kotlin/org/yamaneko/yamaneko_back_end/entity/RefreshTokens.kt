package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "refresh_tokens" )
class RefreshTokens {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @ManyToOne
    @JoinColumn( name = "user_id", nullable = false )
    internal var user: User? = null

    @Column( name = "token_hash", columnDefinition = "varchar(512)" ,nullable = false )
    internal var tokenHash: String = ""

    @Column( name = "created_at" )
    internal var createdAt: String = ""

    @Column( name = "expires_at" )
    internal var expiresAt: String = ""

    @Column( name = "is_revoked", columnDefinition = "BOOLEAN default false" )
    internal var isRevoked: Boolean = false
}