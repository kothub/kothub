package gitlin.kothub.accounts

import android.content.Context
import android.content.SharedPreferences
import gitlin.kothub.R

class TokenStore private constructor(context: Context) {

    private val FILE = context.getString(R.string.github_preferences_file)
    private val TOKEN_KEY = context.getString(R.string.oauth_github_token)

    val preferences: SharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    companion object {

        private var instance: TokenStore? = null

        fun get(context: Context): TokenStore {
            if (instance == null) {
                instance = TokenStore(context)
            }

            return instance!!
        }
    }

    fun getToken (): String? = preferences.getString(TOKEN_KEY, null)

    fun saveToken (token: String) {
        preferences.edit().putString(TOKEN_KEY, token).commit()
    }
}