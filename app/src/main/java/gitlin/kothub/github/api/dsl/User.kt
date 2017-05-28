package gitlin.kothub.github.api.dsl


class User(override val level: Int): ActorFields {

    override val fields = arrayListOf<Field>()

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
            Node("contributedRepositories(${args(pagination, arg("privacy", privacy))})", connection.fields)
        )
    }
}