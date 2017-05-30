package gitlin.kothub.ui

import android.util.Log
import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.drawerInfo
import gitlin.kothub.github.api.feeds
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger


class DrawerData {

    companion object: AnkoLogger {
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

                feeds {
                    error, result ->
                    if (error == null && result != null) {
                        Log.d("DELETE_ME", error)
                    }
                    else {
                        info.onError(error)
                    }
                }
            }
        }

    }
}