package gitlin.kothub.github.api.dsl

import android.util.Log

fun paginationData(
        first: Int? = null,
        after: String? = null,
        last: Int? = null,
        before: String? = null
): String? {

    val result = arrayListOf<String>()

    if (first != null) {
        result.add("first: $first")
    }

    if (after != null) {
        result.add("after: $after")
    }

    if (last != null) {
        result.add("last: $last")
    }

    if (before != null) {
        result.add("before: $before")
    }

    return if (result.isEmpty()) null else result.joinToString(", ")

}

fun arg(key: String, value: Any?): String? {

    return if (value == null) null else "$key: $value"
}

fun args(vararg values: String?): String {

    val arguments = values.fold("") { acc, value ->
        if (value != null) {
            if (acc.isEmpty()) value else "$acc, $value"
        } else {
            acc
        }
    }

    return if (arguments.isEmpty()) "" else "($arguments)"
}