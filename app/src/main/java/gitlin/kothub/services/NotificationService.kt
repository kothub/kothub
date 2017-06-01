package gitlin.kothub.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.byString
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import android.support.v4.content.LocalBroadcastManager



class NotificationService: IntentService("notification-service") {

    companion object {

        // Defines a custom Intent action
        val BROADCAST_ACTION = "gitlin.kothub.services.BROADCAST"

        // Defines the key for the status "extra" in an Intent
        val GITHUB_STATUS = "gitlin.kothub.services.STATUS"

    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("NotificationService", "onHandleIntent")

        Fuel.get("https://status.github.com/api/last-message.json")
                .responseString { request, response, result ->
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
        val localIntent = Intent(BROADCAST_ACTION)
                // Puts the status into the Intent
                .putExtra(GITHUB_STATUS, status)
        // Broadcasts the Intent to receivers in this app.
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
    MAJOR("major")
}