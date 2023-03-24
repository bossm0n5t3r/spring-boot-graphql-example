package graphql.article.presentation

import graphql.article.domain.Article
import graphql.article.domain.ArticleRepository
import graphql.article.presentation.dto.ArticleDto
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

@SpringBootTest
@AutoConfigureGraphQlTester
class ArticleControllerIntegrationTest @Autowired private constructor(
    private val graphQlTester: GraphQlTester,
    private val articleRepository: ArticleRepository,
) {
    private val faker = Faker()
    private val dollarString = "${'$'}"

    private fun getDummyArticles(): List<Article> {
        return (0 until Random.nextInt(1, 10)).map {
            Article(
                subject = faker.book().title(),
                content = faker.lorem().characters(),
                hits = Random.nextInt(),
                recommend = Random.nextInt(),
            )
        }
    }

    @BeforeEach
    fun setup() {
        articleRepository.saveAll(getDummyArticles())
    }

    @AfterEach
    fun cleanUp() {
        articleRepository.deleteAll()
    }

    @Test
    fun findAllShouldReturnAllArticles() {
        val document = """
            query {
                allArticles {
                    id,
                    subject,
                    content
                }
            }
        """.trimIndent()
        val dummyArticles = articleRepository.findAll()

        graphQlTester.document(document)
            .execute()
            .path("allArticles")
            .entityList(ArticleDto::class.java)
            .hasSize(dummyArticles.size)
    }

    @Test
    fun validIdShouldReturnArticle() {
        val document = """
            query findOneArticle(${dollarString}id: ID!) {
                article(articleId: ${dollarString}id) {
                    id,
                    subject,
                    content
                }
            }
        """.trimIndent()
        val randomArticle = articleRepository.findAll().random()
        val expectedId = randomArticle.id

        graphQlTester.document(document)
            .variable("id", expectedId)
            .execute()
            .path("article")
            .entity(ArticleDto::class.java)
            .satisfies { article ->
                assertEquals(article.subject, randomArticle.subject)
                assertEquals(article.content, randomArticle.content)
            }
    }

    @Test
    fun inValidIdShouldReturnArticle() {
        val document = """
            query findOneArticle(${dollarString}id: ID!) {
                article(articleId: ${dollarString}id) {
                    id,
                    subject,
                    content
                }
            }
        """.trimIndent()

        val expectedId = articleRepository.findAll().mapNotNull { it.id }.max() + 1

        graphQlTester.document(document)
            .variable("id", expectedId)
            .execute()
            .path("article")
            .valueIsNull()
    }

    @Test
    fun shouldCreateNewArticle() {
        val expectedSubject = faker.book().title()
        val expectedContent = faker.lorem().characters()

        val document = """
            mutation create(${dollarString}subject: String!, ${dollarString}content: String!) {
                createArticle(dto: {subject: ${dollarString}subject, content: ${dollarString}content}) {
                    id
                    subject
                    content
                }
            }
        """.trimIndent()

        graphQlTester.document(document)
            .variable("subject", expectedSubject)
            .variable("content", expectedContent)
            .execute()
            .path("createArticle")
            .entity(ArticleDto::class.java)
            .satisfies { article ->
                assertNotNull(article.id)

                assertEquals(
                    article.subject,
                    expectedSubject,
                )

                assertEquals(
                    article.content,
                    expectedContent,
                )
            }
    }

    @Test
    fun shouldUpdateExistingArticle() {
        val currentArticle = articleRepository.findAll().random()

        val document = """
            mutation update(${dollarString}articleId: ID!, ${dollarString}subject: String!, ${dollarString}content: String!) {
                updateArticle(articleId: ${dollarString}articleId, dto: {subject: ${dollarString}subject, content: ${dollarString}content}) {
                    id
                    subject
                    content
                }
            }
        """.trimIndent()

        val newSubject = "newSubject"
        val newContent = "newContent"

        graphQlTester.document(document)
            .variable("articleId", currentArticle.id)
            .variable("subject", newSubject)
            .variable("content", newContent)
            .execute()
            .path("updateArticle")
            .entity(ArticleDto::class.java)

        val updatedArticle = articleRepository.findById(currentArticle.id ?: -1).getOrNull()

        assertNotNull(updatedArticle)
        assertThat(updatedArticle?.subject).isEqualTo(newSubject)
        assertThat(updatedArticle?.content).isEqualTo(newContent)
    }

    @Test
    fun shouldRemoveArticleWithValidId() {
        val allArticles = articleRepository.findAll()
        val currentArticleCount = allArticles.size
        val randomArticleId = allArticles.mapNotNull { it.id }.random()

        val document = """
            mutation delete(${dollarString}articleId: ID!) {
                deleteArticle(articleId: ${dollarString}articleId) {
                    id
                    subject
                    content
                }
            }
        """.trimIndent()

        graphQlTester.document(document)
            .variable("articleId", randomArticleId)
            .executeAndVerify()

        assertEquals(
            currentArticleCount - 1,
            articleRepository.findAll().size
        )
    }
}
