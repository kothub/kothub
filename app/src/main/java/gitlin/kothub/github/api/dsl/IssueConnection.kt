package gitlin.kothub.github.api.dsl


class IssueConnection(override val level: Int) : Connection<Issue>(level) {
    fun edges (body: IssueEdges.() -> Unit) {
        val edges = IssueEdges(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}

