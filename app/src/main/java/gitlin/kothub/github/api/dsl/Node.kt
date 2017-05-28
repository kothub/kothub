package gitlin.kothub.github.api.dsl

class Node(name: String = "node", val fields: MutableList<Field>): Field(name) {

    override fun toString(): String {
        return "$name{" + fields.fold("") { acc, value -> acc + value.toString() } + "}"
    }

    override fun prettyPrint(): String {
        val numberOfSpaces = level * 2
        val spaces = (1..numberOfSpaces).fold("") { acc, _ -> acc + " "}

        var result = "$spaces$name {\n"

        fields.forEach { field ->
            result += field.prettyPrint()
        }

        result += "$spaces}\n"
        return result
    }
}