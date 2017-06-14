package gitlin.kothub.utilities

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

inline fun <reified T: ViewModel> ViewModelProvider.get (): T {
    return this.get(T::class.java)
}