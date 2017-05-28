package gitlin.kothub.github.api.dsl


class Issue(override val level: Int): CommentFields {

    override val fields = arrayListOf<Field>()

    fun comments(first: Int = 10, body: IssueCommentConnection.() -> Unit) {
        val connection = IssueCommentConnection(nextLevel())
        connection.body()
        addField(Node("comments", connection.fields))
    }

}

class IssueEdges(override val level: Int): Edges(level) {
    fun node (body: Issue.() -> Unit) {
        val issue = Issue(nextLevel())
        issue.body()
        addField(Node(fields = issue.fields))
    }
}


class IssueConnection(override val level: Int) : Connection<Issue>(level) {
    fun edges (body: IssueEdges.() -> Unit) {
        val edges = IssueEdges(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}


