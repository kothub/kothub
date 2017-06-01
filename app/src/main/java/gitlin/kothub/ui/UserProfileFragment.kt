package gitlin.kothub.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kittinunf.fuel.core.FuelError
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso

import gitlin.kothub.R
import gitlin.kothub.adapters.OrganizationSummaryAdapter
import gitlin.kothub.adapters.PinnedRepositoryAdapter
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.userSummary
import gitlin.kothub.github.api.viewerSummary
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


class UserProfileFragment : Fragment(), AnkoLogger {

    var user: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments[LOGIN] as? String
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user != null) {
            organizationsTitle.visibility = TextView.INVISIBLE
        }
        initProfile()
    }

    fun updateView (summary: UserSummary) {
        username.value = summary.login
        description.value = summary.bio
        followers.value = summary.followers
        stars.value = summary.starredRepositories
        repos.value = summary.repositories
        following.value = summary.following

        Picasso.with(imageView.context).load(summary.avatarUrl).into(imageView)

        val snap = GravitySnapHelper(Gravity.START)
        snap.attachToRecyclerView(pinned)
        pinned.setHasFixedSize(true)
        pinned.adapter = PinnedRepositoryAdapter(context, summary.pinnedRepositories)
        pinned.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (user == null) {
            val snapOrg = GravitySnapHelper(Gravity.START)
            snapOrg.attachToRecyclerView(organizations)
            organizations.setHasFixedSize(true)
            organizations.adapter = OrganizationSummaryAdapter(context, summary.organizations)
            organizations.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initProfile() {

        if (user == null) {
            viewerSummary { error, userSummary -> handleResult(error, userSummary) }
        }
        else {
            userSummary(user!!) { error, userSummary -> handleResult(error, userSummary)}
        }
    }

    private fun handleResult (error: FuelError?, summary: UserSummary?) {
        if (error != null || summary == null) {
            debug(error?.response?.httpResponseMessage ?: "NO ERROR??")
        } else {
            this.updateView(summary)
        }
    }


    companion object {

        val LOGIN: String = "LOGIN"

        fun newInstance(login: String? = null): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle()
            args.putString(LOGIN, login)
            fragment.arguments = args
            return fragment
        }
    }
}
