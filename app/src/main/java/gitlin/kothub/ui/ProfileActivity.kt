package gitlin.kothub.ui

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


class ProfileActivity : AppCompatActivity(), AnkoLogger {

    lateinit var drawer: AppDrawer
    var user: String? = null

    var summary: UserSummary? = null
        set(value) {
            if (value != null) {
                username.value = value.login
                description.value = value.bio
                followers.value = value.followers
                stars.value = value.starredRepositories
                repos.value = value.repositories
                following.value = value.following

                Picasso.with(imageView.context).load(value.avatarUrl).into(imageView)

                val snap = GravitySnapHelper(Gravity.START)
                snap.attachToRecyclerView(pinned)
                pinned.setHasFixedSize(true)
                pinned.adapter = PinnedRepositoryAdapter(this, value.pinnedRepositories)
                pinned.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                if (user == null) {
                    val snapOrg = GravitySnapHelper(Gravity.START)
                    snapOrg.attachToRecyclerView(organizations)
                    organizations.setHasFixedSize(true)
                    organizations.adapter = OrganizationSummaryAdapter(this, value.organizations)
                    organizations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        user = getProfileName()
        if (user != null) {
            organizationsTitle.visibility = TextView.INVISIBLE
        }

        drawer = AppDrawer(this, toolbar)

        initProfile()
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.profile)
    }

    fun initProfile() {

        if (user == null) {
            viewerSummary { error, userSummary -> handleResult(error, userSummary) }
        }
        else {
            userSummary(user!!) { error, userSummary -> handleResult(error, userSummary)}
        }
    }

    fun handleResult (error: FuelError?, summary: UserSummary?) {
        if (error != null || summary == null) {
            debug(error?.response?.httpResponseMessage ?: "NO ERROR??")
        } else {
            this.summary = summary
        }
    }
}
