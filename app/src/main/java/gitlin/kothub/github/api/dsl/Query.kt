package gitlin.kothub.github.api.dsl


class Query(withRateLimit: Boolean = true): Element {

    override val level = 0
    override val fields = arrayListOf<Field>()

    init {
        if (withRateLimit) {
            val rateLimitFields = arrayListOf(
                Field("remaining"),
                Field("cost"),
                Field("limit"),
                Field("resetAt")
            )

            val rateLimit = Node("rateLimit", rateLimitFields)
            addField(rateLimit)
        }
    }

    fun repository(owner: String, name: String, body: Repository.() -> Unit) {
        val repo = Repository(nextLevel())
        repo.body()
        addField(Node("repository(owner: \"$owner\", name: \"$name\")", repo.fields))
    }

    fun viewer(body: User.() -> Unit) {
        val user = User(nextLevel())
        user.body()
        addField(Node("viewer", user.fields))
    }



    fun prettyPrint(): String {
        return "query {\n" + fields.fold("") { acc, value -> acc + value.prettyPrint() } + "}"
    }

    override fun toString(): String {
        return "query{" + fields.fold("") { acc, value -> acc + value.toString() } + "}"
    }
}


fun query (withRateLimit: Boolean = true, body: Query.() -> Unit): Query {
    val query = Query()
    query.body()
    return query
}