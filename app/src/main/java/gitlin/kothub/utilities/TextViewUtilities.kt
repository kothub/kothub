package gitlin.kothub.utilities

import android.content.Context
import android.widget.TextView

fun TextView.setText(value: Int?) {
    text = value?.toString() ?: ""
}
