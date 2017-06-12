package gitlin.kothub.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.AbsListView
import android.widget.ProgressBar
import gitlin.kothub.R
import gitlin.kothub.adapters.NotificationAdapter
import gitlin.kothub.github.api.UserService
import gitlin.kothub.github.api.data.Notifications
import gitlin.kothub.github.api.getService
import gitlin.kothub.ui.drawer.AppDrawer
import kotlinx.android.synthetic.main.activity_notifs.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger

class NotificationActivity : LifecycleAppCompatActivity(), AnkoLogger {

    lateinit var drawer: AppDrawer

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

        drawer = AppDrawer(this, toolbar)
        lifecycle.addObserver(drawer)
        drawer.select(drawer.notifs)

        val progressBar = ProgressBar(this)

        progressBar.layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        progressBar.isIndeterminate = true

        notifs.emptyView = progressBar
        listLayout.addView(progressBar)
        initProfile()
    }

    fun initProfile() {
        getService<UserService>()
                .notifications()
                .subscribe(
                        {
                            this.notifications = it
                        },
                        {

                        }
                )

//        notifications { error, notifs ->
//            if (error != null || notifs == null) {
//                debug("ERROR")
//                debug(error?.response?.httpResponseMessage ?: "NO ERROR??")
//            } else {
//                debug(notifs)
//                this.notifications = notifs
//            }
//        }
    }
}