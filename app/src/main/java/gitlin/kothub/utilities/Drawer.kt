package gitlin.kothub.utilities

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import gitlin.kothub.R

fun setupDrawer (activity: AppCompatActivity, toolbar: Toolbar) {

    val header = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.color.colorPrimaryDark)
            .addProfiles(
                    ProfileDrawerItem()
                            .withName("Florian Knop")
                            .withEmail("florian_knop@hotmail.com")
            )
            .build()

    val drawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withAccountHeader(header)
            .withActionBarDrawerToggle(true)
            .addDrawerItems(
                PrimaryDrawerItem().withName("Feed")
                            .withIcon(GoogleMaterial.Icon.gmd_rss_feed),
                PrimaryDrawerItem().withName("Issues")
                        .withIcon(GoogleMaterial.Icon.gmd_bug_report),
                DividerDrawerItem(),
                SecondaryDrawerItem().withName("Settings")
                        .withIcon(GoogleMaterial.Icon.gmd_settings)
            )
            .build()
}