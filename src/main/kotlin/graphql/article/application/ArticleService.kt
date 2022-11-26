package graphql.article.application

import graphql.article.domain.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {
    fun findAll() = articleRepository.findAll().map { it.toDto() }
}
