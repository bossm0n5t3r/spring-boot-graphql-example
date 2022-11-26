package graphql.article.presentation

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.article.application.ArticleService

@DgsComponent
class ArticleDataFetcher(
    private val articleService: ArticleService,
) {
    @DgsQuery
    fun getAllArticles() = articleService.findAll()
}
