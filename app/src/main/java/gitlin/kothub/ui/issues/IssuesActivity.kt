package gitlin.kothub.ui.issues

import android.os.Bundle
import gitlin.kothub.R
import gitlin.kothub.ui.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*

class IssuesActivity : LifecycleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)
        setSupportActionBar(toolbar)
        initDrawer(this, toolbar)
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.issues)
    }
}
