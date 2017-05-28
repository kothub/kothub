package gitlin.kothub.github.api.data

import gitlin.kothub.utilities.*
import org.json.JSONArray
import org.json.JSONObject


data class UserSummary(val json: JSONObject): RateLimit(json.obj("rateLimit")!!) {
    val avatarUrl: String by json
    val login: String by json
    val name: String by json
    val websiteUrl: String by json
    val bio: String by json
    val url: String by json
    val company: String by json
    val location: String by json
    val followers: Int by json.obj("totalCount")
    val pinnedRepositories =
            json.obj("pinnedRepositories")?.arr("nodes")?.map<JSONObject, PinnedRepository> { PinnedRepository(it) }
            ?: arrayListOf()
}


data class PinnedRepository(val json: JSONObject) {
    val name: String by json
    val description: String by json
}

