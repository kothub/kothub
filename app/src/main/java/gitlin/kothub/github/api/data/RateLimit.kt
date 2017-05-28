package gitlin.kothub.github.api.data

import gitlin.kothub.utilities.getValue
import org.json.JSONObject


abstract class RateLimit(json: JSONObject) {
    val remaining: Int? by json
    val cost: Int? by json
    val limit: Int? by json
    val resetAt: String? by json
}