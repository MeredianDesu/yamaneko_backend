package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table(name = "release_states")
open class ReleaseState(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") internal var id: Long? = 0,
  @Column(name = "state") internal var state: String? = "",
) {

}