package gitlin.kothub.github.api.dsl


interface ActorFields: Element {
    val avatarUrl: Unit
        get() = addField("avatarUrl")

    val login: Unit
        get() = addField("login")

    val resourcePath: Unit
        get() = addField("resourcePath")

    val url: Unit
        get() = addField("url")
}

class Actor(override val level: Int): ActorFields {
    override val fields = arrayListOf<Field>()
}
