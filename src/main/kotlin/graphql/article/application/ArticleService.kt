package graphql.article.application

import graphql.article.application.dto.CreateArticleDto
import graphql.article.application.dto.UpdateArticleDto
import graphql.article.domain.Article
import graphql.article.domain.ArticleRepository
import graphql.article.presentation.dto.ArticleDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {
    fun findAll() = articleRepository.findAll().map { it.toDto() }

    fun findById(articleId: Long): ArticleDto? {
        return articleRepository
            .findById(articleId)
            .orElse(null)
            ?.toDto()
    }

    @Transactional
    fun createArticle(dto: CreateArticleDto): ArticleDto {
        return articleRepository.save(Article(dto)).toDto()
    }

    @Transactional
    fun updateArticle(articleId: Long, dto: UpdateArticleDto): ArticleDto? {
        return articleRepository
            .findById(articleId)
            .orElse(null)
            ?.let {
                it.updateArticle(dto)
                it.toDto()
            }
    }

    @Transactional
    fun deleteArticle(articleId: Long): ArticleDto? {
        return articleRepository
            .findById(articleId)
            .map { article -> article.toDto() }
            .orElse(null)
            ?.also { articleRepository.deleteById(articleId) }
    }
}
