package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.byNullableString
import com.github.salomonbrys.kotson.byString
import com.github.salomonbrys.kotson.get
import com.google.gson.JsonObject


data class DrawerInfo(private val json: JsonObject) {

    val avatarUrl: String by json.byString
    val login: String by json.byString
    val email: String? by json.byNullableString
    val name: String? by json.byNullableString

    val issues: Int = json["repositories"]["nodes"].array.map {
        it["issues"]["totalCount"].asInt
    }.sum()
}



