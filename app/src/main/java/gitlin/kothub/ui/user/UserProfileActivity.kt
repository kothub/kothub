package gitlin.kothub.ui.user

import android.content.Intent
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import gitlin.kothub.R
import gitlin.kothub.ui.drawer.AppDrawer
import gitlin.kothub.ui.getProfileName
import kotlinx.android.synthetic.main.toolbar.*
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import gitlin.kothub.utilities.createFragment


class UserProfileActivity : LifecycleAppCompatActivity(), AnkoLogger {

    lateinit var drawer: AppDrawer
    lateinit var fragment: UserProfileFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        this.fragment = UserProfileFragment.newInstance(getProfileName(intent))
        createFragment(savedInstanceState, R.id.user_profile_fragment) {
            fragment
        }

        drawer = AppDrawer(this, toolbar)
    }

    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)
        if (getProfileName(intent) != getProfileName(this.intent)) {
            setIntent(intent)
            updateUser(intent)
        }
    }

    private fun updateUser (intent: Intent) {
        this.fragment.updateUser(getProfileName(intent))
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.profile)
    }
}