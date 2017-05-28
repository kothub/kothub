package gitlin.kothub.github.api.dsl

class Node(name: String = "node", val fields: MutableList<Field>): Field(name) {
    override fun toString(): String {
        val spaces = (0..level).fold("") { acc, _ -> acc + "  "}

        var result = "$spaces$name {\n"

        fields.forEach { field ->
            result += field.toString()
        }

        result += "}\n"
        return result
    }
}