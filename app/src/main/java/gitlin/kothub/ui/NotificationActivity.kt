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
import kotlinx.android.synthetic.main.activity_notifs.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger

class NotificationActivity : LifecycleAppCompatActivity(), AnkoLogger {

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
        initDrawer(toolbar)

        val progressBar = ProgressBar(this)

        progressBar.layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        progressBar.isIndeterminate = true

        notifs.emptyView = progressBar
        listLayout.addView(progressBar)
        initProfile()
    }

    override fun onResume() {
        super.onResume()
        drawer.select(drawer.notifs)
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
    }
}