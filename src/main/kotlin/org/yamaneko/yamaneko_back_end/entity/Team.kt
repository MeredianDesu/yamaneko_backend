package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table(name = "team")
open class Team {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  internal var id: Long = 0
  
  @Column(name = "name")
  internal var name: String = ""
  
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  internal var user: User? = null
  
  override fun toString(): String {
    return "Team(id=$id, name='$name', user=$user)"
  }
}