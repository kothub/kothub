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
import com.squareup.picasso.Picasso
import gitlin.kothub.R
import gitlin.kothub.adapters.PinnedRepositoryAdapter
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.SnapHelper
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import gitlin.kothub.adapters.OrganizationSummaryAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.jetbrains.anko.info


class ProfileActivity : AppCompatActivity(), AnkoLogger {

    lateinit var drawer: AppDrawer

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

                val snapOrg = GravitySnapHelper(Gravity.START)
                snapOrg.attachToRecyclerView(organizations)
                organizations.setHasFixedSize(true)
                organizations.adapter = OrganizationSummaryAdapter(this, value.organizations)
                organizations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        drawer = AppDrawer(this, toolbar)

        initProfile()
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.profile)
    }

    fun initProfile() {
        userSummary { error, summary ->
            if (error != null || summary == null) {
                debug("ERROR")
                debug(error?.response?.httpResponseMessage ?: "NO ERROR??")
            } else {
                debug(summary)
                this.summary = summary
            }
        }
    }
}
