package gitlin.kothub.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.userSummary

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import android.view.Gravity
import android.widget.*
import com.github.kittinunf.fuel.core.FuelError
import com.squareup.picasso.Picasso
import gitlin.kothub.R
import gitlin.kothub.adapters.PinnedRepositoryAdapter
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import gitlin.kothub.adapters.OrganizationSummaryAdapter
import gitlin.kothub.github.api.viewerSummary
import gitlin.kothub.utilities.createFragment


class UserProfileActivity : AppCompatActivity(), AnkoLogger, LifecycleRegistryOwner {

    private val registry = LifecycleRegistry(this)
    override fun getLifecycle() = registry

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