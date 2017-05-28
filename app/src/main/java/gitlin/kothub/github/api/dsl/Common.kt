package gitlin.kothub.github.api.dsl

fun paginationData(
        first: Int,
        after: String? = null,
        last: Int? = null,
        before: String? = null
): String {

    var result = "first: $first"
    if (after != null) {
        result += ", after: $after"
    }

    if (last != null) {
        result += ", last: $last"
    }

    if (before != null) {
        result += ", before: $before"
    }

    return result
}

fun arg(key: String, value: Any?): String? {

    return if (value == null) null else "$key: $value"
}

fun args(vararg values: String?): String {

    return values.fold("") { acc, value ->
        if (value != null) {
            return if (acc.isEmpty()) value else "$acc, $value"
        }
        else {
            return acc
        }
    }
}