package gitlin.kothub.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.mikepenz.iconics.utils.IconicsMenuInflatorUtil
import gitlin.kothub.R
import gitlin.kothub.ui.drawer.AppDrawer
import org.jetbrains.anko.AnkoLogger


/**
 * Temporary class until lifecycles are implemented in AppCompatActivity
 */
open class LifecycleAppCompatActivity: AppCompatActivity(), LifecycleRegistryOwner, AnkoLogger {


    private val registry = LifecycleRegistry(this)

    lateinit var drawer: AppDrawer

    override fun getLifecycle(): LifecycleRegistry = registry

    protected fun initDrawer (context: LifecycleAppCompatActivity, toolbar: Toolbar) {
        this.drawer = AppDrawer(context, toolbar)
    }

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