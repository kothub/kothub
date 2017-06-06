package gitlin.kothub.github.api.dsl

import kotlin.reflect.full.primaryConstructor

@DslMarker
annotation class ElementMarker

@ElementMarker
interface Element {
    val fields: MutableList<Field>
    val level: Int

    fun nextLevel () = level + 1

    fun addField(field: Field) {
        field.level = nextLevel()
        fields.add(field)
    }

    fun addField(field: String) {
        addField(Field(field))
    }

}

inline fun <reified T : Element> Element.on (body: T.() -> Unit) {
    val constructor = T::class.primaryConstructor
    val t = constructor!!.call(nextLevel())
    t.body()
    addField(Node("... on ${T::class.simpleName}", t.fields))
}
