package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "characters" )
open class Character{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @Column( name = "original_name" )
    internal var originalName: String = ""

    @Column( name = "translated_name" )
    internal var translatedName: String = ""

    @Column( name = "image", columnDefinition = "varchar(255) default 'var/yamaneko_files/mascot.jfif'", nullable = true )
    internal var image: String? = null

    override fun toString(): String {
        return "Character(id=$id, originalName='$originalName', translatedName='$translatedName', image='$image')"
    }
}