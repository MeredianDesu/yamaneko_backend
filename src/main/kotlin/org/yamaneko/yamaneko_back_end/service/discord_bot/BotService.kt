package org.yamaneko.yamaneko_back_end.service.discord_bot

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.yamaneko.yamaneko_back_end.dto.bot_dto.BotRequestDTO

interface BotService {
    fun createReleaseNotification( @Valid @RequestBody request: BotRequestDTO): ResponseEntity<Any>
    fun createCharacterNotification( @Valid @RequestBody request: BotRequestDTO ): ResponseEntity<Any>
}