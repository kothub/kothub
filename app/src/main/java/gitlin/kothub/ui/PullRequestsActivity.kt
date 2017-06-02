package gitlin.kothub.ui

import android.os.Bundle
import android.app.Activity
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity
import gitlin.kothub.R
import gitlin.kothub.ui.AppDrawer
import kotlinx.android.synthetic.main.toolbar.*

class PullRequestsActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val registry = LifecycleRegistry(this)
    override fun getLifecycle() = registry

    lateinit var drawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)
        setSupportActionBar(toolbar)
        drawer = AppDrawer(this, toolbar)
        lifecycle.addObserver(drawer)
        drawer.select(drawer.pulls)
    }
}
