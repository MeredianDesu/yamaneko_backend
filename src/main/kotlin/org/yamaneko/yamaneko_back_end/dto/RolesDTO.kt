package org.yamaneko.yamaneko_back_end.dto

data class RolesDTO(
    val characterId: Long, // dubber id
    val characterOriginalName: String, // character names
    val characterTranslatedName: String,
    val characterImage: String?,
    val dubberName: String
)
