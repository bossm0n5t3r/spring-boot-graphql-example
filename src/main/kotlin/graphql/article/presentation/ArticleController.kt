package graphql.article.presentation

import graphql.article.application.ArticleService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ArticleController(
    private val articleService: ArticleService,
) {
    @QueryMapping(name = "getAllArticles")
    fun getAllArticles() = articleService.findAll()
}
