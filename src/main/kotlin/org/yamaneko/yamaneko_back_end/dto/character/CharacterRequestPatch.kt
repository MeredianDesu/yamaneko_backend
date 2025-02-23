package org.yamaneko.yamaneko_back_end.dto.character

data class CharacterRequestPatch(
    val originalName: String? = null,
    val translatedName: String? = null,
    val image: String? = null,
)
