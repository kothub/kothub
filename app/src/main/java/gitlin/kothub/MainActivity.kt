package gitlin.kothub

import android.app.Activity
import android.app.AlarmManager
import android.app.IntentService
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.github.LoginActivity
import gitlin.kothub.github.OAuthValues
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.NotificationService
import gitlin.kothub.utilities.getOAuthToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import android.content.Context.ALARM_SERVICE
import android.app.PendingIntent
import android.os.SystemClock


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
        val intent: Intent = Intent(this, NotificationService::class.java)
        //intent.putExtra("receiver", notificationReceiver)
        startService(intent)
    }

    fun scheduleAlarm() {
        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(this, NotificationReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val firstMillis = SystemClock.elapsedRealtime()
        val alarm = this.getSystemService(ALARM_SERVICE) as AlarmManager
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                60 * 1000, pIntent)
    }
}
