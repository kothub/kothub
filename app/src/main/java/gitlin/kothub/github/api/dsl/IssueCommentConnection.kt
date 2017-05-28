package gitlin.kothub.github.api.dsl


class IssueCommentConnection(override val level: Int): Connection<IssueComment>(level) {

    fun edges (body: IssueCommentEdge.() -> Unit) {
        val edges = IssueCommentEdge(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}

