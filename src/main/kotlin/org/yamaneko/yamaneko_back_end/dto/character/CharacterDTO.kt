package org.yamaneko.yamaneko_back_end.dto.character

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CharacterDTO @JsonCreator constructor(
    @JsonProperty( "id" ) val id: Long,
    @JsonProperty( "originalName" ) val originalName: String,
    @JsonProperty( "translatedName" ) val translatedName: String,
    @JsonProperty( "image" ) val image: String?
)
