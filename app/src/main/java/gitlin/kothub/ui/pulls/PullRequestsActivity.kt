package gitlin.kothub.ui.pulls

import android.os.Bundle
import gitlin.kothub.R
import gitlin.kothub.ui.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*

class PullRequestsActivity : LifecycleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)
        setSupportActionBar(toolbar)
        initDrawer(toolbar)
    }

    override fun onResume() {
        super.onStart()
        drawer.select(drawer.pulls)
    }
}
