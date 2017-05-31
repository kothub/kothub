package gitlin.kothub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import gitlin.kothub.github.api.data.UserSummary
import gitlin.kothub.github.api.userSummary
import kotlinx.android.synthetic.main.activity_notifs.*
import gitlin.kothub.utilities.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import android.view.Gravity
import android.widget.*
import gitlin.kothub.adapters.NotificationAdapter
import gitlin.kothub.adapters.PinnedRepositoryAdapter
import gitlin.kothub.github.api.data.Notification
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.ui.AppDrawer
import gitlin.kothub.ui.DrawerData
import kotlinx.android.synthetic.main.toolbar.*
import gitlin.kothub.github.api.*

class NotificationActivity : AppCompatActivity(), AnkoLogger {


    var notifications: Notifications? = null
        set(value) {
            if (value != null) {

                notifs.adapter = NotificationAdapter(this, value.notifications)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifs)
        setSupportActionBar(toolbar)

        //AppDrawer(this, toolbar)

        val progressBar = ProgressBar(this)

        progressBar.layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        progressBar.isIndeterminate = true

        notifs.emptyView = progressBar
        listLayout.addView(progressBar)
        initProfile()
    }

    fun initProfile() {
        notifications { error, notifs ->
            if (error != null || notifs == null) {
                debug("ERROR")
                debug(error?.response?.httpResponseMessage ?: "NO ERROR??")
            } else {
                debug(notifs)
                this.notifications = notifs
            }
        }
    }
}