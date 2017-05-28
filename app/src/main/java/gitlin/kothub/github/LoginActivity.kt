package gitlin.kothub.github


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import gitlin.kothub.R
import gitlin.kothub.github.api.userSummary
import gitlin.kothub.utilities.editSharedPreferences
import gitlin.kothub.utilities.set


class LoginActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun openWebView () {

        val intent = Intent(this, LoginWebViewActivity::class.java)
        intent.putExtra("url", authorizeUrl())
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        super.onActivityResult(requestCode, resultCode, intent)

        if (intent != null && requestCode == 0) {

            val code = intent.data.getQueryParameter("code")

            requestAccessToken(code, { error, token ->
                if (error == null) {
                    Log.d("OAuthToken", token)
                    OAuthValues.isLoggedIn = true
                    OAuthValues.GITHUB_TOKEN = token!!

                    editSharedPreferences(getString(R.string.github_preferences_file)) {
                        set(getString(R.string.oauth_github_token) to token)
                    }

                    userSummary { error, summary ->
                        if (summary == null) {
                            Log.d("USERSUMMARY", "ERROR")
                            Log.d("USERSUMMARY", error?.response?.httpResponseMessage ?: "NO ERROR??")
                        }
                        else {
                            Log.d("USERSUMMARY", summary.toString())
                            Log.d("USERSUMMARY", summary.location)
                        }
                    }
                } else {
                    // Handle error
                }
            })


        }
    }

    fun onLoginClick (view: View) {
        openWebView()
    }
}