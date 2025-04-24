package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
open class User(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) internal var id: Long = 0,
  
  @Column(name = "username", unique = true) internal var username: String = "",
  
  @Column(name = "password", columnDefinition = "varchar(65)") internal var password: String = "",
  
  @Column(name = "email", unique = true) internal var email: String = "",
  
  @Enumerated(EnumType.STRING) @Column(name = "roles", nullable = false) internal var roles: Role = Role.ROLE_USER,
  
  @Column(name = "header") internal var header: String? = "",

//  @Column(name = "roles")
//  @Convert(converter = StringSetConverter::class)
//  internal var roles: MutableSet<String> = mutableSetOf("USER"),
  
  @Column(name = "avatar") internal var avatar: String? = "",
  
  @Column(name = "created_at") internal var createdAt: String = "",

// Links
  @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL]) internal var team: Team? = null,
  
  @OneToMany(
    mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true
  ) internal var userAchievements: MutableList<UserAchievement> = mutableListOf()


): UserDetails {
  
  override fun getAuthorities(): Collection<GrantedAuthority> {
    return listOf(SimpleGrantedAuthority(roles.name))
  }
  
  override fun getPassword(): String {
    return password
  }
  
  override fun getUsername(): String {
    return username
  }
  
  override fun isAccountNonExpired(): Boolean {
    return true
  }
  
  override fun isAccountNonLocked(): Boolean {
    return true
  }
  
  override fun isCredentialsNonExpired(): Boolean {
    return true
  }
  
  override fun isEnabled(): Boolean {
    return true
  }
  
  override fun toString(): String {
    return "User(id=$id, username='$username', password='$password', email='$email', roles=$roles, header='$header', avatar='$avatar', createdAt='$createdAt')"
  }
}