package gitlin.kothub

import android.accounts.AccountManager
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.NotificationService
import gitlin.kothub.ui.ActivityLauncher
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class LauncherActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var notificationReceiver: NotificationReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)

        LocalBroadcastManager.getInstance(this).registerReceiver(NotificationReceiver(), NotificationService.filter())
    }

    override fun onStart() {
        super.onStart()

        val authTokenType = getString(R.string.accountType)
        val am = AccountManager.get(this)
        val accounts = am.getAccountsByType(authTokenType)

        if (accounts.size > 0) {
            NotificationService.schedule(applicationContext)
            ActivityLauncher.startUserProfileActivity(this, "Astalaseven")
        }
        else {
            am.addAccount(authTokenType, authTokenType, null, null, this, {
                info("Account created")
                info(it)
            }, null)
        }

    }
}
