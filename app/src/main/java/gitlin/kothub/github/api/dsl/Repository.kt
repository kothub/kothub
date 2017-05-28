package gitlin.kothub.github.api.dsl


class Repository(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    fun issues(first: Int = 10, body: IssueConnection.() -> Unit) {
        val connection = IssueConnection(nextLevel())
        connection.body()

        val pagination = paginationData(first)
        addField(Node("issues($pagination)", connection.fields))
    }
}