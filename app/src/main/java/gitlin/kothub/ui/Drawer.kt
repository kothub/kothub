package gitlin.kothub.ui

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import gitlin.kothub.R
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.octicons_typeface_library.Octicons
import com.squareup.picasso.Picasso
import gitlin.kothub.ProfileActivity
import gitlin.kothub.github.api.ApiRateLimit
import gitlin.kothub.github.api.data.RateLimit
import org.jetbrains.anko.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AppDrawer(private val activity: AppCompatActivity, toolbar: Toolbar): AnkoLogger {

    private val redStyle = BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_600)
    private val blueStyle = BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_blue_600)
    private val greenStyle = BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_green_600)
    private var id = 0L
    private var currentRateLimit: RateLimit? = null

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
            .withBadgeStyle(greenStyle)
            .withIcon(GoogleMaterial.Icon.gmd_network_locked)
            .withIdentifier(id++)
            .withSelectable(false)
            .withOnDrawerItemClickListener { _, _, _ ->

                if (currentRateLimit == null) {
                    false
                }

                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(currentRateLimit?.resetAt)
                val hour = SimpleDateFormat("HH:mm").format(date)

                activity.alert("The rate limit is the number of points remaining on your API account. " +
                               "It resets every hour. If you somehow reach zero, you will not be able to use the application until $hour GMT+00:00") {
                    okButton {  }
                }.show()

                true
            }


    val settings: SecondaryDrawerItem = SecondaryDrawerItem()
            .withName(R.string.settings)
            .withIcon(GoogleMaterial.Icon.gmd_settings)
            .withIdentifier(id++)


    inner class ProfileImageListener: AccountHeader.OnAccountHeaderProfileImageListener {
        override fun onProfileImageClick(p0: View?, p1: IProfile<*>?, p2: Boolean): Boolean {
            Handler().postDelayed({
                this@AppDrawer.navigateTo<ProfileActivity>()
            }, 300)

            return false
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
            .withCloseOnClick(true)
            .withDelayDrawerClickEvent(250)
            .addDrawerItems(feed, issues, pulls, DividerDrawerItem(), settings, DividerDrawerItem(), rate)
            .withOnDrawerItemClickListener { _, _, item ->
                when (item.identifier) {
                    issues.identifier -> navigateTo<IssuesActivity>()
                    pulls.identifier -> navigateTo<PullRequestsActivity>()
                    else -> false
                }
            }
            .build()

    fun update (item: AbstractDrawerItem<*, *>) {
        drawer.updateItem(item)
    }

    fun select (item: IDrawerItem<*, *>) {
        drawer.setSelection(item)
    }

    private inline fun <reified T: Any> navigateTo (): Boolean {

        with(activity) {
            val intent = intentFor<T>().singleTop()
            startActivity(intent)
        }

        return false
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

            this.currentRateLimit = it
            rate.withBadge("${it.remaining}/${it.limit}")
            update(rate)
        }

        DrawerData.fetch()
    }
}

