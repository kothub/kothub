package gitlin.kothub.utilities

import android.widget.TextView

var TextView.value: Any?
    get() = text
    set(value) {
        text = value?.toString() ?: ""
    }
