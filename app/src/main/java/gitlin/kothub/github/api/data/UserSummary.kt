package gitlin.kothub.github.api.data

import com.github.salomonbrys.kotson.*
import com.google.gson.JsonObject


data class UserSummary(private val json: JsonObject) {

    val avatarUrl: String by json.byString
    val login: String by json.byString
    val name: String by json.byString
    val websiteUrl: String? by json.byNullableString
    val bio: String? by json.byNullableString
    val url: String? by json.byNullableString
    val company: String? by json.byNullableString
    val location: String? by json.byNullableString
    val followers : Int = json["followers"]["totalCount"].asInt
    val following: Int = json["following"]["totalCount"].asInt
    val starredRepositories: Int = json["starredRepositories"]["totalCount"].asInt
    val repositories: Int = json["repositories"]["totalCount"].asInt

    val pinnedRepositories = json["pinnedRepositories"]["nodes"].array.map { PinnedRepository(it.asJsonObject) }
    val organizations = json["organizations"]["nodes"].array.map { Organization(it.asJsonObject) }
}

data class Language(val json: JsonObject) {
    val name: String by json.byString
    val color: String by json.byString
}

data class PinnedRepository(val json: JsonObject) {
    val name: String by json.byString
    val description: String? by json.byNullableString
    val stargazers: Int = json["stargazers"]["totalCount"].asInt
    val forks: Int = json["forks"]["totalCount"].asInt
    val language: Language? = if (json["primaryLanguage"].isJsonNull) null else Language(json["primaryLanguage"].asJsonObject)
}

data class Organization(val json: JsonObject) {
    val name: String by json.byString
    val avatarUrl: String by json.byString
}

