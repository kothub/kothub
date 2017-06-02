package gitlin.kothub.ui

import android.arch.lifecycle.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import gitlin.kothub.R
import kotlinx.android.synthetic.main.toolbar.*
import gitlin.kothub.utilities.createFragment


class ViewerProfileActivity : AppCompatActivity(), AnkoLogger, LifecycleRegistryOwner {

    private val registry = LifecycleRegistry(this)
    override fun getLifecycle() = registry

    lateinit var drawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        createFragment(savedInstanceState, R.id.user_profile_fragment) {
            UserProfileFragment.newInstance()
        }

        drawer = AppDrawer(this, toolbar)
        lifecycle.addObserver(drawer)
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.profile)
    }
}