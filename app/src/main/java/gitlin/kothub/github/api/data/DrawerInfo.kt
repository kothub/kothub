package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.*
import com.google.gson.JsonObject


data class DrawerInfo(private val json: JsonObject) {

    val avatarUrl: String by json.byString
    val login: String by json.byString
    val email: String? = json["email"].nullString
    val name: String? = json["name"].nullString

    val issues: Int = json["repositories"]["nodes"].array.map {
        it["issues"]["totalCount"].asInt
    }.sum()
}



