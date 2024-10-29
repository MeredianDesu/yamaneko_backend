package org.yamaneko.yamaneko_back_end.service.news

import org.yamaneko.yamaneko_back_end.dto.news.NewsDTO

interface NewsService {
    fun getAdvertisements() : List<NewsDTO>
}