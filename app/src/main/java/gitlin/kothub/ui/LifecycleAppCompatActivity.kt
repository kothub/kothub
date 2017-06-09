package gitlin.kothub.ui

import android.app.SearchManager
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.graphics.Color
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SearchViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.IconicsMenuInflatorUtil
import gitlin.kothub.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Temporary class until lifecycles are implemented in AppCompatActivity
 */
open class LifecycleAppCompatActivity: AppCompatActivity(), LifecycleRegistryOwner, AnkoLogger {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        IconicsMenuInflatorUtil.inflate(menuInflater, this, R.menu.base_menu, menu)

//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val menuItem = menu.findItem(R.id.action_search)
//
//        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView.setIconifiedByDefault(false)

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