package gitlin.kothub.github.api.dsl

enum class RepositoryPrivacy (val value: String){
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE")
}



class Repository(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val description: Unit get() = addField("description")
    val name: Unit get() = addField("name")


    fun issues(first: Variable<Int>? = null, states: Variable<IssueState>? = null, body: IssueConnection.() -> Unit) {
        val connection = IssueConnection(nextLevel())
        connection.body()
        addField(Node("issues", connection.fields, variables("first" to first, "states" to states)))
    }

    fun stargazers(body: UserConnection.() -> Unit) {
        val connection = UserConnection(nextLevel())
        connection.body()
        addField(Node("stargazers", connection.fields))
    }

    fun forks(body: UserConnection.() -> Unit) {
        val connection = UserConnection(nextLevel())
        connection.body()
        addField(Node("forks", connection.fields))
    }

    fun primaryLanguage(body: Language.() -> Unit) {
        val language = Language(nextLevel())
        language.body()
        addField(Node("primaryLanguage", language.fields))
    }
}

class RepositoryConnection(override val level: Int) : Connection<Repository>(level) {
    fun edges (body: RepositoryEdge.() -> Unit) {
        val edges = RepositoryEdge(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }

    fun nodes (body: Repository.() -> Unit) {
        val repo = Repository(nextLevel())
        repo.body()
        addField(Node("nodes", repo.fields))
    }

}


class RepositoryEdge(override val level: Int): Edges(level) {
    fun node (body: Repository.() -> Unit) {
        val repo = Repository(nextLevel())
        repo.body()
        addField(Node(fields = repo.fields))
    }
}
