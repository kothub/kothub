package gitlin.kothub.github.api.data

import gitlin.kothub.utilities.*
import org.json.JSONObject

data class Feeds(private val json: JSONObject) {
    val timeline_url: String? by json
    val user_url: String? by json
    val current_user_public_url: String? by json

    private val links = json.obj("_links")!!
    private val timeline = links.obj("timeline")!!
    val href: String? by timeline
}
