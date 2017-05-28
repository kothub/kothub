package gitlin.kothub.github.api.dsl

import android.util.Log


class User(override val level: Int): ActorFields {

    override val fields = arrayListOf<Field>()

    val bio: Unit get() = addField("bio")
    val company: Unit get() = addField("company")
    val location: Unit get() = addField("location")
    val name: Unit get() = addField("name")
    val websiteUrl: Unit get() = addField("websiteUrl")

    fun followers(first: Int? = null, body: UserConnection.() -> Unit) {

        val connection = UserConnection(nextLevel())
        connection.body()

        val pagination = paginationData(first)
        addField(
            Node("followers${args(pagination)}", connection.fields)
        )
    }


    fun contributedRepositories (
            first: Int,
            after: String? = null,
            last: Int? = null,
            before: String? = null,
            privacy: RepositoryPrivacy? = null,
            body: RepositoryConnection.() -> Unit
            // TODO: order
            // TODO: affiliations
            // TODO: isLocked
    ) {
        val connection = RepositoryConnection(nextLevel())
        connection.body()

        val pagination = paginationData(first, after, last, before)
        addField(
            Node("contributedRepositories${args(pagination, arg("privacy", privacy.toString()))}", connection.fields)
        )
    }

    fun pinnedRepositories (
            first: Int?,
            after: String? = null,
            last: Int? = null,
            before: String? = null,
            privacy: RepositoryPrivacy? = null,
            body: RepositoryConnection.() -> Unit
            // TODO: order
            // TODO: affiliations
            // TODO: isLocked
    ) {
        val connection = RepositoryConnection(nextLevel())
        connection.body()

        val pagination = paginationData(first, after, last, before)
        Log.d("PAGINATION", pagination)
        Log.d("PAGINATION?", args(pagination, arg("privacy", privacy)))
        addField(
            Node("pinnedRepositories${args(pagination, arg("privacy", privacy))}", connection.fields)
        )
    }
}


class UserConnection(override val level: Int) : Connection<User>(level) {
    fun edges (body: UserEdge.() -> Unit) {
        val edges = UserEdge(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}


class UserEdge(override val level: Int): Edges(level) {
    fun node (body: User.() -> Unit) {
        val issue = User(nextLevel())
        issue.body()
        addField(Node(fields = issue.fields))
    }
}
