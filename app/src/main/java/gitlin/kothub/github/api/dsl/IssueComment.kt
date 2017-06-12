package gitlin.kothub.github.api.dsl


class IssueComment(override val level: Int): CommentFields {
    override val fields = arrayListOf<Field>()
}
