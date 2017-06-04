package gitlin.kothub.github.api

import android.content.Context
import com.github.kittinunf.fuel.core.FuelError
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.nullString
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import gitlin.kothub.R.string.login
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.data.LoginData
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
        name
    }
}

fun Context.getUser(): Single<LoginData> {

    return post(loginQuery)
            .map { result ->
                LoginData.fromJson(result["viewer"].obj)
            }
}


fun Context.viewerSummary (): Single<UserSummary> {

    return post(viewerSummaryQuery)
            .map { UserSummary.fromJson(it["viewer"].obj) }
}


fun Context.userSummary (login: String): Single<UserSummary> {

    return post(userSummaryQuery, mapOf("login" to login))
            .map { UserSummary.fromJson(it["user"].obj) }
}

fun Context.totalIssues(): Single<DrawerInfo> {
    return post(
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
    )
    .map { DrawerInfo.fromJson(it["viewer"].obj) }
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