package gitlin.kothub.github.api.dsl


class Language(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val color: Unit get() = addField("color")
    val id: Unit get() = addField("id")
    val name: Unit get() = addField("name")

}

