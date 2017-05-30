package gitlin.kothub.github.api.data

import gitlin.kothub.utilities.*
import org.json.JSONArray
import org.json.JSONObject


data class DrawerInfo(private val viewer: JSONObject?) {

    val avatarUrl: String? by viewer
    val login: String? by viewer
    val email: String? by viewer
    val name: String? by viewer

    val issues: Int =
            viewer?.obj("repositories")?.arr("nodes")
                ?.map<JSONObject, Int> {
                    it.totalCount("issues") ?: 0
                }?.sum() ?: 0
}



