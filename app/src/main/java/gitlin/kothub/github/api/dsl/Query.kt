package gitlin.kothub.github.api.dsl


class Query: Element {

    override val level = 0
    override val fields = arrayListOf<Field>()

    fun repository(owner: String, name: String, body: Repository.() -> Unit) {
        val repo = Repository(nextLevel())
        repo.body()
        addField(Node("repository(owner: $owner, name: $name)})", repo.fields))
    }

    fun viewer(body: User.() -> Unit) {
        val user = User(nextLevel())
        user.body()
        addField(Node("viewer", user.fields))
    }
}


fun query (body: Query.() -> Unit): Query {
    val query = Query()
    query.body()
    return query
}