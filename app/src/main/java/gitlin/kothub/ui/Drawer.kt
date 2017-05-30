package gitlin.kothub.ui

import android.graphics.Color
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
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


class AppDrawer(activity: AppCompatActivity, toolbar: Toolbar): AnkoLogger {

    private val badgeStyle = BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_600)

    val profile: ProfileDrawerItem = ProfileDrawerItem().withIdentifier(1)

    val feed: PrimaryDrawerItem = PrimaryDrawerItem()
            .withName(R.string.feed)
            .withIcon(GoogleMaterial.Icon.gmd_rss_feed)
            .withIdentifier(2)

    val issues: PrimaryDrawerItem = PrimaryDrawerItem()
            .withName(R.string.issues)
            .withBadge("0")
            .withBadgeStyle(badgeStyle)
            .withIcon(GoogleMaterial.Icon.gmd_bug_report)
            .withIdentifier(3)


    val settings: SecondaryDrawerItem = SecondaryDrawerItem()
            .withName(R.string.settings)
            .withIcon(GoogleMaterial.Icon.gmd_settings)
            .withIdentifier(4)

    init {

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
                .addDrawerItems(feed, issues, settings)
                .build()

        DrawerData.drawerInfo().subscribe {

            profile.withName(it.login).withEmail(it.email).withIcon(it.avatarUrl)

            issues.withBadge("${it.issues}")

            drawer.updateItem(issues)
            header.updateProfile(profile)
        }


        DrawerData.fetch()
    }
}

