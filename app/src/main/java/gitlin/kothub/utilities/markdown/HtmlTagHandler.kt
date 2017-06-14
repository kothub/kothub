package gitlin.kothub.utilities.markdown

import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.TypefaceSpan
import android.util.Log
import org.xml.sax.XMLReader
import java.util.*
import kotlin.reflect.KClass


class HtmlList {

    fun append(output: Editable) {
        output.append('\u2022')
        output.append(' ').append(' ')
    }

    fun newLine(output: Editable) {
        output.append("\n")
    }
}

class HtmlTagHandler : Html.TagHandler {

    companion object {
        val TAG_UL = "ul"
        val TAG_LI = "li"
        val TAG_CODE = "code"
    }

    val list = LinkedList<HtmlList>()

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {

        Log.i("Markdown", tag)

        when (tag) {
            TAG_UL ->
                if (opening) {
                    handleUlStart()
                }
                else {
                    handleUlEnd()
                }

            TAG_LI ->
                if (opening) {
                    handleLiStart(output)
                }
                else {
                    handleLiEnd(output)
                }

            TAG_CODE ->
                if (opening) {
                    startSpan(TypefaceSpan("monospace"), output)
                }
                else {
                    endSpan(TypefaceSpan::class, output)
                }

        }
    }

    private fun handleUlStart () {
        list.addFirst(HtmlList())
    }

    private fun handleUlEnd () {
        list.removeFirst()
    }

    private fun handleLiStart (output: Editable) {
        list.first.append(output)
    }

    private fun handleLiEnd(output: Editable) {
        list.first.newLine(output)
    }

    private fun getLast(text: Spanned, kind: KClass<*>): Any? {
        val spans = text.getSpans(0, text.length, kind.java)
        return if (spans.size > 0) spans[spans.size - 1] else null
    }

    private fun startSpan(span: Any, output: Editable) {
        val length = output.length
        output.setSpan(span, length, length, Spanned.SPAN_MARK_MARK)
    }

    private fun endSpan(type: KClass<*>, output: Editable) {

        val length = output.length
        val span = getLast(output, type)
        val start = output.getSpanStart(span)
        output.removeSpan(span)

        if (start != length) {
            output.setSpan(span, start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}