package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.byString
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

private val gson = Gson()

data class Feeds(private val json: String) {
    private val obj: JsonObject = JsonParser().parse(json).asJsonObject

    val timeline_url: String by obj.byString
    val user_url: String by obj.byString
    val current_user_public_url: String by obj.byString

//    private val links = json.obj("_links")!!
//    private val timeline = links.obj("timeline")!!
//    val href: String? by timeline
}
