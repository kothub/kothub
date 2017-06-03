package gitlin.kothub.github.api

import android.content.Context
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.rx.rx_responseString
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.get
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gitlin.kothub.accounts.TokenStore
import gitlin.kothub.github.api.data.RateLimit
import gitlin.kothub.github.api.dsl.Query
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun Context.post(query: Query, variables: Map<String, Any> = mapOf<String, Any>()): Single<JsonObject> {
    return post(query.toString(), variables)
}

fun Context.post(query: String, variables: Map<String, Any> = mapOf<String, Any>()): Single<JsonObject> {

    val jsonVariables = GsonBuilder().create().toJson(variables)

    val token = TokenStore.get(this).getToken()

    return Fuel.post("https://api.github.com/graphql")
        .header(Pair("Authorization", "Bearer $token"))
        .body("{ \"query\": \"$query\", \"variables\": $jsonVariables }")
        .rx_responseString()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { (response, result) ->

            val (body) = result

            val json = JsonParser().parse(body)
            val data = json["data"]

            if (data.isJsonNull) {
                val errors = json["errors"].asJsonArray.map { it["message"].asString }
                throw GithubApiException(errors)
            }

            data.asJsonObject
        }
        .doAfterSuccess { data ->
            val rate = data["rateLimit"]
            if (!rate.isJsonNull) {
                ApiRateLimit.updateRate(RateLimit(rate.asJsonObject))
            }

        }
}

fun Context.header(query: String): Request {

    val token = TokenStore.get(this).getToken()

    return Fuel.get("https://api.github.com/$query")
            .header(Pair("Authorization", "token $token"))
}

fun Context.get(query: String, callback: (FuelError?, String?) -> Unit): Request {

    return header(query)
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> callback(result.error, null)
                    is Result.Success -> callback(null, result.value)
                }
            }
}
