package gitlin.kothub

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.github.LoginActivity
import gitlin.kothub.github.OAuthValues
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.NotificationService
import gitlin.kothub.utilities.getAlarmManager
import gitlin.kothub.utilities.getOAuthToken
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger


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

        val filter = IntentFilter(NotificationService.BROADCAST_ACTION)

        val notificationReceiver = NotificationReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, filter)
    }

    override fun onStart() {
        super.onStart()
        initOAuth()

        if (OAuthValues.isLoggedIn) {
            startActivity(Intent(this, ProfileActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //launchNotificationService()
        scheduleAlarm()
    }

    fun launchNotificationService() {
        val intent = Intent(applicationContext, NotificationService::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startService(intent)
    }

    fun scheduleAlarm() {
        val intent = Intent(applicationContext, NotificationService::class.java)
        val pendingIntent = PendingIntent.getService(this@MainActivity, 0, intent, 0)
        val alarm = getAlarmManager()
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), (30 * 1000).toLong(), pendingIntent)
    }
}
