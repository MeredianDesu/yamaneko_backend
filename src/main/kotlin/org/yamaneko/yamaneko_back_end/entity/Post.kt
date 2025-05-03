package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table(name = "posts")
open class Post {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  internal var id: Long = 0
  
  @Column(name = "text", columnDefinition = "text", nullable = false)
  internal var text: String = ""
  
  @Column(name = "created_at")
  internal var createdAt: String = ""
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  open var user: User? = null
}