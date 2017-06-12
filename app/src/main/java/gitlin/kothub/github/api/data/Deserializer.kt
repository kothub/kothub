package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonArray
import com.google.gson.JsonObject


interface Deserializer<T> {
    fun fromJson (json: JsonObject): T
    fun fromJson (json: JsonArray): List<T> {
        return json.map { fromJson(it.obj) }
    }
}