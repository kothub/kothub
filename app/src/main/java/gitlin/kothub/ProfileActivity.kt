package gitlin.kothub

import android.app.Activity
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.userSummary
import kotlinx.android.synthetic.main.activity_profile.*
import gitlin.kothub.utilities.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import java.net.URL
import kotlin.properties.Delegates
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import gitlin.kothub.adapters.PinnedRepositoryAdapter
import gitlin.kothub.github.api.data.PinnedRepository


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
                val imageLoader = ImageLoader.getInstance()
                imageLoader.displayImage(value.avatarUrl, imageView)

                pinned.adapter = PinnedRepositoryAdapter(this, value.pinnedRepositories)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))

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
