package gitlin.kothub.github.api.dsl



class User(override val level: Int): ActorFields {

    override val fields = arrayListOf<Field>()

    val bio: Unit get() = addField("bio")
    val company: Unit get() = addField("company")
    val location: Unit get() = addField("location")
    val name: Unit get() = addField("name")
    val websiteUrl: Unit get() = addField("websiteUrl")
    val email: Unit get() = addField("email")

    fun followers(first: Variable<Int>? = null, body: Connection<User>.() -> Unit) {

        val connection = Connection<User>(nextLevel())
        connection.body()
        addField(
            Node(
                "followers",
                connection.fields,
                variables("first" to first)
            )
        )
    }

    fun following(first: Variable<Int>? = null, body: Connection<User>.() -> Unit) {

        val connection = Connection<User>(nextLevel())
        connection.body()

        addField(
            Node(
                "following",
                connection.fields,
                variables("first" to first)
            )
        )
    }

    fun starredRepositories(first: Variable<Int>? = null, body: Connection<User>.() -> Unit) {

        val connection = Connection<User>(nextLevel())
        connection.body()
        addField(
            Node("starredRepositories", connection.fields, variables("first" to first))
        )
    }

    fun repositories(first: Variable<Int>? = null, body: Connection<Repository>.() -> Unit) {

        val connection = Connection<Repository>(nextLevel())
        connection.body()
        addField(
            Node("repositories", connection.fields, variables("first" to first))
        )
    }

    fun contributedRepositories (
            first: Variable<Int>? = null,
            after: Variable<String>? = null,
            last: Variable<Int>? = null,
            before: Variable<String>? = null,
            privacy: Variable<RepositoryPrivacy>? = null,
            body: Connection<Repository>.() -> Unit
            // TODO: order
            // TODO: affiliations
            // TODO: isLocked
    ) {
        val connection = Connection<Repository>(nextLevel())
        connection.body()
        addField(
            Node(
                "contributedRepositories",
                connection.fields,
                variables("first" to first, "after" to after, "last" to last, "before" to before, "privacy" to privacy)
            )
        )
    }

    fun pinnedRepositories (
            first: Variable<Int>? = null,
            after: Variable<String>? = null,
            last: Variable<Int>? = null,
            before: Variable<String>? = null,
            privacy: Variable<RepositoryPrivacy>? = null,
            body: Connection<Repository>.() -> Unit
            // TODO: order
            // TODO: affiliations
            // TODO: isLocked
    ) {
        val connection = Connection<Repository>(nextLevel())
        connection.body()

        addField(
            Node(
                "pinnedRepositories",
                connection.fields,
                variables("first" to first, "after" to after, "last" to last, "before" to before, "privacy" to privacy)
            )
        )
    }

    fun organizations (
            first: Variable<Int>? = null,
            after: Variable<String>? = null,
            last: Variable<Int>? = null,
            before: Variable<String>? = null,
            body: Connection<Organization>.() -> Unit
    ) {
        val connection = Connection<Organization>(nextLevel())
        connection.body()

        addField(
            Node(
                "organizations", connection.fields,
                variables("first" to first, "after" to after, "last" to last, "before" to before)
            )
        )
    }
}

