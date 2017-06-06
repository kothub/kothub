package gitlin.kothub.ui.pulls

import android.os.Bundle
import gitlin.kothub.R
import gitlin.kothub.ui.drawer.AppDrawer
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*

class PullRequestsActivity : LifecycleAppCompatActivity() {

    lateinit var drawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)
        setSupportActionBar(toolbar)
        drawer = AppDrawer(this, toolbar)
        drawer.select(drawer.pulls)
    }
}
