package gitlin.kothub.utilities

import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KProperty

operator fun <T> JSONObject.getValue(thisRef: Any, prop: KProperty<*>): T {
    return this.get(prop.name) as T
}

operator fun JSONObject.contains(property: String) = this.has(property)

fun JSONObject.obj(property: String): JSONObject? = if (property in this) this.getJSONObject(property) else null
fun JSONObject.arr(property: String): JSONArray? = if (property in this) this.getJSONArray(property) else null


fun <T, V> JSONArray.map(mapper: (T) -> V): MutableList<V> {
    return (0..length() - 1).mapTo(arrayListOf<V>()) { mapper(get(it) as T) }
}