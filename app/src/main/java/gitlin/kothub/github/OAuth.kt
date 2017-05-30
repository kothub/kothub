package gitlin.kothub.github

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result


object OAuthValues {
    var isLoggedIn: Boolean = false

    lateinit var GITHUB_CLIENT: String
    lateinit var GITHUB_SECRET: String
    lateinit var REDIRECT_URL: String
    lateinit var GITHUB_TOKEN: String


    operator fun component1() = GITHUB_CLIENT
    operator fun component2() = GITHUB_SECRET
    operator fun component3() = REDIRECT_URL
    operator fun component4() = scopes.joinToString(" ")


    val scopes = arrayOf(
            "user",
            "repo",
            "notifications",
            "gist",
            "read:org"
    )
}

fun isLoggedIn () = OAuthValues.isLoggedIn

fun authorizeUrl (): String {

    val (id, secret, redirect, scopes) = OAuthValues

    return "https://github.com/login/oauth/authorize?scope=$scopes&client_id=$id&redirect_uri=$redirect"
}

fun requestAccessToken (code: String, callback: (Any?, String?) -> Unit) {

    val (id, secret, redirect, scopes) = OAuthValues

    val url = "https://github.com/login/oauth/access_token?code=$code&client_id=$id&client_secret=$secret"

    Fuel.post(url)
        .header(Pair("Accept", "application/json"))
        .responseJson() { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    callback(result.error, null)
                }
                is Result.Success -> {
                    val (data) = result
                    val token = data!!.obj().getString("access_token")
                    callback(null, token)
                }
            }
        }
}