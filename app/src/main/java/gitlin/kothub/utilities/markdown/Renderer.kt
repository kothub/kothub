package gitlin.kothub.utilities.markdown

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.TextView
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

val PAGE_START =
//            <!DOCTYPE html><html lang="en">
//            <head> <title></title>
//                <meta charset="UTF-8">
//                <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"/>
        """
                <link href="github_.css\" rel=\"stylesheet\">
        """
//                <style>
//
//                </style>
//            </head>
val PAGE_END = "</html>"

fun renderMarkdown (context: Context, markdown: String, nameWithOwner: String, branch: String): Spanned? {

    val flavour = GFMFlavourDescriptor()
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
    var html = HtmlGenerator(markdown, parsedTree, flavour).generateHtml()

    html = formatReadme(html, nameWithOwner, branch)

//    val imageGetter = UrlImageParser(view, context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(PAGE_START + html, Html.FROM_HTML_MODE_COMPACT, null, HtmlTagHandler())
    }
    else {
        Log.i("Markdown", PAGE_START + html)
        return Html.fromHtml(PAGE_START + html, null, HtmlTagHandler())
    }
}