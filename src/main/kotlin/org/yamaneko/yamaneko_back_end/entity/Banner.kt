package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "banners" )
open class Banner{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @Column( name = "banner_img" )
    internal var bannerImg: String = ""

    @Column( name = "poster_link" )
    internal var posterLink: String = ""

    @Column( name = "visible", columnDefinition = "boolean default false" )
    internal var visible: Boolean = false

    @Column( name = "created_at" )
    internal var createdAt: String = ""
}