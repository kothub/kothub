package gitlin.kothub.github.api.dsl



class Blob(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val text: Unit get() = addField("text")
}


