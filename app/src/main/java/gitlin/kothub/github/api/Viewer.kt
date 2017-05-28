package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.dsl.query
import gitlin.kothub.utilities.obj


fun getLogin (token: String) {

    val body =
        """
            {
                "query": "query { viewer { login } }"

            }
        """

    Fuel.post("https://api.github.com/graphql")
        .header(Pair("Authorization", "Bearer $token"))
        .body(body)
        .responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.d("error", response.toString())
                }
                is Result.Success -> {
                    val (data) = result
                    Log.d("success", data)
                }
            }
    }
}

fun userSummary (callback: (Any?, UserSummary?) -> Unit) {
    post(
        query {
            viewer {
                avatarUrl
                login
                bio
                company
                location
                followers { totalCount }
                name
                websiteUrl
                url
                pinnedRepositories(first = 6) {
                    nodes {
                        name
                        description
                    }
               }
            }
        }
    )
    .responseJson { request, response, result ->
        when (result) {
            is Result.Failure -> callback(result.error, null)
            is Result.Success -> callback(null, UserSummary(result.value.obj().obj("data")!!.obj("viewer")!!))
        }
    }

}