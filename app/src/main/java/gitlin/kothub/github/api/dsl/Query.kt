package gitlin.kothub.github.api.dsl

enum class Type(val type: String) {
    INT("Int!"),
    STRING("String!");
    // Add more here...

    private fun default (default: Any?): String {
        return if (default == null) "" else " = $default"
    }

    operator fun invoke(key: String, default: Any? = null): String = "$$key: $type${default(default)}"
}

class Query(val variablesDeclarations: List<String>, withRateLimit: Boolean = true): Element {
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

    fun repository(owner: Variable<String>, name: Variable<String>, body: Repository.() -> Unit) {
        val repo = Repository(nextLevel())
        repo.body()
        addField(Node("repository", repo.fields, variables("owner" to owner, "name" to name)))
    }

    fun user(login: Variable<String>, body: User.() -> Unit) {
        val user = User(nextLevel())
        user.body()
        addField(Node("user", user.fields, variables("login" to login)))
    }

    fun viewer(body: User.() -> Unit) {
        val user = User(nextLevel())
        user.body()
        addField(Node("viewer", user.fields))
    }


    fun prettyPrint(): String {
        return "query${queryArgs()} {\n" + fields.fold("") { acc, value -> acc + value.prettyPrint() } + "}"
    }

    override fun toString(): String {
        return "query${queryArgs()}{" + fields.fold("") { acc, value -> acc + value.toString() } + "}"
    }

    fun build(): String = toString()

    private fun queryArgs (): String {
        if (variablesDeclarations.isEmpty()) {
            return ""
        }

        return "(${variablesDeclarations.joinToString(", ")})"
    }
}



fun query (vararg variables: String = arrayOf(), withRateLimit: Boolean = true, body: Query.() -> Unit): String {

    val query = Query(variables.toList(), withRateLimit)
    query.body()
    return query.toString()
}

