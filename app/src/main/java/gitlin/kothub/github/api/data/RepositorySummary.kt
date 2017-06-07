package gitlin.kothub.github.api.data

import android.util.Log
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/*
query(Type.STRING("owner"), Type.STRING("name")) {
                repository(owner = variable("owner"), name = variable("name")) {
                    watchers { totalCount }
                    stargazers { totalCount }
                    forks { totalCount }
                    license
                    primaryLanguage {
                        color
                        name
                    }
                    viewerHasStarred
                    viewerCanSubscribe
                    pushedAt
                    obj(alias = "READMEMD", expression = "master:README.md") {
                        on<Blob> {
                            text
                        }
                    }
                    obj(alias = "README", expression = "master:README") {
                        on<Blob> {
                            text
                        }
                    }
                    obj(alias = "READMETXT", expression = "master:README.txt") {
                        on<Blob> {
                            text
                        }
                    }
                }
            }
 */


class RepositorySummary (
        val ownerAvatarUrl: String,
        val ownerLogin: String,
        val nameWithOwner: String,
        val description: String?,
        val stargazers: Int,
        val forks: Int,
        val watchers: Int,
        val license: String?,
        val language: Language?,
        val viewHasStarred: Boolean,
        val viewerCanSubscribe: Boolean,
        val pushedAt: String?,
        val readme: String
) {


    companion object: Deserializer<RepositorySummary> {

        val gson = Gson()

        override fun fromJson(json: JsonObject): RepositorySummary {

            val language =
                    if (json["primaryLanguage"].isJsonNull) null
                    else gson.fromJson<Language>(json["primaryLanguage"].obj)


            val readmemd = json["READMEMD"].nullString
            val readme = json["README"].nullString
            val readmetxt = json["READMETXT"].nullString

            val finalReadme = readmemd ?: readme ?: readmetxt ?: ""

            return RepositorySummary(
                    ownerAvatarUrl = json["owner"]["avatarUrl"].string,
                    ownerLogin = json["owner"]["login"].string,
                    nameWithOwner = json["nameWithOwner"].string,
                    description = json["description"].nullString,
                    stargazers = json["stargazers"]["totalCount"].int,
                    watchers = json["watchers"]["totalCount"].int,
                    forks = json["forks"]["totalCount"].int,
                    license = json["license"].nullString,
                    language = language,
                    viewerCanSubscribe = json["viewerCanSubscribe"].bool,
                    viewHasStarred = json["viewerHasStarred"].bool,
                    pushedAt = json["pushedAt"].nullString,
                    readme = finalReadme
            )
        }
    }
}

