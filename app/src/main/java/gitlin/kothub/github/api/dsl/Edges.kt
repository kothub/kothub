package gitlin.kothub.github.api.dsl

import kotlin.reflect.full.primaryConstructor

//abstract class Edges(override val level: Int): Element {
//
//    override val fields = arrayListOf<Field>()
//
//    val cursor: Unit
//        get() = addField("cursor")
//}


class Edges<T: Element>(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val cursor: Unit get() = addField("cursor")
}

inline fun <reified T: Element> Edges<T>.node (body: T.() -> Unit) {
    val constructor = T::class.primaryConstructor
    val t = constructor!!.call(nextLevel())
    t.body()
    addField(Node(fields = t.fields))
}
