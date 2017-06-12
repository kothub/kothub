package gitlin.kothub.utilities

import android.os.Handler

fun delay (ms: Long, body: () -> Unit) {
    Handler().postDelayed(body, ms)
}