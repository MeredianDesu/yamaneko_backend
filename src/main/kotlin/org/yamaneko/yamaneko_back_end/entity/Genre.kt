package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*

@Entity
@Table( name = "genres" )
open class Genre{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    internal var id: Long = 0

    @Column( name = "name" )
    internal var name: String = ""

    //Ссылки
    @ManyToMany( mappedBy = "genres" )
    internal var releases: MutableSet<Release> = mutableSetOf()
}