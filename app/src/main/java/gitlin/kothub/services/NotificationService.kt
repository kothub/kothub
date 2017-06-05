package gitlin.kothub.services

import android.app.Activity
import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.byString
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gitlin.kothub.utilities.getAlarmManager
import org.jetbrains.anko.intentFor


class NotificationService: IntentService("notification-service") {

    companion object {

        val BROADCAST_ACTION = "gitlin.kothub.services.BROADCAST"

        val GITHUB_STATUS = "gitlin.kothub.services.STATUS"

        val RESULT_CODE = "gitlin.kothub.services.CODE"

        fun filter() = IntentFilter(BROADCAST_ACTION)

        fun schedule (applicationContext: Context, delay: Long = 60000) {
            val intent = applicationContext.intentFor<NotificationService>()
            val pendingIntent = PendingIntent.getService(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarm = applicationContext.getAlarmManager()
            pendingIntent.cancel()
            alarm.cancel(pendingIntent)
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), delay, pendingIntent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("NotificationService", "onHandleIntent")

        Fuel.get("https://status.github.com/api/last-message.json")
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> Log.e("NotificationService", result.error.message)
                    is Result.Success -> {
                        val json: JsonObject = JsonParser().parse(result.value).asJsonObject

                        val status: Status = Status(json)
                        if (status.status == GithubStatus.GOOD.status) {
                            Log.d("NotificationService", Status(json).status)
                        }

                        broadcast(Status(json).status)
                    }
                }
            }
    }

    fun broadcast(status: String) {
        val localIntent: Intent = Intent(BROADCAST_ACTION)
                .putExtra(RESULT_CODE, Activity.RESULT_OK)
                .putExtra(GITHUB_STATUS, status)

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
    }
}



data class Status(private val json: JsonObject) {
    var status: String by json.byString
    var body: String by json.byString
    var lastUpdated: String by json.byString { "last_updated" }
}

enum class GithubStatus(val status: String) {
    GOOD("good"),
    MINOR("minor"),
    MAJOR("major");

}

