package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.*
import com.google.gson.*

import gitlin.kothub.utilities.*

data class Notifications(private val json: JsonArray) {
    val notifications: List<Notification> = json.map { Notification(it.asJsonObject) }
}

data class Notification(private val json: JsonObject) {
    var id: String by json.byString

    val repository: Repository = Repository(json)
    val subject: Subject = Subject(json)

    val reason: String by json.byString
    val unread: Boolean by json.byBool
    val updated_at: String by json.byString
    val last_read_at: String by json.byString
    val url: String by json.byString
}

data class Repository(private val json: JsonObject) {
    private val repository: JsonObject = json.getAsJsonObject("repository")

    val name: String by repository.byString
    val full_name: String by repository.byString
    val description: String by repository.byString
    val private: Boolean by repository.byBool
    val fork: Boolean by repository.byBool
    val url: String by repository.byString
    val html_url: String by repository.byString
}

data class Subject(private val json: JsonObject) {
    private val subject: JsonObject = json.getAsJsonObject("subject")

    val title: String by subject.byString
    val url: String by subject.byString
    val latest_comment_url: String by subject.byString
    val type: String by subject.byString
}