package gitlin.kothub.github.api.dsl


class Organization(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val name: Unit get() = addField("name")
    val avatarUrl: Unit get() = addField("avatarUrl")
}


