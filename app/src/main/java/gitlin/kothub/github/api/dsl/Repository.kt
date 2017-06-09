package gitlin.kothub.github.api.dsl

enum class RepositoryPrivacy (val value: String){
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE")
}



class Repository(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val description: Unit get() = addField("description")
    val name: Unit get() = addField("name")
    val nameWithOwner: Unit get() = addField("nameWithOwner")
    val license: Unit get() = addField("license")
    val viewerHasStarred: Unit get() = addField("viewerHasStarred")
    val viewerCanSubscribe: Unit get() = addField("viewerCanSubscribe")
    val pushedAt: Unit get() = addField("pushedAt")


    fun owner (body: User.() -> Unit) {
        val user = User(nextLevel())
        user.body()
        addField(Node("owner", user.fields))
    }


    fun issues(first: Variable<Int>? = null, states: Variable<IssueState>? = null, body: Connection<Issue>.() -> Unit) {
        val connection = Connection<Issue>(nextLevel())
        connection.body()
        addField(Node("issues", connection.fields, variables("first" to first, "states" to states)))
    }

    fun watchers(body: Connection<User>.() -> Unit) {
        val connection = Connection<User>(nextLevel())
        connection.body()
        addField(Node("watchers", connection.fields))
    }


    fun stargazers(body: Connection<User>.() -> Unit) {
        val connection = Connection<User>(nextLevel())
        connection.body()
        addField(Node("stargazers", connection.fields))
    }

    fun forks(body: Connection<User>.() -> Unit) {
        val connection = Connection<User>(nextLevel())
        connection.body()
        addField(Node("forks", connection.fields))
    }

    fun primaryLanguage(body: Language.() -> Unit) {
        val language = Language(nextLevel())
        language.body()
        addField(Node("primaryLanguage", language.fields))
    }

    fun obj(alias: String, expression: Variable<String>, body: GitObject.() -> Unit) {

        val gitobject = GitObject(nextLevel())
        gitobject.body()
        addField(Node("$alias: object", gitobject.fields, variables("expression" to expression)))
    }
}
//
//class RepositoryConnection(override val level: Int) : Connection<Repository>(level) {
//    fun edges (body: RepositoryEdge.() -> Unit) {
//        val edges = RepositoryEdge(nextLevel())
//        edges.body()
//        addField(Node("edges", edges.fields))
//    }
//
//    fun nodes (body: Repository.() -> Unit) {
//        val repo = Repository(nextLevel())
//        repo.body()
//        addField(Node("nodes", repo.fields))
//    }
//
//}
//
//
//class RepositoryEdge(override val level: Int): Edges(level) {
//    fun node (body: Repository.() -> Unit) {
//        val repo = Repository(nextLevel())
//        repo.body()
//        addField(Node(fields = repo.fields))
//    }
//}
