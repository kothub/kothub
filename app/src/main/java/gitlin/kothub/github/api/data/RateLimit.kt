package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.*


data class RateLimit(
        val remaining: Int,
        val cost: Int,
        val limit: Int,
        val resetAt: String
) {

    companion object: Deserializer<RateLimit> {

        private val gson = Gson()

        override fun fromJson(json: JsonArray): List<RateLimit> {
            return json.map {
                fromJson(it.asJsonObject)
            }
        }

        override fun fromJson(json: JsonObject): RateLimit {
            return gson.fromJson<RateLimit>(json)
        }
    }
}