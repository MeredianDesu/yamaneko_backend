package org.yamaneko.yamaneko_back_end.dto.dubber

data class DubberRequestPost(
    val dubberId: Long, // team`s id
    val charactersId: List<Long>,
)
