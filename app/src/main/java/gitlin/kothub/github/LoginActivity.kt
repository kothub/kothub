package gitlin.kothub.github


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity
import android.view.View
import gitlin.kothub.R
import gitlin.kothub.utilities.editSharedPreferences
import gitlin.kothub.utilities.set
import gitlin.kothub.utilities.setupDrawer
import kotlinx.android.synthetic.main.toolbar.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


class LoginActivity: AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        setupDrawer(this, toolbar)
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
                    debug(token)
                    OAuthValues.isLoggedIn = true

                    OAuthValues.GITHUB_TOKEN = token!!

                    editSharedPreferences(getString(R.string.github_preferences_file)) {
                        set(getString(R.string.oauth_github_token) to token)
                    }

                    finish()
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
