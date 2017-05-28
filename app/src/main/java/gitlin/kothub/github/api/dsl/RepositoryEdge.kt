package gitlin.kothub.github.api.dsl

class RepositoryEdge(override val level: Int): Edges(level) {
    fun node (body: Repository.() -> Unit) {
        val issue = Repository(nextLevel())
        issue.body()
        addField(Node(fields = issue.fields))
    }
}
