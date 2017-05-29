package gitlin.kothub.ui

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
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.squareup.picasso.Picasso
import gitlin.kothub.github.api.data.DrawerInfo
import io.reactivex.Observer
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import java.util.function.Consumer


class AppDrawer(activity: AppCompatActivity, toolbar: Toolbar): AnkoLogger {

    val profile: ProfileDrawerItem = ProfileDrawerItem().withIdentifier(1)

    init {

        debug("TEST")

        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String) {
                Picasso.with(imageView.context).load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.with(imageView.context).cancelRequest(imageView)
            }
        })



        val header = AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.colorPrimaryDark)
                .withAlternativeProfileHeaderSwitching(false)
                .withCompactStyle(true)
                .addProfiles(profile)
                .build()


        val drawer = DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                    PrimaryDrawerItem().withName("Feed")
                                .withIcon(GoogleMaterial.Icon.gmd_rss_feed).withIdentifier(2),
                    PrimaryDrawerItem().withName("Issues")
                            .withIcon(GoogleMaterial.Icon.gmd_bug_report).withIdentifier(3),
                    DividerDrawerItem(),
                    SecondaryDrawerItem().withName("Settings")
                            .withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(4)
                )
                .build()

        DrawerData.drawerInfo().subscribe {

            profile.withName(it.login)

            if (it.email != null && it.email != "") {
                profile.withEmail(it.email)
            }

            profile.withIcon(it.avatarUrl)

            header.updateProfile(profile)
        }


        DrawerData.fetch()
    }
}

