package gitlin.kothub.receivers

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import gitlin.kothub.services.NotificationService.Companion.GITHUB_STATUS
import gitlin.kothub.services.NotificationService.Companion.RESULT_CODE


class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val resultCode = intent?.getIntExtra(RESULT_CODE, RESULT_CANCELED)
        if (resultCode == RESULT_OK) {
            val status = intent.getStringExtra(GITHUB_STATUS)

            Log.d("NotificationReceiver", status)
            Toast.makeText(context, status, Toast.LENGTH_LONG).show()
        }
    }
}
