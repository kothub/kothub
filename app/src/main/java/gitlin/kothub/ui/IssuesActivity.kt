package gitlin.kothub.ui

import android.os.Bundle
import android.app.Activity
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity
import gitlin.kothub.R
import gitlin.kothub.ui.AppDrawer
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*

class IssuesActivity : LifecycleAppCompatActivity() {

    lateinit var drawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)
        setSupportActionBar(toolbar)
        drawer = AppDrawer(this, toolbar)
        lifecycle.addObserver(drawer)
        drawer.select(drawer.issues)
    }
}
