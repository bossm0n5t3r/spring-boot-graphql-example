package graphql.article.presentation.dto

data class ArticleDto(
    val id: Long,
    val subject: String,
    val content: String,
    val hits: Int,
    val recommend: Int,
)
