package gitlin.kothub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import gitlin.kothub.R.layout.activity_main
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        tv_hello.text = "Github / Kotlin"
    }
}
