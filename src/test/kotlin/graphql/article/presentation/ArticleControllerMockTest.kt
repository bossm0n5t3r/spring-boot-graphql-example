package graphql.article.presentation

import com.ninjasquad.springmockk.MockkBean
import graphql.article.application.ArticleService
import graphql.article.domain.Article
import graphql.article.domain.ArticleRepository
import graphql.article.presentation.dto.ArticleDto
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.context.annotation.Import
import org.springframework.graphql.test.tester.GraphQlTester
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

@GraphQlTest(ArticleController::class)
@Import(ArticleService::class)
class ArticleControllerMockTest @Autowired private constructor(
    private val graphQlTester: GraphQlTester,
) {
    @MockkBean
    private lateinit var mockArticleRepository: ArticleRepository

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
        val expectedArticles = getDummyArticles()
        every { mockArticleRepository.findAll() } returns expectedArticles

        graphQlTester.document(document)
            .execute()
            .path("allArticles")
            .entityList(ArticleDto::class.java)
            .hasSize(expectedArticles.size)
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

        val expectedId = Random.nextLong()

        val dummySubject = faker.book().title()
        val dummyContent = faker.lorem().characters()
        val mockArticle = mockk<Article>(relaxed = true) {
            every { id } returns expectedId
            every { subject } returns dummySubject
            every { content } returns dummyContent
        }
        every { mockArticleRepository.findById(expectedId) } returns Optional.of(mockArticle)

        graphQlTester.document(document)
            .variable("id", expectedId)
            .execute()
            .path("article")
            .entity(ArticleDto::class.java)
            .satisfies { article ->
                assertEquals(article.subject, dummySubject)
                assertEquals(article.content, dummyContent)
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

        val expectedId = Random.nextLong()
        every { mockArticleRepository.findById(expectedId) } returns Optional.empty()

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
        every { mockArticleRepository.save(any()) } returns mockk(relaxed = true) {
            every { id } returns Random.nextLong()
            every { subject } returns expectedSubject
            every { content } returns expectedContent
        }

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
        val currentSubject = "currentSubject"
        val currentContent = "currentContent"

        val expectedId = Random.nextLong()
        val mockArticle = Article(subject = currentSubject, content = currentContent)
        every { mockArticleRepository.findById(expectedId) } returns Optional.of(mockArticle)

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
            .variable("articleId", expectedId)
            .variable("subject", newSubject)
            .variable("content", newContent)
            .execute()
            .path("updateArticle")
            .entity(ArticleDto::class.java)

        val updatedArticle = mockArticleRepository.findById(expectedId).getOrNull()
        assertNotNull(updatedArticle)
        assertEquals(updatedArticle?.subject, newSubject)
        assertEquals(updatedArticle?.content, newContent)
    }

    @Test
    fun shouldRemoveArticleWithValidId() {
        val mockArticle = mockk<Article>(relaxed = true) {
            every { id } returns Random.nextLong()
            every { subject } returns faker.book().title()
            every { content } returns faker.lorem().characters()
        }
        every { mockArticleRepository.findById(any()) } returns Optional.of(mockArticle)
        every { mockArticleRepository.deleteById(any()) } just runs

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
            .variable("articleId", Random.nextLong())
            .executeAndVerify()

        verify(exactly = 1) {
            mockArticleRepository.findById(any())
            mockArticleRepository.deleteById(any())
        }
    }
}
