package gitlin.kothub.utilities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import gitlin.kothub.R



fun Activity.editSharedPreferences(body: SharedPreferences.Editor.() -> Unit) {
    val sharedPref = getPreferences(Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    body(editor)
    editor.commit()
}

fun Activity.editSharedPreferences(vararg pairs: Pair<String, Any>) {
    val sharedPref = getPreferences(Context.MODE_PRIVATE)
    val editor = sharedPref.edit()


    pairs.forEach {
        val (first, second) = it
        when (second) {
            null -> editor.remove(first)
            is String -> editor.putString(first, second)
            is Boolean -> editor.putBoolean(first, second)
            is Float -> editor.putFloat(first, second)
            is Int -> editor.putInt(first, second)
            is Long -> editor.putLong(first, second)
        }
    }


    editor.commit()
}