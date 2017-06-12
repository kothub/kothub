package gitlin.kothub.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.mikepenz.iconics.utils.IconicsMenuInflatorUtil
import gitlin.kothub.R
import org.jetbrains.anko.AnkoLogger

/**
 * Temporary class until lifecycles are implemented in AppCompatActivity
 */
open class LifecycleAppCompatActivity: AppCompatActivity(), LifecycleRegistryOwner, AnkoLogger {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        IconicsMenuInflatorUtil.inflate(menuInflater, this, R.menu.base_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                onSearchRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}