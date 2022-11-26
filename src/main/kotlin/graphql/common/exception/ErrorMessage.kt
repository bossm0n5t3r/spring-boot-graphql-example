package graphql.common.exception

enum class ErrorMessage(val message: String) {
    SUBJECT_IS_BLANK("Subject is blank."),
    CONTENT_IS_BLANK("Content is blank."),
    NOT_FOUND_ARTICLE_BY_ID("There are no articles for this id."),
    NOT_FOUND_COMMENT("There is no comment."),
}
