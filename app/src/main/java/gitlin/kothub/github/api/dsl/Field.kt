package gitlin.kothub.github.api.dsl


open class Field(val name: String) {

    var level: Int = 0

    override fun toString(): String {
        return "$name "
    }

    open fun prettyPrint(): String {
        val numberOfSpaces = level * 2
        val spaces = (1..numberOfSpaces).fold("") { acc, _ -> acc + " "}

        return "$spaces$name\n"
    }
}
