package gitlin.kothub.github.api.data

import gitlin.kothub.utilities.*
import org.json.JSONArray
import org.json.JSONObject


data class UserSummary(private val json: JSONObject): RateLimit(json.obj("rateLimit")!!) {

    private val viewer = json.obj("viewer")!!

    val avatarUrl: String? by viewer
    val login: String? by viewer
    val name: String? by viewer
    val websiteUrl: String? by viewer
    val bio: String? by viewer
    val url: String? by viewer
    val company: String? by viewer
    val location: String? by viewer
    //val followers: Int? by viewer.obj("totalCount")
    val followers : Int? = totalCount(viewer, "followers")
    val following : Int? = totalCount(viewer, "following")
    val starredRepositories : Int? = totalCount(viewer, "starredRepositories")
    val repositories : Int? = totalCount(viewer, "repositories")
    val pinnedRepositories =
            viewer.obj("pinnedRepositories")?.arr("nodes")?.map<JSONObject, PinnedRepository> { PinnedRepository(it) }
            ?: arrayListOf()

    fun totalCount(jsonObj: JSONObject, name: String): Int? = jsonObj.obj(name)?.getInt("totalCount")
}


data class PinnedRepository(val json: JSONObject) {
    val name: String? by json
    val description: String? by json
}

