package gitlin.kothub.github.api.dsl


interface CommentFields : Element {

    fun author (body: Actor.() -> Unit) {
        val actor = Actor(nextLevel())
        actor.body()
        addField(Node("author", actor.fields))
    }

    val body: Unit
        get() = addField("body")

    val bodyHTML: Unit
        get() = addField("bodyHTML")

    val createdAt: Unit
        get() = addField("createdAt")

    val createdViaEmail: Unit
        get() = addField("createdViaEmail")
}