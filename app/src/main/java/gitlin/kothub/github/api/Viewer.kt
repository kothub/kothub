package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.core.FuelError
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.github.api.dsl.*
import gitlin.kothub.utilities.arr
import gitlin.kothub.utilities.map
import gitlin.kothub.utilities.obj
import gitlin.kothub.utilities.totalCount
import org.json.JSONObject


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

fun viewerSummary (callback: (FuelError?, UserSummary?) -> Unit) {
    post(viewerSummaryQuery) { error, result -> callback(error, if (result == null) null else UserSummary(result["viewer"].asJsonObject)) }
}


fun userSummary (login: String, callback: (FuelError?, UserSummary?) -> Unit) {
    post(
        userSummaryQuery,
        mapOf("login" to login)
    ) { error, result -> callback(error, if (result == null) null else UserSummary(result["user"].asJsonObject)) }
}

fun drawerInfo (callback: (FuelError?, DrawerInfo?) -> Unit) {
    post(
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
    ) {
        error, result -> callback(error, if (result == null) null else DrawerInfo(result["viewer"].asJsonObject))
    }
}

fun notifications (callback: (FuelError?, Notifications?) -> Unit) {
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