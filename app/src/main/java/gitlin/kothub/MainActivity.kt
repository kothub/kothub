package gitlin.kothub

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.github.LoginActivity
import gitlin.kothub.github.OAuthValues
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun initOAuth () {
        OAuthValues.REDIRECT_URL = "oauth://kothub"
        OAuthValues.GITHUB_CLIENT = getString(R.string.github_client)
        OAuthValues.GITHUB_SECRET = getString(R.string.github_secret)


        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val token = sharedPref.getString(getString(R.string.oauth_github_token), "")
        OAuthValues.isLoggedIn = token.isNotEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        tv_hello.text = "Github / Kotlin"
    }

    override fun onStart() {
        super.onStart()
        initOAuth()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
