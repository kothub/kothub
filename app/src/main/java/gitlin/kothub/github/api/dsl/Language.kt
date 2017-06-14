package gitlin.kothub.github.api.dsl




class Language(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val color: Unit get() = addField("color")
    val id: Unit get() = addField("id")
    val name: Unit get() = addField("name")

}

class LanguageEdges(override val level: Int): Edges(level) {
    fun node (body: Language.() -> Unit) {
        val language = Language(nextLevel())
        language.body()
        addField(Node(fields = language.fields))
    }
}


class LanguageConnection(override val level: Int) : Connection<Language>(level) {
    fun edges (body: LanguageEdges.() -> Unit) {
        val edges = LanguageEdges(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }
}


