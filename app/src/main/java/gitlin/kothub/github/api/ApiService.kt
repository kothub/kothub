package gitlin.kothub.github.api

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.rx.rx_responseString
import com.github.kittinunf.fuel.rx.rx_string
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.obj
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gitlin.kothub.accounts.TokenStore
import gitlin.kothub.github.api.data.RateLimit
import gitlin.kothub.github.api.dsl.Query
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


inline fun <reified T: ApiService> Context.getService(): T {
    return T::class.constructors.first().call(this)
}


abstract class ApiService(val context: Context) {

    companion object {
        private val gson = GsonBuilder().create()
        private val parser = JsonParser()
    }

    /**
     * Base query for API v4
     */
    fun baseQuery(query: String, variables: Map<String, Any> = mapOf<String, Any>()): Single<JsonObject> {

        val jsonVariables = gson.toJson(variables)
        Log.i("Post JSON Variables", jsonVariables)

        val token = TokenStore.get(context).getToken()

        Log.i("Query", query)
        Log.i("Post TOKEN", token)

        return Fuel.post("https://api.github.com/graphql")
                .header(Pair("Authorization", "Bearer $token"))
                .body("{ \"query\": \"$query\", \"variables\": $jsonVariables }")
                .rx_responseString()
                .subscribeOn(Schedulers.io())
                .map { (response, result) ->

                    val (body) = result

                    if (response.httpStatusCode >= 400) {
                        throw GithubApiException(String(response.data), response.httpStatusCode)
                    }

                    Log.i("Post BODY", response.httpStatusCode.toString())
                    Log.i("Post BODY", String(response.data))

                    val json = parser.parse(body)
                    val data = json["data"]

                    if (data.isJsonNull) {
                        val errors = json["errors"].asJsonArray.map { it["message"].asString }
                        throw GithubApiException(errors, response.httpStatusCode)
                    }

                    data.asJsonObject
                }
                .doAfterSuccess { data ->
                    val rate = data["rateLimit"]
                    if (!rate.isJsonNull) {
                        ApiRateLimit.updateRate(RateLimit.fromJson(rate.obj))
                    }

                }
    }

    fun query(query: Query, variables: Map<String, Any> = mapOf<String, Any>()): Single<JsonObject> {
        return query(query.toString(), variables)
    }

    fun query(query: String, variables: Map<String, Any> = mapOf<String, Any>()): Single<JsonObject> {
        return baseQuery(query, variables).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> query(query: String, variables: Map<String, Any> = mapOf<String, Any>(), fn: (Single<JsonObject>) -> Single<T>): Single<T> {
        return baseQuery(query, variables).compose(fn).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Base get for API v3
     */
    private fun baseGet(query: String): Single<JsonElement> {

        val token = TokenStore.get(context).getToken()

        return Fuel.get("https://api.github.com/$query")
                .header(Pair("Authorization", "token $token"))
                .rx_string()
                .subscribeOn(Schedulers.io())
                .map { result ->
                    val (body) = result
                    val json = parser.parse(body)
                    json
                }
    }

    fun <T> get(query: String, fn: (Single<JsonElement>) -> Single<T>): Single<T> {
        return baseGet(query).compose(fn).observeOn(AndroidSchedulers.mainThread())
    }

    fun get(query: String): Single<JsonElement> {
        return baseGet(query).observeOn(AndroidSchedulers.mainThread())
    }

}