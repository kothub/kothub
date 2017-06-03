package gitlin.kothub

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.github.LoginActivity
import gitlin.kothub.github.OAuthValues
import gitlin.kothub.ui.ActivityLauncher
import gitlin.kothub.utilities.getOAuthToken
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.NotificationService
import gitlin.kothub.utilities.getAlarmManager
import org.jetbrains.anko.intentFor
import java.util.*


class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var notificationReceiver: NotificationReceiver

    fun initOAuth () {
        OAuthValues.REDIRECT_URL = "oauth://kothub"
        OAuthValues.GITHUB_CLIENT = getString(R.string.github_client)
        OAuthValues.GITHUB_SECRET = getString(R.string.github_secret)

        val token: String? = getOAuthToken()
        if (token != null) {
            OAuthValues.GITHUB_TOKEN = token
            OAuthValues.isLoggedIn = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)

        LocalBroadcastManager.getInstance(this).registerReceiver(NotificationReceiver(), NotificationService.filter())
    }

    override fun onStart() {
        super.onStart()
        initOAuth()

        if (OAuthValues.isLoggedIn) {
            ActivityLauncher.startUserProfileActivity(this, "Astalaseven")
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        scheduleAlarm()
    }


    fun scheduleAlarm() {
        val intent = applicationContext.intentFor<NotificationService>()
        val pendingIntent = PendingIntent.getService(this@MainActivity, 0, intent, 0)
        val alarm = getAlarmManager()
        pendingIntent.cancel()
        alarm.cancel(pendingIntent)
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent)
    }
}
