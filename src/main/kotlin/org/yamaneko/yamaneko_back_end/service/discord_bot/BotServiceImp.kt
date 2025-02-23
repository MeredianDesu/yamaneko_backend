package org.yamaneko.yamaneko_back_end.service.discord_bot

import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.yamaneko.yamaneko_back_end.dto.bot_dto.BotRequestDTO

@Service
class BotServiceImp: BotService {
    private val botApi = "http://localhost:8080/webhook/"
    private val restTemplate = RestTemplate()
    private val headers = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
    }

    override fun createReleaseNotification( request: BotRequestDTO): ResponseEntity<Any> {
        val requestEntity = HttpEntity(request, headers)
        val endpoint = "releases"

        try {
            restTemplate.postForEntity("$botApi/$endpoint", requestEntity, String::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }

        return ResponseEntity.ok().build()
    }

    override fun createCharacterNotification( request: BotRequestDTO ): ResponseEntity<Any> {
        val requestEntity = HttpEntity(request, headers)
        val endpoint = "characters"

        try {
            restTemplate.postForEntity("$botApi/$endpoint", requestEntity, String::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }

        return ResponseEntity.ok().build()
    }
}