package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.data.UserSummary
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
                    }
               }
            }
        }
    ) { error, result -> callback(error, if (result == null) null else UserSummary(result.obj("viewer")) ) }
}


typealias ApiCall = (FuelError?, Any?) -> Unit

// TODO: pagination
fun issueNumber (callback: (FuelError?, Int?) -> Unit) {
    post(
        query {
            viewer {
                repositories(first = 30) {
                    pageInfo {
                        hasNextPage
                        endCursor
                    }
                    nodes {
                        issues(states = IssueState.OPEN) {  }
                    }
                }
            }
        }
    ) { error, result ->

        if (error == null && result != null) {
            val nodes = result.obj("viewer")?.obj("repositories")?.arr("nodes")
            val count: Int = nodes?.map<JSONObject, Int> {
              it.totalCount("issues") ?: 0
            }?.sum() ?: 0

            callback(error, count)
        }
        else {
            callback(error, null)
        }
    }
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
        error, result ->

        callback(error, if (result == null) null else DrawerInfo(result.obj("viewer")))
    }
}