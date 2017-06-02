package gitlin.kothub.github.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.get
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gitlin.kothub.github.OAuthValues.GITHUB_TOKEN
import gitlin.kothub.github.api.data.RateLimit
import gitlin.kothub.github.api.dsl.Query


fun post(query: Query, variables: Map<String, Any> = mapOf<String, Any>(), callback: (FuelError?, JsonObject?) -> Unit): Request {
    return post(query.toString(), variables, callback)
}

fun post(query: String, variables: Map<String, Any> = mapOf<String, Any>(), callback: (FuelError?, JsonObject?) -> Unit): Request {

    val jsonVariables = GsonBuilder().create().toJson(variables)

    return Fuel.post("https://api.github.com/graphql")
        .header(Pair("Authorization", "Bearer $GITHUB_TOKEN"))
        .body("{ \"query\": \"$query\", \"variables\": $jsonVariables }")
        .responseString { _, _, result ->
            when (result) {
                is Result.Failure -> callback(result.error, null)
                is Result.Success -> {

                    val json = JsonParser().parse(result.value)
                    val data = json["data"]

                    if (data.isJsonNull) {
                        val errors = json["errors"]
                        // TODO: return errors
                        callback(null, null)
                    }
                    else {
                        val rate = data["rateLimit"]
                        if (!rate.isJsonNull) {
                            ApiRateLimit.updateRate(RateLimit(rate.asJsonObject))
                        }

                        callback(null, data.asJsonObject)
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
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> callback(result.error, null)
                    is Result.Success -> callback(null, result.value)
                }
            }
}
