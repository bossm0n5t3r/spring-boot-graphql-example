package graphql.article.application.dto

import graphql.common.exception.ErrorMessage
import graphql.common.exception.InvalidException

data class UpdateArticleDto(
    val subject: String? = null,
    val content: String? = null,
) {
    init {
        if (subject.isNullOrBlank()) throw InvalidException(ErrorMessage.SUBJECT_IS_BLANK.message)
    }
}
