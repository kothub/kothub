package gitlin.kothub.ui

import android.os.Bundle
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import gitlin.kothub.R
import gitlin.kothub.ui.AppDrawer
import kotlinx.android.synthetic.main.toolbar.*

class IssuesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)
        setSupportActionBar(toolbar)
        val drawer = AppDrawer(this, toolbar)
        drawer.select(drawer.issues)
    }
}
