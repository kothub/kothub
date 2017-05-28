package gitlin.kothub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gitlin.kothub.github.api.userSummary
import kotlinx.android.synthetic.main.activity_profile.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class ProfileActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initProfile()
    }

    fun initProfile() {
        userSummary { error, summary ->
            if (summary == null) {
                debug("ERROR")
                debug(error?.response?.httpResponseMessage ?: "NO ERROR??")
            } else {
                debug(summary)

                username.text = summary.login
                description.text = summary.bio
                followers.text = summary.followers?.toString()
                stars.text = summary.starredRepositories?.toString()
            }
        }
    }
}
