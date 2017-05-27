package gitlin.kothub.github.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result



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
