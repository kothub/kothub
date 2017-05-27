package gitlin.kothub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.api.api
import gitlin.kothub.github.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        tv_hello.text = "Github / Kotlin"


        Log.d("MainActivity", getString(R.string.github_client))
        Log.d("MainActivity", getString(R.string.github_secret))

        startActivity(Intent(this, LoginActivity::class.java))
    }
}
