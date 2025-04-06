package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table(name = "achievements")
open class Achievement(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") internal var id: Long = 0,
  @Column(name = "image") internal var image: String = "",
  @Column(name = "name") internal var name: String = "",
  @Column(name = "condition") internal var condition: String = "",
) {
  
  @OneToMany(mappedBy = "achievement", cascade = [CascadeType.ALL], orphanRemoval = true)
  internal var userAchievements: MutableList<UserAchievement> = mutableListOf()
}