package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.news.NewsDTO
import org.yamaneko.yamaneko_back_end.entity.News
import org.yamaneko.yamaneko_back_end.entity.User
import org.yamaneko.yamaneko_back_end.repository.UserRepository

class NewsMapper {

    private val userRepository: UserRepository? = null

    fun toDTO( news: News ): NewsDTO {
        return NewsDTO(
            id = news.id,
            title = news.title,
            content = news.content,
            previewImg = news.previewImg,
            createdAt = news.createdAt
        )
    }

    fun toEntity( newsDTO: NewsDTO ): News{
        val news = News()
        news.id = newsDTO.id
        news.title = newsDTO.title
        news.content = newsDTO.content
        news.previewImg = newsDTO.previewImg
        news.createdAt = newsDTO.createdAt

        return news
    }
}