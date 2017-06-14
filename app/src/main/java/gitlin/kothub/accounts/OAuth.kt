package gitlin.kothub.accounts

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.rx.rx_string
import com.github.salomonbrys.kotson.get
import com.google.gson.JsonParser
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object OAuthConstants {

    const val REDIRECT_URI = "oauth://kothub"
    const val DOMAIN = "https://github.com"
    const val OAUTH_PATH = "/login/oauth/authorize"
    const val TOKEN_PATH = "/login/oauth/access_token"

    val scopes = arrayOf(
            "user",
            "repo",
            "notifications",
            "gist",
            "read:org"
    )
}


fun authorizeUrl (clientId: String): String {

    val domain = OAuthConstants.DOMAIN
    val path = OAuthConstants.OAUTH_PATH
    val scopes = OAuthConstants.scopes.joinToString(" ")
    val redirect = OAuthConstants.REDIRECT_URI

    return "$domain$path?scope=$scopes&client_id=$clientId&redirect_uri=$redirect"
}

fun requestAccessToken (code: String, clientId: String, secret: String): Single<String> {

    val domain = OAuthConstants.DOMAIN
    val path = OAuthConstants.TOKEN_PATH

    val url = "$domain$path?code=$code&client_id=$clientId&client_secret=$secret"

    return Fuel.post(url)
            .header(Pair("Accept", "application/json"))
            .rx_string()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.get() }
            .map { JsonParser().parse(it) }
            .map { it["access_token"].asString }
}