package gitlin.kothub.ui

import android.os.Bundle

import org.jetbrains.anko.AnkoLogger
import gitlin.kothub.R
import kotlinx.android.synthetic.main.toolbar.*
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import gitlin.kothub.utilities.createFragment


class UserProfileActivity : LifecycleAppCompatActivity(), AnkoLogger {

    lateinit var drawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        createFragment(savedInstanceState, R.id.user_profile_fragment) {
            UserProfileFragment.newInstance(getProfileName())
        }


        drawer = AppDrawer(this, toolbar)
        lifecycle.addObserver(drawer)
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.profile)
    }
}