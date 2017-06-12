package gitlin.kothub.github.api.dsl


enum class IssueState (val value: String){
    OPEN("OPEN"),
    CLOSED("CLOSED")
}


class Issue(override val level: Int): CommentFields {

    override val fields = arrayListOf<Field>()

    fun comments(first: Variable<Int>? = null, body: Connection<IssueComment>.() -> Unit) {
        val connection = Connection<IssueComment>(nextLevel())
        connection.body()
        addField(Node("comments", connection.fields, variables("first" to first)))
    }

}

