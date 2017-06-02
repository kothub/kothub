package gitlin.kothub.github.api.dsl

import kotlin.reflect.KClass

abstract class Edges(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val cursor: Unit
        get() = addField("cursor")
}

