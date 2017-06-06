package gitlin.kothub.github.api

import android.content.Context
import gitlin.kothub.github.api.dsl.*


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

        }
    }
}