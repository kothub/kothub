package gitlin.kothub.github.api.dsl


typealias GFragment<T> = T.() -> Unit
inline fun <reified T: Element> createFragment (noinline body: T.() -> Unit) = body
inline fun <reified T: Element> T.fragment(body: T.() -> Unit) = this.body()

