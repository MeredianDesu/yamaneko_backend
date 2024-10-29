package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yamaneko.yamaneko_back_end.entity.News

interface NewsRepository: JpaRepository<News, Long>