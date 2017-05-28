package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import gitlin.kothub.github.OAuthValues
import gitlin.kothub.github.api.dsl.Query


fun post(query: Query): Request {

    Log.d("query", query.toString())
    Log.d("query", OAuthValues.GITHUB_TOKEN)

    return Fuel.post("https://api.github.com/graphql")
        .header(Pair("Authorization", "Bearer ${OAuthValues.GITHUB_TOKEN}"))
        .body("{ \"query\": \"$query\" }")
}