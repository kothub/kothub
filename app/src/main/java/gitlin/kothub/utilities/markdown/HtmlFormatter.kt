package gitlin.kothub.utilities.markdown

import android.util.Log


val START_A = "<a"
val END_A = "</a>"
val START_HREF = "href=\""
val END_HREF = "\""

// TODO: comment, refactors, this is just testing for now.

fun formatReadme (html: String, nameWithOwner: String, branch: String): String {


    val builder = StringBuilder(html)

    replaceRelativeHrefs(builder, nameWithOwner, branch)

    return builder.toString()
}

fun replaceRelativeHrefs (builder: StringBuilder, nameWithOwner: String, branch: String) {

    var startA = builder.indexOf(START_A)

    val GITHUB_URL = "https://github.com/$nameWithOwner/blob/$branch"

    while (startA != -1) {
        var endA = builder.indexOf(END_A, startA + START_A.length)

        if (endA == -1) {
            break
        }

        val startHref = builder.indexOf(START_HREF, startA)
        if (startHref < endA && startHref != -1) {
            val endHref = builder.indexOf(END_HREF, startHref + START_HREF.length)

            if (endHref < endA && endHref != -1) {
                Log.i("HTML startHref", startHref.toString())
                Log.i("HTML endHref", endHref.toString())
                val url = builder.substring(startHref + START_HREF.length, endHref)
                if (isRelativeUrl(url)) {
                    builder.replace(startHref, endHref + 1, "href=\"${absoluteUrl(GITHUB_URL, url)}\"")
                }
            }
        }

        startA = builder.indexOf(START_A, endA)
    }

}

fun isRelativeUrl (url: String): Boolean {
    return (url.startsWith(".") || url.startsWith("/"))
            && !url.contains("http")
            && !url.contains("www")
}

fun absoluteUrl (prefix: String, url: String): String {
    if (url.startsWith(".")) {
        return prefix + url.substring(1)
    }
    else {
        return prefix + url
    }
}