package gitlin.kothub.github.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.result.Result
import gitlin.kothub.github.OAuthValues.GITHUB_TOKEN
import gitlin.kothub.github.api.dsl.Query
import gitlin.kothub.utilities.obj
import org.json.JSONArray
import org.json.JSONObject


fun post(query: Query, callback: (FuelError?, JSONObject?) -> Unit): Request {

    return Fuel.post("https://api.github.com/graphql")
        .header(Pair("Authorization", "Bearer $GITHUB_TOKEN"))
        .body("{ \"query\": \"$query\" }")
        .responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> callback(result.error, null)
                is Result.Success -> callback(null, result.value.obj().obj("data")!!)
            }
        }
}

fun getObj(query: String, callback: (FuelError?, JSONObject?) -> Unit): Request {

    return Fuel.get("https://api.github.com/$query")
            .header(Pair("Authorization", "token $GITHUB_TOKEN"))
            .responseJson { request, response, result ->
                when (result) {
                    is Result.Failure -> callback(result.error, null)
                    is Result.Success -> callback(null, result.value.obj())
                }
            }
}

fun getArr(query: String, callback: (FuelError?, JSONArray?) -> Unit): Request {

    return Fuel.get("https://api.github.com/$query")
            .header(Pair("Authorization", "token $GITHUB_TOKEN"))
            .responseJson { request, response, result ->
                when (result) {
                    is Result.Failure -> callback(result.error, null)
                    is Result.Success -> callback(null, result.value.array())
                }
            }
}