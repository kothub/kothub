package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.*
import com.google.gson.JsonObject


data class DrawerInfo(val issues: Int) {

    companion object: Deserializer<DrawerInfo> {
        override fun fromJson(json: JsonObject): DrawerInfo {
            val issues: Int = json["repositories"]["nodes"].array.map {
                it["issues"]["totalCount"].asInt
            }.sum()

            return DrawerInfo(issues)
        }

    }

}



