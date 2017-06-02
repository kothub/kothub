package gitlin.kothub.receivers

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import gitlin.kothub.services.GithubStatus
import gitlin.kothub.services.NotificationService.Companion.GITHUB_STATUS
import gitlin.kothub.services.NotificationService.Companion.RESULT_CODE
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


object NotificationReceiver: BroadcastReceiver() {

    private val status = BehaviorSubject.createDefault(GithubStatus.GOOD)

    fun apiStatus(): Observable<GithubStatus> = status.distinctUntilChanged()

    override fun onReceive(context: Context, intent: Intent?) {
        val resultCode = intent?.getIntExtra(RESULT_CODE, RESULT_CANCELED)
        if (resultCode == RESULT_OK) {
            val status = intent.getStringExtra(GITHUB_STATUS)
            this.status.onNext(GithubStatus.valueOf(status.toUpperCase()))
        }
    }
}
