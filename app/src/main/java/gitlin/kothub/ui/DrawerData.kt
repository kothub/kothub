package gitlin.kothub.ui

import android.util.Log
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.drawerInfo
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger


object DrawerData {

    val info: BehaviorSubject<DrawerInfo> = BehaviorSubject.create()

    fun drawerInfo () = info

    fun fetch () {
        if (!info.hasValue()) {
            drawerInfo {
                error, result ->
                    if (error == null && result != null) {
                        info.onNext(result)
                    }
                    else {
                        info.onError(error)
                    }
            }
        }
    }
}