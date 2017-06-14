package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.byInt
import com.github.salomonbrys.kotson.byString
import com.google.gson.JsonObject


data class RateLimit(private val json: JsonObject) {
    val remaining: Int by json.byInt
    val cost: Int by json.byInt
    val limit: Int by json.byInt
    val resetAt: String? by json.byString
}