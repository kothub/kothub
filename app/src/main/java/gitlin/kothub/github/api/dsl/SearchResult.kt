package gitlin.kothub.github.api.dsl



class SearchResult(override val level: Int): CommentFields {
    override val fields = arrayListOf<Field>()
}

class SearchResultEdges(override val level: Int): Edges(level) {
    fun node (body: SearchResult.() -> Unit) {
        val search = SearchResult(nextLevel())
        search.body()
        addField(Node(fields = search.fields))
    }

    fun nodeAsUser (body: User.() -> Unit) {
        val user = User(nextLevel())
        user.body()
        addField(Node(fields = user.fields))
    }
}


class SearchResultConnection(override val level: Int) : Connection<SearchResult>(level) {
    fun edges (body: SearchResultEdges.() -> Unit) {
        val edges = SearchResultEdges(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}


