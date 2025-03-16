package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*
import org.yamaneko.yamaneko_back_end.config.converters.RolesDTOConverter
import org.yamaneko.yamaneko_back_end.dto.RolesDTO

@Entity
@Table( name = "releases" )
open class Release{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    internal var id: Long = 0

    @Column( name = "original_name" )
    internal var originalName: String = ""

    @Column( name = "translated_name" )
    internal var translatedName: String = ""

    @Column( name = "age_restriction" )
    internal var ageRestriction: String = ""

    @Column( name = "max_episodes" )
    internal var maxEpisodes: Int = 0

    @Column( name = "status" )
    internal var status: String = "Planned"

    @Column( name = "poster_image_url", columnDefinition = "varchar(255) default 'files/mascot.jfif'", nullable = true )
    internal var posterImageUrl: String? = null

    @Column( name = "preview_video_url", columnDefinition = "varchar(255) default 'files/preview.mp4'", nullable = true )
    internal var previewVideoUrl: String? = null

    @OneToMany( mappedBy = "release", cascade = [ CascadeType.ALL ] )
    internal var episodes: MutableList<Episode> = mutableListOf()

    @Column( name = "synopsis", columnDefinition = "TEXT" )
    internal var synopsis: String = ""

    @Column( name = "info", columnDefinition = "TEXT" )
    internal var info: String = ""

    @Column( name = "dubbers", columnDefinition = "text" )
    @Convert( converter = RolesDTOConverter::class )
    internal var dubbers: MutableList<RolesDTO> = mutableListOf()

    @ManyToMany
    @JoinTable(
        name = "release_genres",
        joinColumns = [ JoinColumn( name = "release_id" ) ],
        inverseJoinColumns = [ JoinColumn( name = "genre_id" ) ]
    )
    internal var genres: MutableSet<Genre> = mutableSetOf()

    @Column( name = "updated_at" )
    internal var updatedAt: String = ""

    @Column( name = "uploaded_at" )
    internal var uploadedAt: String = ""
}