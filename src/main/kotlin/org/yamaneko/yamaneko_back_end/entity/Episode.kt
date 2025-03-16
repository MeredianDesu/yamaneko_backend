package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table( name = "episodes" )
open class Episode(
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    internal val id: Long = 0,

    @ManyToOne
    @JoinColumn( name = "release_id", nullable = false )
    internal var release: Release,

    @Column( name = "episode_number" )
    internal var episodeNumber: Long = 1,

    @Column( name = "episode_name" )
    internal var episodeName: String = "",

    @OneToMany( mappedBy = "episode", cascade = [ CascadeType.ALL ] )
    internal var qualities: MutableList<EpisodeQuality> = mutableListOf(),
)