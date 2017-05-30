package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.result.Result
import gitlin.kothub.github.OAuthValues
import gitlin.kothub.github.api.dsl.Query
import gitlin.kothub.utilities.obj
import org.json.JSONObject


fun post(query: Query, callback: (FuelError?, JSONObject?) -> Unit): Request {

    return Fuel.post("https://api.github.com/graphql")
        .header(Pair("Authorization", "Bearer ${OAuthValues.GITHUB_TOKEN}"))
        .body("{ \"query\": \"$query\" }")
        .responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> callback(result.error, null)
                is Result.Success -> {
                    if (result.value.obj().isNull("data")) {
                        callback(null, null)
                    }
                    else {
                        callback(null, result.value.obj().obj("data")!!)
                    }
                }
            }
        }
}