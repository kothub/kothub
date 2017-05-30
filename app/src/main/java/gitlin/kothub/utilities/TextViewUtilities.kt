package gitlin.kothub.utilities

import android.widget.TextView

fun TextView.setText(value: Int?) {
    text = value?.toString() ?: ""
}

var TextView.value: Any?
    get() = text
    set(value) {
        text = value?.toString() ?: ""
    }
