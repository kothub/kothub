package gitlin.kothub.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.BroadcastReceiver
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
import android.support.v4.content.LocalBroadcastManager
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
import gitlin.kothub.github.api.ApiRateLimit
import gitlin.kothub.github.api.data.RateLimit
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.GithubStatus
import gitlin.kothub.services.NotificationService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.*
import java.text.SimpleDateFormat

inline fun BadgeStyle.whiteText () = this.withTextColor(Color.WHITE)!!

class ProfileImageListener(val onClick: () -> Unit): AccountHeader.OnAccountHeaderProfileImageListener {
    override fun onProfileImageClick(p0: View?, p1: IProfile<*>?, p2: Boolean): Boolean {
        onClick()
        return false
    }

    override fun onProfileImageLongClick(p0: View?, p1: IProfile<*>?, p2: Boolean) = false
}


fun AccountHeaderBuilder.withProfileImageClick (onClick: () -> Unit): AccountHeaderBuilder {
    this.withOnAccountHeaderProfileImageListener(ProfileImageListener(onClick))
    return this
}

val redStyle = BadgeStyle().whiteText().withColorRes(R.color.md_red_600)
val yellowStyle = BadgeStyle().whiteText().withColorRes(R.color.md_yellow_900)
val blueStyle = BadgeStyle().whiteText().withColorRes(R.color.md_blue_600)
val greenStyle = BadgeStyle().whiteText().withColorRes(R.color.md_green_600)

class AppDrawer(private val activity: AppCompatActivity, toolbar: Toolbar): LifecycleObserver, AnkoLogger {

    private var id = 0L
    private var currentRateLimit: RateLimit? = null
    private var disposables = CompositeDisposable()

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

    val notifs: PrimaryDrawerItem = PrimaryDrawerItem()
            .withName(R.string.title_notifications)
            .withBadge("0")
            .withBadgeStyle(blueStyle)
            .withIcon(Octicons.Icon.oct_bell)
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

    val status: SecondaryDrawerItem = SecondaryDrawerItem()
            .withName("GitHub API Status")
            .withSelectable(false)
            .withIcon(Octicons.Icon.oct_radio_tower)
            .withIdentifier(0)

    val sticky = mutableListOf(status)


    val header = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.color.colorPrimaryDark)
            .withCompactStyle(true)
            .withAlternativeProfileHeaderSwitching(false)
            .addProfiles(profile)
            .withProfileImageClick {
                Handler().postDelayed({
                   ActivityLauncher.startViewerProfileActivity(activity)
                }, 300)
            }
            .build()


    val drawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withAccountHeader(header)
            .withActionBarDrawerToggle(true)
            .withCloseOnClick(true)
            .withDelayDrawerClickEvent(250)
            .addDrawerItems(feed, issues, pulls, DividerDrawerItem(), notifs, DividerDrawerItem(), settings, DividerDrawerItem(), rate)
            .withOnDrawerItemClickListener { _, _, item ->
                when (item.identifier) {
                    issues.identifier -> navigateTo<IssuesActivity>()
                    pulls.identifier -> navigateTo<PullRequestsActivity>()
                    notifs.identifier -> navigateTo<NotificationActivity>()
                    else -> false
                }
            }
            .build()

    val statusReceiver = NotificationReceiver()

    fun update (item: AbstractDrawerItem<*, *>) {
        drawer.updateItem(item)
    }

    fun select (item: IDrawerItem<*, *>) {
        drawer.setSelection(item)
    }



    private inline fun <reified T: Any> navigateTo (vararg params: Pair<String, Any?>): Boolean {

        with(activity) {
            val intent = intentFor<T>(*params).singleTop().noHistory()
            startActivity(intent)
        }

        return false
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String) {
                Picasso.with(imageView.context).load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.with(imageView.context).cancelRequest(imageView)
            }
        })

        DrawerData.drawerInfo().subscribe {

            profile.withName(it.login).withEmail(it.email).withIcon(it.avatarUrl)

            if (it.email.isNullOrBlank()) {
                profile.withEmail(it.name)
            }

            issues.withBadge("${it.issues}")

            drawer.updateItem(issues)
            header.updateProfile(profile)
        }.addTo(disposables)


        ApiRateLimit.observable().subscribe {

            this.currentRateLimit = it
            rate.withBadge("${it.remaining}/${it.limit}")
            update(rate)
        }.addTo(disposables)


        drawer.addStickyFooterItem(status)
        LocalBroadcastManager.getInstance(activity).registerReceiver(statusReceiver, NotificationService.filter())
        NotificationReceiver.apiStatus().subscribe({
            when (it) {
                GithubStatus.GOOD -> status.withBadge(R.string.github_status_good).withBadgeStyle(greenStyle)
                GithubStatus.MINOR -> status.withBadge(R.string.github_status_minor).withBadgeStyle(yellowStyle)
                GithubStatus.MAJOR -> status.withBadge(R.string.github_status_major).withBadgeStyle(redStyle)
            }

            drawer.updateStickyFooterItem(status)
        }, {
            throw it
        }).addTo(disposables)

        DrawerData.fetch()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy () {
        disposables.dispose()
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(statusReceiver)
    }
}

