package gitlin.kothub.ui.drawer

import android.content.Context
import gitlin.kothub.github.api.UserService
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.getService
import io.reactivex.subjects.BehaviorSubject


object DrawerData {

    val info: BehaviorSubject<DrawerInfo> = BehaviorSubject.create()

    fun totalIssues() = info

    fun fetch (context: Context, update: Boolean = false) {
        if (!info.hasValue() || update) {
            context
            .getService<UserService>()
            .totalIssues()
            .subscribe(
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