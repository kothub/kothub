package gitlin.kothub.github.api.dsl


open class Variable<T: Any>(val name: String, val default: T? = null) {

    var key: String = ""
    var value: T? = default

    fun bind(value: T) {
        this.value = value
    }

    open fun isValue() = false

    fun isBound() = value != null
}

class Value<T: Any>(value: T): Variable<T>("value", value) {

    override fun isValue() = true
}

fun <T: Any> value(value: T) = Value(value)
fun <T: Any> variable(name: String, default: T? = null) = Variable(name, default)

fun variables(vararg variables: Pair<String, Variable<*>?>): List<Variable<Any>> {
    return variables.map {
        (key, variable) ->
            if (variable != null) {
                variable.key = key
            }

            if (variable == null) {
                variable
            }
            else {
                variable as Variable<Any>
            }
    }.filterNotNull()
}