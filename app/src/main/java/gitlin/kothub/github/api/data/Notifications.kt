package gitlin.kothub.github.api.data

import gitlin.kothub.utilities.getValue
import gitlin.kothub.utilities.obj
import org.json.JSONObject
import org.json.JSONArray

import gitlin.kothub.utilities.*

data class Notifications(private val json: JSONArray) {
    val x = json.map<JSONObject, Notification> { Notification(it) }
}

data class Notification(val json: JSONObject) {
    val id: String? by json

    val repository = Repository(json.obj("repository"))
    val subject = Subject(json.obj("subject"))

    val reason: String? by json
    val unread: Boolean? by json
    val updated_at: String? by json
    val last_read_at: String? by json
    val url: String? by json
}

data class Repository(val json: JSONObject?) {
    val name: String? by json
    val full_name: String? by json
    val description: String? by json
    val private: Boolean? by json
    val fork: Boolean? by json
    val url: String? by json
    val html_url: String? by json
}

data class Subject(val json: JSONObject?) {
    val title: String? by json
    val url: String? by json
    val latest_comment_url: String? by json
    val type: String? by json
}