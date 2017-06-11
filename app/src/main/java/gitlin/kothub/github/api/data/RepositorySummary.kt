package gitlin.kothub.github.api.data

import android.util.Log
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class RepositoryReadme(
    val value: String
) {
    companion object: Deserializer<RepositoryReadme> {

        val gson = Gson()

        override fun fromJson(json: JsonObject): RepositoryReadme {
            val readmemd: String? = if (json["READMEMD"].isJsonNull) null else json["READMEMD"]["text"].string
            val readme: String? = if (json["README"].isJsonNull) null else json["README"]["text"].string
            val readmetxt: String? = if (json["READMETXT"].isJsonNull) null else json["READMETXT"]["text"].string

            return RepositoryReadme(readmemd ?: readme ?: readmetxt ?: "# No README")
        }
    }
}

class RepositorySummary (
        val ownerAvatarUrl: String,
        val ownerLogin: String,
        val nameWithOwner: String,
        val name: String,
        val description: String?,
        val stargazers: Int,
        val forks: Int,
        val watchers: Int,
        val license: String?,
        val language: Language?,
        val viewHasStarred: Boolean,
        val viewerCanSubscribe: Boolean,
        val pushedAt: String?
) {


    companion object: Deserializer<RepositorySummary> {

        val gson = Gson()

        override fun fromJson(json: JsonObject): RepositorySummary {

            val language =
                    if (json["primaryLanguage"].isJsonNull) null
                    else gson.fromJson<Language>(json["primaryLanguage"].obj)

            return RepositorySummary(
                    ownerAvatarUrl = json["owner"]["avatarUrl"].string,
                    ownerLogin = json["owner"]["login"].string,
                    nameWithOwner = json["nameWithOwner"].string,
                    name = json["name"].string,
                    description = json["description"].nullString,
                    stargazers = json["stargazers"]["totalCount"].int,
                    watchers = json["watchers"]["totalCount"].int,
                    forks = json["forks"]["totalCount"].int,
                    license = json["license"].nullString,
                    language = language,
                    viewerCanSubscribe = json["viewerCanSubscribe"].bool,
                    viewHasStarred = json["viewerHasStarred"].bool,
                    pushedAt = json["pushedAt"].nullString
            )
        }
    }
}

