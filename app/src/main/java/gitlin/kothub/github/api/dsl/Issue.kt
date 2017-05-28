package gitlin.kothub.github.api.dsl


class Issue(override val level: Int): CommentFields {

    override val fields = arrayListOf<Field>()

    fun comments(first: Int = 10, body: IssueCommentConnection.() -> Unit) {
        val connection = IssueCommentConnection(nextLevel())
        connection.body()
        addField(Node("comments", connection.fields))
    }

}

