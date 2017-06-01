package gitlin.kothub.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import gitlin.kothub.services.NotificationService

class NotificationReceiver: BroadcastReceiver() {
    companion object {
        var REQUEST_CODE = 12345
        val ACTION = "gitlin.kothub.receivers.alarm"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, NotificationService::class.java)
        i.putExtra("foo", "bar")
        context?.startService(i)
    }

}

//class NotificationReceiver(private val init: (Context?, Intent?) -> Unit): BroadcastReceiver() {
//
//    private var receiver: Receiver
//        get() = receiver
//        set(value) { this.receiver = value }
//
//    override fun onReceive(context: Context?, intent: Intent?) {
//        init(context, intent)
//    }
//
//    fun onReceiveResult(resultCode: Int, resultData: Bundle) {
//        receiver.onReceiveResult(resultCode, resultData)
//    }
//
//}

interface Receiver {
    fun onReceiveResult(resultCode: Int, resultData: Bundle)
}