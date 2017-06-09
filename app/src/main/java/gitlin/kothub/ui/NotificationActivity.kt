package gitlin.kothub.ui

import android.os.Bundle

import org.jetbrains.anko.AnkoLogger
import android.view.Gravity
import android.widget.*
import gitlin.kothub.R
import gitlin.kothub.adapters.NotificationAdapter
import gitlin.kothub.github.api.data.Notifications
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.activity_notifs.*
import gitlin.kothub.github.api.*
import gitlin.kothub.ui.drawer.AppDrawer

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
        getService<ViewerService>()
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