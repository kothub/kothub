package gitlin.kothub.ui

import android.arch.lifecycle.*
import android.content.Context
import android.os.Bundle
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
import gitlin.kothub.utilities.get
import gitlin.kothub.utilities.value
import io.reactivex.Single
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.jetbrains.anko.AnkoLogger

class UserViewModel: ViewModel() {

    val summary = MutableLiveData<UserSummary>()
    var user: String? = null


    fun loadUser(context: Context) {

        if (summary.value != null) {
            return
        }

        var obs: Single<UserSummary>

        if (user == null) {
            obs = context.viewerSummary()
        }
        else {
            obs = context.userSummary(user!!)
        }

        obs.subscribe(
            {
                this.summary.value = it
            },
            {
                throw it
            }
        )
    }
}


class UserProfileFragment : LifecycleFragment(), AnkoLogger {

    lateinit var model: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity).get<UserViewModel>()
        model.user = arguments[LOGIN] as? String
        model.loadUser(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (model.user != null) {
            organizationsTitle.visibility = TextView.INVISIBLE
        }
    }

    private fun updateView (summary: UserSummary) {

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

        if (model.user == null) {
            val snapOrg = GravitySnapHelper(Gravity.START)
            snapOrg.attachToRecyclerView(organizations)
            organizations.setHasFixedSize(true)
            organizations.adapter = OrganizationSummaryAdapter(context, summary.organizations)
            organizations.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.summary.observe(this, Observer { if (it != null) updateView(it) })
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