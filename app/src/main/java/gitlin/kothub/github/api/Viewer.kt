package gitlin.kothub.github.api

import com.github.kittinunf.fuel.core.FuelError
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.github.api.dsl.IssueState
import gitlin.kothub.github.api.dsl.query
import gitlin.kothub.utilities.arr
import gitlin.kothub.utilities.map
import gitlin.kothub.utilities.obj
import gitlin.kothub.utilities.totalCount
import org.json.JSONObject


fun userSummary (callback: (FuelError?, UserSummary?) -> Unit) {
    post(
        query {
            viewer {
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
                pinnedRepositories(6) {
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
                organizations(first = 10) {
                    nodes {
                        name
                        avatarUrl
                    }
                }
            }
        }
    ) { error, result -> callback(error, if (result == null) null else UserSummary(result["viewer"].asJsonObject)) }
}


fun drawerInfo (callback: (FuelError?, DrawerInfo?) -> Unit) {
    post(
        query {
            viewer {
                login
                email
                avatarUrl
                name
                repositories(first = 30) {
                    pageInfo {
                        hasNextPage
                        endCursor
                    }
                    nodes {
                        issues(states = IssueState.OPEN) {
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