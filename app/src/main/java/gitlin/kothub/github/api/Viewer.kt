package gitlin.kothub.github.api

import android.content.Context
import com.github.kittinunf.fuel.core.FuelError
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.nullString
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import gitlin.kothub.R.string.login
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.github.api.dsl.*
import io.reactivex.Single


val userFragment: User.() -> Unit =
    createFragment {
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


val userOrganizationsFragment: User.() -> Unit =
    createFragment {
        organizations(first = value(10)) {
            nodes {
                name
                avatarUrl
            }
        }
    }

val viewerSummaryQuery =
        query {
            viewer {
                fragment(userFragment)
                fragment(userOrganizationsFragment)
            }
        }

val userSummaryQuery =
        query(Type.STRING("login")) {
            user(login = variable("login")) {
                fragment(userFragment)
            }
        }

val loginQuery = query {
    viewer {
        login
        email
        avatarUrl
    }
}

fun Context.getUser(): Single<Triple<String, String?, String>> {

    return post(loginQuery)
            .map { result ->
                val viewer = result["viewer"]
                val login = viewer["login"].asString
                val email = viewer["email"].nullString
                val avatarUrl = viewer["avatarUrl"].asString

                Triple(login, email, avatarUrl)
            }
}


fun Context.viewerSummary (): Single<UserSummary> {

    return post(viewerSummaryQuery)
            .map {
                UserSummary(it["viewer"].asJsonObject)
            }
}


fun Context.userSummary (login: String): Single<UserSummary> {

    return post(userSummaryQuery, mapOf("login" to login))
            .map {
                UserSummary(it["user"].asJsonObject)
            }
}

fun Context.drawerInfo (): Single<DrawerInfo> {
    return post(
        query {
            viewer {
                login
                email
                avatarUrl
                name
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
    )
    .map {
        DrawerInfo(it["viewer"].asJsonObject)
    }
}

fun Context.notifications (callback: (FuelError?, Notifications?) -> Unit) {
    get("notifications") {
        error, result -> callback(error,
            if (result == null)
                null
            else {
                val json: JsonArray = JsonParser().parse(result).asJsonArray
                Notifications(json)
            })
    }
}