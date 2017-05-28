package gitlin.kothub.github.api.dsl


abstract class Connection<out T>(override val level: Int) : Element {

    override val fields = arrayListOf<Field>()

    val totalCount: Unit
        get() = addField("totalCount")

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