package gitlin.kothub.github.api.dsl

class IssueCommentEdge(override val level: Int): Edges(level) {

    fun node (body: IssueComment.() -> Unit) {
        val issue = IssueComment(nextLevel())
        issue.body()
        addField(Node(fields = issue.fields))
    }
}
