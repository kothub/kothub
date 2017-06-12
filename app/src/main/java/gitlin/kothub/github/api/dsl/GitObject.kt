package gitlin.kothub.github.api.dsl




class GitObject(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val oid: Unit get() = addField("oid")
}


