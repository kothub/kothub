package gitlin.kothub.ui

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import gitlin.kothub.R
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.octicons_typeface_library.Octicons
import com.squareup.picasso.Picasso
import gitlin.kothub.ProfileActivity
import gitlin.kothub.github.api.ApiRateLimit
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info


class AppDrawer(private val activity: AppCompatActivity, toolbar: Toolbar): AnkoLogger {

    private val redStyle = BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_600)
    private val blueStyle = BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_blue_600)
    private var id = 0L

    val profile: ProfileDrawerItem = ProfileDrawerItem().withIdentifier(id++)

    val feed: PrimaryDrawerItem = PrimaryDrawerItem()
            .withName(R.string.feed)
            .withIcon(GoogleMaterial.Icon.gmd_rss_feed)
            .withIdentifier(id++)

    val issues: PrimaryDrawerItem = PrimaryDrawerItem()
            .withName(R.string.issues)
            .withBadge("0")
            .withBadgeStyle(redStyle)
            .withIcon(Octicons.Icon.oct_issue_opened)
            .withIdentifier(id++)

    val pulls: PrimaryDrawerItem = PrimaryDrawerItem()
            .withName(R.string.pulls)
            .withBadge("0")
            .withBadgeStyle(blueStyle)
            .withIcon(Octicons.Icon.oct_git_pull_request)
            .withIdentifier(id++)

    val rate: SecondaryDrawerItem = SecondaryDrawerItem()
            .withName(R.string.rate_limit)
            .withBadge("5000")
            .withBadgeStyle(blueStyle)
            .withIcon(GoogleMaterial.Icon.gmd_network_locked)
            .withIdentifier(id++)


    val settings: SecondaryDrawerItem = SecondaryDrawerItem()
            .withName(R.string.settings)
            .withIcon(GoogleMaterial.Icon.gmd_settings)
            .withIdentifier(id++)


    inner class ProfileImageListener: AccountHeader.OnAccountHeaderProfileImageListener {
        override fun onProfileImageClick(p0: View?, p1: IProfile<*>?, p2: Boolean): Boolean {

            val drawer = this@AppDrawer
            if (drawer.activity.localClassName == ProfileActivity::class.java.simpleName) {
                return false
            }

            val intent = Intent(drawer.activity, ProfileActivity::class.java)
            drawer.activity.startActivity(intent)
            return true
        }

        override fun onProfileImageLongClick(p0: View?, p1: IProfile<*>?, p2: Boolean) = false

    }

    val header = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.color.colorPrimaryDark)
            .withCompactStyle(true)
            .withAlternativeProfileHeaderSwitching(false)
            .addProfiles(profile)
            .withOnAccountHeaderProfileImageListener(ProfileImageListener())
            .build()


    val drawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withAccountHeader(header)
            .withActionBarDrawerToggle(true)
            .addDrawerItems(feed, issues, pulls, DividerDrawerItem(), settings, DividerDrawerItem(), rate)
            .build()

    fun update (item: AbstractDrawerItem<*, *>) {
        drawer.updateItem(item)
    }

    init {

        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String) {
                Picasso.with(imageView.context).load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.with(imageView.context).cancelRequest(imageView)
            }
        })





        // TODO: unsubscribe
        DrawerData.drawerInfo().subscribe {

            profile.withName(it.login).withEmail(it.email).withIcon(it.avatarUrl)

            if (it.email.isNullOrBlank()) {
                profile.withEmail(it.name)
            }

            issues.withBadge("${it.issues}")

            drawer.updateItem(issues)
            header.updateProfile(profile)
        }


        // TODO: unsubscribe
        ApiRateLimit.observable().subscribe {

            info("IS IT CALLED?")

            rate.withBadge("${it.remaining}")
            update(rate)
        }

        DrawerData.fetch()
    }
}

