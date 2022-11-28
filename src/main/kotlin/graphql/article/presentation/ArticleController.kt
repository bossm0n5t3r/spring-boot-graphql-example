package graphql.article.presentation

import graphql.article.application.ArticleService
import graphql.article.application.dto.CreateArticleDto
import graphql.article.application.dto.UpdateArticleDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ArticleController(
    private val articleService: ArticleService,
) {
    @QueryMapping
    fun allArticles() = articleService.findAll()

    @QueryMapping
    fun article(@Argument articleId: Long) = articleService.findById(articleId)

    @MutationMapping
    fun createArticle(@Argument dto: CreateArticleDto) = articleService.createArticle(dto)

    @MutationMapping
    fun updateArticle(
        @Argument articleId: Long,
        @Argument dto: UpdateArticleDto,
    ) = articleService.updateArticle(articleId, dto)

    @MutationMapping
    fun deleteArticle(@Argument articleId: Long) = articleService.deleteArticle(articleId)
}
