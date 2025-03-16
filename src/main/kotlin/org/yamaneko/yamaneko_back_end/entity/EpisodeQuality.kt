package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "episode_qualities")
open class EpisodeQuality(
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    internal var id: Long = 0,

    @ManyToOne
    @JoinColumn( name = "episode_id", nullable = false )
    internal var episode: Episode,

    @Column( name = "quality" )
    internal var quality: String = "", // "480p" | "720p" | "1080p"

    @Column( name = "video_url" )
    internal var videoUrl: String = "",
)