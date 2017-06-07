package gitlin.kothub.github.api.data

import android.util.Log
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject


open class LoginData(
        val avatarUrl: String,
        val login: String,
        val name: String?,
        val email: String?
) {
    companion object: Deserializer<LoginData> {

        val gson = Gson()

        override fun fromJson(json: JsonObject): LoginData {
            return gson.fromJson<LoginData>(json)
        }
    }
}


class UserSummary (
        avatarUrl: String,
        login: String,
        email: String?,
        name: String?,
        val websiteUrl: String?,
        val bio: String?,
        val company: String?,
        val location: String?,
        val followers: Int,
        val following: Int,
        val starredRepositories: Int,
        val repositories: Int,
        val pinnedRepositories: List<PinnedRepository>,
        val organizations: List<Organization>
): LoginData(avatarUrl, login, name, email) {


    companion object: Deserializer<UserSummary> {

        override fun fromJson(json: JsonObject): UserSummary {

            val pinnedRepositories = PinnedRepository.fromJson(json["pinnedRepositories"]["nodes"].array)
            val organizations =
                if ("organizations" in json)
                    Organization.fromJson(json["organizations"]["nodes"].array)
                else
                    arrayListOf()

            return UserSummary(
                    avatarUrl = json["avatarUrl"].string,
                    login = json["login"].string,
                    email = json["email"].nullString,
                    name = json["name"].nullString,
                    websiteUrl = json["websiteUrl"].nullString,
                    bio = json["bio"].nullString,
                    company = json["company"].nullString,
                    location = json["location"].nullString,
                    followers = json["followers"]["totalCount"].int,
                    following = json["following"]["totalCount"].int,
                    starredRepositories = json["starredRepositories"]["totalCount"].int,
                    repositories = json["repositories"]["totalCount"].int,
                    pinnedRepositories = pinnedRepositories,
                    organizations = organizations
            )
        }
    }
}

data class Language(val name: String, val color: String)
data class Organization(val name: String, val avatarUrl: String) {

    companion object: Deserializer<Organization> {

        private val gson = Gson()

        override fun fromJson(json: JsonObject): Organization {
            return gson.fromJson<Organization>(json)
        }
    }
}


data class PinnedRepository(
        val owner: String,
        val name: String,
        val description: String?,
        val stargazers: Int,
        val forks: Int,
        val language: Language?
) {

    companion object: Deserializer<PinnedRepository> {

        private val gson = Gson()

        override fun fromJson(json: JsonObject): PinnedRepository {

            val language =
                if (json["primaryLanguage"].isJsonNull) null
                else gson.fromJson<Language>(json["primaryLanguage"].obj)

            return PinnedRepository(
                    name = json["name"].string,
                    description = json["description"].nullString,
                    stargazers = json["stargazers"]["totalCount"].int,
                    forks = json["forks"]["totalCount"].int,
                    language = language,
                    owner = json["owner"]["login"].string
            )
        }
    }
}


