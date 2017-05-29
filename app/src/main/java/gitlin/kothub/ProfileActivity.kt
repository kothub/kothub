package gitlin.kothub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.userSummary
import kotlinx.android.synthetic.main.activity_profile.*
import gitlin.kothub.utilities.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import android.view.Gravity
import android.widget.*
import com.squareup.picasso.Picasso
import gitlin.kothub.adapters.PinnedRepositoryAdapter
import gitlin.kothub.ui.AppDrawer
import kotlinx.android.synthetic.main.toolbar.*


class ProfileActivity : AppCompatActivity(), AnkoLogger {


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

                pinned.adapter = PinnedRepositoryAdapter(this, value.pinnedRepositories)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        AppDrawer(this, toolbar)

        val progressBar = ProgressBar(this)

        progressBar.layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        progressBar.isIndeterminate = true

        pinned.emptyView = progressBar
        listLayout.addView(progressBar)
        initProfile()
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
