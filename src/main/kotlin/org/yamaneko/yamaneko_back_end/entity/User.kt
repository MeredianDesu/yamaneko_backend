package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*
import org.yamaneko.yamaneko_back_end.config.converters.StringSetConverter

@Entity
@Table( name = "users" )
open class User{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @Column( name = "username", unique = true )
    internal var username: String = ""

    @Column( name = "password", columnDefinition = "varchar(65)" )
    internal var password: String = ""

    @Column( name = "email", unique = true )
    internal var email: String = ""

    @Column( name = "roles" )
    @Convert( converter = StringSetConverter::class )
    internal var roles: MutableSet<String> = mutableSetOf("USER")

    @Column( name = "avatar" )
    internal var avatar: String = ""

    @Column( name = "created_at" )
    internal var createdAt: String = ""

    // Links
    @OneToOne( mappedBy = "user", cascade = [CascadeType.ALL] )
    internal var team: Team? = null

    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password', email='$email', roles=$roles, avatar='$avatar', createdAt='$createdAt')"
    }
}