package graphql.article.domain

import graphql.article.application.dto.CreateArticleDto
import graphql.article.application.dto.UpdateArticleDto
import graphql.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "article_info")
class Article(
    @Column(name = "subject", nullable = false)
    var subject: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "hits", nullable = false, updatable = true)
    var hits: Int = 0,

    @Column(name = "recommend", nullable = false, updatable = true)
    var recommend: Int = 0,
) : BaseEntity<Long>() {
    constructor(dto: CreateArticleDto) : this(
        subject = dto.subject,
        content = dto.content
    )

    fun updateArticle(updateArticleDto: UpdateArticleDto) {
        updateArticleDto.subject?.let { this.subject = it }
        updateArticleDto.content?.let { this.content = it }
    }

    fun updateHit() {
        this.hits++
    }

    fun updateRecommend() {
        this.recommend++
    }
}
