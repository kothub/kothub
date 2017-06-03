package gitlin.kothub.ui

import android.content.Context
import android.util.Log
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.drawerInfo
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger


object DrawerData {

    val info: BehaviorSubject<DrawerInfo> = BehaviorSubject.create()

    fun drawerInfo () = info

    fun fetch (context: Context) {
        if (!info.hasValue()) {
            context.drawerInfo().subscribe { result ->
                info.onNext(result)
            }

            context.drawerInfo().subscribe(
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