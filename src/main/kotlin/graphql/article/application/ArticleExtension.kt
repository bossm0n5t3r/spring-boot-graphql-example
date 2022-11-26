package graphql.article.application

import graphql.article.domain.Article
import graphql.article.presentation.dto.ArticleDto

fun Article.toDto() = ArticleDto(
    id = this.id ?: 0L,
    subject = this.subject,
    content = this.content,
    hits = this.hits,
    recommend = this.recommend
)
