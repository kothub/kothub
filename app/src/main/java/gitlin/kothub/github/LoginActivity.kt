package gitlin.kothub.github


import android.app.Activity
import kotlin.jvm.javaClass
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import gitlin.kothub.R
import gitlin.kothub.R.layout.activity_login

class LoginActivity: Activity() {

    init {
        Log.d("LoginActivity", "init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "before setContentView")
        setContentView(R.layout.activity_login)
        Log.d("LoginActivity", "created");
    }

    fun openLogin () {
        val scopes = "user"

        val url = "https://github.com/login/oauth/authorize?scope=${scopes}&client_id=${getString(R.string.github_client)}&redirect_uri=oauth://kothub"
        Log.d("LoginActivity", url)
        val intent = Intent(this, LoginWebViewActivity::class.java)
        intent.putExtra("url", url)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (intent != null && requestCode == 0) {
            val code = intent.data.getQueryParameter("code")
            Log.d("LoginActivity", code)
        }
    }

    fun onLoginClick (view: View) {
        openLogin()
    }
}