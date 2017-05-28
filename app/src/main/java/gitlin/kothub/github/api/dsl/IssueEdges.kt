package gitlin.kothub.github.api.dsl


class IssueEdges(override val level: Int): Edges(level) {
    fun node (body: Issue.() -> Unit) {
        val issue = Issue(nextLevel())
        issue.body()
        addField(Node(fields = issue.fields))
    }
}
