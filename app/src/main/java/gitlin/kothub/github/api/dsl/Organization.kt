package gitlin.kothub.github.api.dsl


class Organization(override val level: Int): Element {

    override val fields = arrayListOf<Field>()

    val name: Unit get() = addField("name")
    val avatarUrl: Unit get() = addField("avatarUrl")


}

class OrganizationEdges(override val level: Int): Edges(level) {
    fun node (body: Organization.() -> Unit) {
        val org = Organization(nextLevel())
        org.body()
        addField(Node(fields = org.fields))
    }
}


class OrganizationConnection(override val level: Int) : Connection<Organization>(level) {
    fun edges (body: OrganizationEdges.() -> Unit) {
        val edges = OrganizationEdges(nextLevel())
        edges.body()
        addField(Node("edges", edges.fields))
    }

    fun nodes (body: Organization.() -> Unit) {
        val org = Organization(nextLevel())
        org.body()
        addField(Node("nodes", org.fields))
    }

}


