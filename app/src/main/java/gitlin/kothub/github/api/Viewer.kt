package gitlin.kothub.github.api

import android.content.Context
import com.github.kittinunf.fuel.core.FuelError
import com.github.salomonbrys.kotson.*
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import gitlin.kothub.R.string.login
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.data.LoginData
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.github.api.dsl.*
import io.reactivex.Single



class ViewerService(context: Context): ApiService(context) {

    companion object {
        val userFragment: GFragment<User> by lazy {
            createFragment<User> {
                avatarUrl
                login
                bio
                company
                location
                followers { totalCount }
                following { totalCount }
                starredRepositories { totalCount }
                repositories { totalCount }
                name
                websiteUrl
                url
                pinnedRepositories(value(6)) {
                    nodes {
                        name
                        owner {
                            login
                        }
                        description
                        stargazers { totalCount }
                        forks { totalCount }
                        primaryLanguage {
                            color
                            name
                        }
                    }
                }
            }
        }


        val userOrganizationsFragment: GFragment<User> by lazy {
            createFragment<User> {
                organizations(first = value(10)) {
                    nodes {
                        name
                        avatarUrl
                    }
                }
            }
        }

        val viewerSummaryQuery by lazy {
            query {
                viewer {
                    fragment(userFragment)
                    fragment(userOrganizationsFragment)
                }
            }
        }


        val userSummaryQuery by lazy {
            query(Type.STRING("login")) {
                user(login = variable("login")) {
                    fragment(userFragment)
                }
            }
        }

        val loginQuery by lazy {
            query {
                viewer {
                    login
                    email
                    avatarUrl
                    name
                }
            }
        }

        val drawerInfoQuery by lazy {
            query {
                viewer {
                    repositories(first = value(30)) {
                        pageInfo {
                            hasNextPage
                            endCursor
                        }
                        nodes {
                            issues(states = value(IssueState.OPEN)) {
                                totalCount
                            }
                        }
                    }
                }
            }
        }
    }


    fun getUser (): Single<LoginData> {

        return query(loginQuery) {
            it.map {
                LoginData.fromJson(it["viewer"].obj)
            }
        }
    }


    fun viewerSummary (): Single<UserSummary> {
        return query(viewerSummaryQuery) {
            it.map { UserSummary.fromJson(it["viewer"].obj) }
        }
    }

    fun userSummary (login: String): Single<UserSummary> {
        return query(userSummaryQuery, mapOf("login" to login)) {
            it.map { UserSummary.fromJson(it["user"].obj )}
        }
    }

    fun totalIssues(): Single<DrawerInfo> {
        return query(drawerInfoQuery) {
            it.map { DrawerInfo.fromJson(it["viewer"].obj) }
        }
    }

    fun notifications (): Single<Notifications> {
        return get("notifications") {
            it.map { Notifications(it.array) }
        }
    }
}


