package gitlin.kothub.github.api.dsl


class PageInfo(override val level: Int): Element {
    override val fields = arrayListOf<Field>()

    val hasNextPage: Unit
        get() = addField("hasNextPage")

    val hasPreviousPage: Unit
        get() = addField("hasPreviousPage")

    val startCursor: Unit
        get() = addField("startCursor")

    val endCursor: Unit
        get() = addField("endCursor")

}

abstract class Connection<out T>(override val level: Int) : Element {

    override val fields = arrayListOf<Field>()

    val totalCount: Unit
        get() = addField("totalCount")

    fun pageInfo (body: PageInfo.() -> Unit) {
        val info = PageInfo(nextLevel())
        info.body()
        addField(Node("pageInfo", info.fields))
    }

    override fun toString(): String {
        return fields.fold("") { acc, field ->
            acc + field.toString()
        }
    }

    fun prettyPrint(): String {
        return fields.fold("") { acc, field ->
            acc + field.prettyPrint()
        }
    }
}