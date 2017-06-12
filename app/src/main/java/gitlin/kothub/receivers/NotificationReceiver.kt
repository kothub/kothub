package gitlin.kothub.receivers

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import gitlin.kothub.services.GithubStatus
import gitlin.kothub.services.NotificationService.Companion.GITHUB_STATUS
import gitlin.kothub.services.NotificationService.Companion.RESULT_CODE
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


class NotificationReceiver: BroadcastReceiver() {

    companion object {
        private val status = BehaviorSubject.createDefault(GithubStatus.UNKNOWN)
        fun apiStatus(): Observable<GithubStatus> = status.distinctUntilChanged()
    }


    override fun onReceive(context: Context, intent: Intent?) {
        val resultCode = intent?.getIntExtra(RESULT_CODE, RESULT_CANCELED)
        if (resultCode == RESULT_OK) {
            val status = intent.getStringExtra(GITHUB_STATUS)
            NotificationReceiver.status.onNext(GithubStatus.valueOf(status.toUpperCase()))
        }
    }
}
