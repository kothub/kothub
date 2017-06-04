package gitlin.kothub.ui

import android.content.Context
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.totalIssues
import io.reactivex.subjects.BehaviorSubject


object DrawerData {

    val info: BehaviorSubject<DrawerInfo> = BehaviorSubject.create()

    fun totalIssues() = info

    fun fetch (context: Context, update: Boolean = false) {
        if (!info.hasValue() || update) {
            context.totalIssues().subscribe(
                {
                    result -> info.onNext(result)
                },
                {
                    error -> info.onError(error)
                }
            )
        }
    }
}