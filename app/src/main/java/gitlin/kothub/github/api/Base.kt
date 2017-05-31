package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.result.Result
import gitlin.kothub.github.OAuthValues.GITHUB_TOKEN
import gitlin.kothub.github.api.data.RateLimit
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
                is Result.Success -> {
                    val obj = result.value.obj()
                    if (obj.isNull("data")) {
                        callback(null, null)
                    }
                    else {
                        val rate = obj.obj("data").obj("rateLimit")
                        Log.i("TEST", rate.toString())
                        if (rate != null) {
                            ApiRateLimit.updateRate(RateLimit(rate))
                        }

                        callback(null, obj.obj("data")!!)
                    }
                }
            }
        }
}

fun header(query: String): Request {

    return Fuel.get("https://api.github.com/$query")
            .header(Pair("Authorization", "token $GITHUB_TOKEN"))
}

fun get(query: String, callback: (FuelError?, String?) -> Unit): Request {

    return header(query)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> callback(result.error, null)
                    is Result.Success -> callback(null, result.value)
                }
            }
}
