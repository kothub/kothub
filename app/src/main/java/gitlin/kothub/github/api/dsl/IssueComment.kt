package gitlin.kothub.github.api.dsl


class IssueComment(override val level: Int): CommentFields {
    override val fields = arrayListOf<Field>()
}

class IssueCommentConnection(override val level: Int): Connection<IssueComment>(level) {

    fun edges (body: IssueCommentEdge.() -> Unit) {
        val edges = IssueCommentEdge(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}


class IssueCommentEdge(override val level: Int): Edges(level) {

    fun node (body: IssueComment.() -> Unit) {
        val issue = IssueComment(nextLevel())
        issue.body()
        addField(Node(fields = issue.fields))
    }
}
