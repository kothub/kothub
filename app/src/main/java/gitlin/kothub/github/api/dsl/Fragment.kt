package gitlin.kothub.github.api.dsl

import android.util.Log
import com.google.gson.GsonBuilder


inline fun <reified T: Element> createFragment (noinline body: T.() -> Unit) = body
inline fun <reified T: Element> T.fragment(body: T.() -> Unit) = this.body()

