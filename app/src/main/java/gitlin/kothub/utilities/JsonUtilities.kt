package gitlin.kothub.utilities

import com.github.kittinunf.fuel.android.core.Json
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KProperty

operator fun <T> JSONObject.getValue(thisRef: Any, prop: KProperty<*>): T? {
    if (this.isNull(prop.name)) {
        return null
    }
    else {
        return this.get(prop.name) as T?
    }
}

operator fun JSONObject.contains(property: String) = this.has(property)

fun JSONObject.obj(property: String): JSONObject? = if (property in this) this.getJSONObject(property) else null
fun JSONObject.arr(property: String): JSONArray? = if (property in this) this.getJSONArray(property) else null


fun <T, V> JSONArray.map(mapper: (T) -> V): MutableList<V> {
    return (0..length() - 1).mapTo(arrayListOf<V>()) { mapper(get(it) as T) }
}

operator fun JSONArray.iterator(): Iterator<JSONObject>
        = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()


operator fun <T> JsonObject.getValue(thisRef: Any, prop: KProperty<*>): T? {
    if (prop.name == null) {
        return null
    }
    else {
        return this.get(prop.name) as T?
    }
}

fun <T, V> JsonArray.map(mapper: (T) -> V): MutableList<V> {
    return (0..size() - 1).mapTo(arrayListOf<V>()) { mapper(get(it) as T) }
}

operator fun JsonArray.iterator(): Iterator<JsonObject>
        = (0 until size()).asSequence().map { get(it) as JsonObject }.iterator()

fun JSONObject.totalCount(name: String): Int? = obj(name)?.getInt("totalCount")