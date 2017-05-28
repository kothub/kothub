package gitlin.kothub.github.api.dsl

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