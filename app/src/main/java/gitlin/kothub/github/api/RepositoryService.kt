package gitlin.kothub.github.api

import android.content.Context
import android.text.Spanned
import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonObject
import gitlin.kothub.github.api.data.RepositoryReadme
import gitlin.kothub.github.api.data.RepositorySummary
import gitlin.kothub.github.api.dsl.*
import gitlin.kothub.utilities.markdown.renderMarkdown
import io.reactivex.Single


class RepositoryService(context: Context): ApiService(context) {

    companion object {

        val summary by lazy {

            query(Type.STRING("owner"), Type.STRING("name")) {
                repository(owner = variable("owner"), name = variable("name")) {
                    nameWithOwner
                    owner {
                        login
                        avatarUrl
                    }
                    name
                    description
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
                    obj(alias = "READMEMD", expression = value("master:README.md")) {
                        on<Blob> {
                            text
                        }
                    }
                    obj(alias = "README", expression = value("master:README")) {
                        on<Blob> {
                            text
                        }
                    }
                    obj(alias = "READMETXT", expression = value("master:README.txt")) {
                        on<Blob> {
                            text
                        }
                    }
                }
            }

        }

        val README by lazy {
            query(Type.STRING("owner"), Type.STRING("name")) {
                repository(owner = variable("owner"), name = variable("name")) {
                    obj(alias = "READMEMD", expression = value("master:README.md")) {
                        on<Blob> {
                            text
                        }
                    }
                    obj(alias = "README", expression = value("master:README")) {
                        on<Blob> {
                            text
                        }
                    }
                    obj(alias = "READMETXT", expression = value("master:README.txt")) {
                        on<Blob> {
                            text
                        }
                    }
                }
            }
        }
    }


    fun repository (owner: String, name: String): Single<RepositorySummary> {

        return query(summary, mapOf("owner" to owner, "name" to name)) {
            it.map { RepositorySummary.fromJson(it["repository"].obj) }
        }
    }

    fun readme(owner: String, name: String): Single<Spanned?> {

        return query(README, mapOf("owner" to owner, "name" to name)) {
            it.map { RepositoryReadme.fromJson(it["repository"].obj) }
            .map {
                renderMarkdown(context, it.value, "$owner/$name", "master")
            }
        }
    }
}