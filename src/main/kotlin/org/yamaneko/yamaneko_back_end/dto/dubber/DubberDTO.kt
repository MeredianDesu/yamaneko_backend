package org.yamaneko.yamaneko_back_end.dto.dubber

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO

data class DubberDTO @JsonCreator constructor(
    @JsonProperty( "dubberId" ) val dubberId: Long,
    @JsonProperty( "dubberName" ) val dubberName: String,
    @JsonProperty( "characters" ) val characters: List<CharacterDTO>
)
