package gitlin.kothub.github.api.dsl



class RepositoryConnection(override val level: Int) : Connection<Repository>(level) {
    fun edges (body: RepositoryEdge.() -> Unit) {
        val edges = RepositoryEdge(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}

