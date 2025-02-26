package org.yamaneko.yamaneko_back_end.api.controllers.private_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yamaneko.yamaneko_back_end.dto.news.NewsDTO
import org.yamaneko.yamaneko_back_end.service.news.NewsService

@Tag(name = "{ v1 } News API")
@RestController
@RequestMapping("api/news/v1")
class NewsController( @Autowired private val newsService: NewsService ) {

    @Operation( summary = "Get all news." )
    @GetMapping("")
    fun getAllNews(): List<NewsDTO>{

        return newsService.getAdvertisements()
    }
}