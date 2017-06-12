package gitlin.kothub.ui.issues

import android.os.Bundle
import gitlin.kothub.R
import gitlin.kothub.ui.LifecycleAppCompatActivity
import gitlin.kothub.ui.drawer.AppDrawer
import kotlinx.android.synthetic.main.toolbar.*

class IssuesActivity : LifecycleAppCompatActivity() {

    lateinit var drawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)
        setSupportActionBar(toolbar)
        drawer = AppDrawer(this, toolbar)
        drawer.select(drawer.issues)
    }
}
