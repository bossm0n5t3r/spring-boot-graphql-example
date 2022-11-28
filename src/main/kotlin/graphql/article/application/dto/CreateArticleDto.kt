package graphql.article.application.dto

import graphql.common.exception.ErrorMessage
import graphql.common.exception.InvalidException

data class CreateArticleDto(
    val subject: String,
    val content: String,
) {
    init {
        if (subject.isBlank()) throw InvalidException(ErrorMessage.SUBJECT_IS_BLANK.message)
        if (content.isBlank()) throw InvalidException(ErrorMessage.CONTENT_IS_BLANK.message)
    }
}
