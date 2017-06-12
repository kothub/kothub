package gitlin.kothub.github.api.dsl

class Node(name: String = "node", val fields: MutableList<Field>, val variables: List<Variable<Any>> = arrayListOf()): Field(name) {

    override fun toString(): String {
        return "$name${parameters()}{" + fields.fold("") { acc, value -> acc + value.toString() } + "}"
    }

    override fun prettyPrint(): String {
        val numberOfSpaces = level * 2
        val spaces = (1..numberOfSpaces).fold("") { acc, _ -> acc + " "}

        var result = "$spaces$name${parameters()} {\n"

        fields.forEach { field ->
            result += field.prettyPrint()
        }

        result += "$spaces}\n"
        return result
    }

    private fun parameters (): String {
        val strings = variables.map { arg(it.key, it) }
        return args(strings)
    }

    private fun arg(key: String, variable: Variable<Any>): String? {

        if (variable.isValue()) {
            val value: Any? = variable.value
            return when (value) {
                null -> null
                is String -> "$key: \\\"$value\\\""
                else -> "$key: $value"
            }
        }

        return "$key: $${variable.name}"
    }

    private fun args(values: List<String?>): String {

        val arguments = values.fold("") { acc, value ->
            if (value != null) {
                if (acc.isEmpty()) value else "$acc, $value"
            } else {
                acc
            }
        }

        return if (arguments.isEmpty()) "" else "($arguments)"
    }

}