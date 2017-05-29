package gitlin.kothub.ui

import gitlin.kothub.github.api.data.DrawerInfo
import gitlin.kothub.github.api.drawerInfo
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info


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
            }
        }

    }
}