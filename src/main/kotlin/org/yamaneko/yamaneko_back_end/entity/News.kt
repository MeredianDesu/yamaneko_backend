package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "news" )
open class News{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @Column( name = "title" )
    internal var title: String = ""

    @Column( name = "content" )
    internal var content: String = ""

    @Column( name = "preview_img" )
    internal var previewImg: String = ""

    @Column( name = "created_at" )
    internal var createdAt: String = ""
}