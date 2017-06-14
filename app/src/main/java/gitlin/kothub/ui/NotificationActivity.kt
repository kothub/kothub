package gitlin.kothub.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import gitlin.kothub.R
import gitlin.kothub.adapters.NotificationAdapter
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.github.api.notifications
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_notifs.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class NotificationActivity : LifecycleAppCompatActivity(), AnkoLogger {

    lateinit var drawer: AppDrawer

    var notifications: Notifications? = null
        set(value) {
            if (value != null) {

                notifs.adapter = NotificationAdapter(this, value.notifications)
                notifs.layoutManager = LinearLayoutManager(this)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifs)
        setSupportActionBar(toolbar)

        drawer = AppDrawer(this, toolbar)
        lifecycle.addObserver(drawer)
        drawer.select(drawer.notifs)

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