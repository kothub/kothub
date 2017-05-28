package gitlin.kothub

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.github.LoginActivity
import gitlin.kothub.github.OAuthValues
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	//private val TAG: String? = MainActivity::class.simpleName

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
        tv_hello.text = "Kothub"
    }

    override fun onStart() {
        super.onStart()
        initOAuth()
		Log.d("MainActivity", OAuthValues.isLoggedIn.toString())
		if (!OAuthValues.isLoggedIn)
        	startActivity(Intent(this, LoginActivity::class.java))
    }
}
