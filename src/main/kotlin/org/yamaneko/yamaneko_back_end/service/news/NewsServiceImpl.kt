package org.yamaneko.yamaneko_back_end.service.news

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.news.NewsDTO
import org.yamaneko.yamaneko_back_end.mappers.NewsMapper
import org.yamaneko.yamaneko_back_end.repository.NewsRepository

@Service
class NewsServiceImpl: NewsService {

    private val newsMapper = NewsMapper()

    @Autowired
    private lateinit var newsRepository: NewsRepository

    override fun getAdvertisements(): List<NewsDTO> {
        val news = newsRepository.findAll()

        return news.map { newsMapper.toDTO( it ) }
    }
}