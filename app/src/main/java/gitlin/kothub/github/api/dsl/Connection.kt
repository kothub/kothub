package gitlin.kothub.github.api.dsl

import kotlin.reflect.full.primaryConstructor


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

open class Connection<out T>(override val level: Int) : Element {

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

inline fun <reified T: Element> Connection<T>.edges (body: Edges<T>.() -> Unit) {
    val edges = Edges<T>(nextLevel())
    edges.body()
    addField(Node("edges", edges.fields))
}

inline fun <reified T: Element> Connection<T>.nodes (body: T.() -> Unit) {
    val constructor = T::class.primaryConstructor
    val t = constructor!!.call(nextLevel())
    t.body()
    addField(Node("nodes", t.fields))
}

