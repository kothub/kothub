package gitlin.kothub.github.api.dsl


open class Field(val name: String) {

    var level: Int = 0

    override fun toString(): String {
        val spaces = (0..level).fold("") { acc, _ -> acc + "  "}
        return "$spaces$name\n"
    }
}
