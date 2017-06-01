package gitlin.kothub.utilities

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import gitlin.kothub.R


fun SharedPreferences.Editor.set(pair: Pair<String, Any?>) {
    val (first, second) = pair
    when (second) {
        null -> remove(first)
        is String -> putString(first, second)
        is Boolean -> putBoolean(first, second)
        is Float -> putFloat(first, second)
        is Int -> putInt(first, second)
        is Long -> putLong(first, second)
    }
}

fun Activity.editSharedPreferences(file: String, body: SharedPreferences.Editor.() -> Unit) {
    val sharedPref = getSharedPreferences(file, Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    body(editor)
    editor.commit()
}

fun Activity.getOAuthToken() =
        getSharedPreferences(getString(R.string.github_preferences_file), Context.MODE_PRIVATE)
        .getString(getString(R.string.oauth_github_token), null)

fun Context.getAlarmManager() = getSystemService(Context.ALARM_SERVICE) as AlarmManager