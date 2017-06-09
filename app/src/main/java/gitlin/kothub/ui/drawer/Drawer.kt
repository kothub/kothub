package gitlin.kothub.ui.drawer

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.octicons_typeface_library.Octicons
import com.squareup.picasso.Picasso
import gitlin.kothub.R
import gitlin.kothub.accounts.getUserEmail
import gitlin.kothub.accounts.getUserLogin
import gitlin.kothub.accounts.getUserName
import gitlin.kothub.accounts.getUserPicture
import gitlin.kothub.github.api.ApiRateLimit
import gitlin.kothub.github.api.data.RateLimit
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.GithubStatus
import gitlin.kothub.services.NotificationService
import gitlin.kothub.ui.ActivityLauncher
import gitlin.kothub.ui.NotificationActivity
import gitlin.kothub.ui.issues.IssuesActivity
import gitlin.kothub.ui.pulls.PullRequestsActivity
import gitlin.kothub.ui.settings.SettingsActivity
import gitlin.kothub.ui.LifecycleAppCompatActivity
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.*

fun BadgeStyle.whiteText () = this.withTextColor(android.graphics.Color.WHITE)!!

class ProfileImageListener(val onClick: () -> Unit): AccountHeader.OnAccountHeaderProfileImageListener {
    override fun onProfileImageClick(p0: android.view.View?, p1:IProfile<*>?, p2: Boolean): Boolean {
        onClick()
        return false
    }

    override fun onProfileImageLongClick(p0: android.view.View?, p1: IProfile<*>?, p2: Boolean) = false
}


fun AccountHeaderBuilder.withProfileImageClick (onClick: () -> Unit): AccountHeaderBuilder {
    this.withOnAccountHeaderProfileImageListener(ProfileImageListener(onClick))
    return this
}

val redStyle = BadgeStyle().whiteText().withColorRes(R.color.md_red_600)
val yellowStyle = BadgeStyle().whiteText().withColorRes(R.color.md_yellow_900)
val blueStyle = BadgeStyle().whiteText().withColorRes(R.color.md_blue_600)
val greenStyle = BadgeStyle().whiteText().withColorRes(R.color.md_green_600)

class AppDrawer(private val activity: LifecycleAppCompatActivity, toolbar: Toolbar): LifecycleObserver, AnkoLogger {


    init {
        activity.lifecycle.addObserver(this)
    }

    private var id = 0L
    private var currentRateLimit: RateLimit? = null
    private var disposables = io.reactivex.disposables.CompositeDisposable()

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
                else {

                    val date = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(currentRateLimit?.resetAt)
                    val hour = java.text.SimpleDateFormat("HH:mm").format(date)

                    activity.alert("The rate limit is the number of points remaining on your API account. " +
                                   "It resets every hour. If you somehow reach zero, you will not be able to use the application until $hour GMT+00:00") {
                        okButton {  }
                    }.show()

                    true
                }
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
                android.os.Handler().postDelayed({
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
                    settings.identifier -> { activity.startActivity(activity.intentFor<SettingsActivity>()); false }
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


    @android.arch.lifecycle.OnLifecycleEvent(android.arch.lifecycle.Lifecycle.Event.ON_CREATE)
    fun onCreate() {

        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String) {
                Picasso.with(imageView.context).load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.with(imageView.context).cancelRequest(imageView)
            }
        })

        val login = getUserLogin(activity)
        val email = getUserEmail(activity)
        val avatarUrl = getUserPicture(activity)


        profile.withName(login).withEmail(email).withIcon(avatarUrl)

        if (email.isNullOrBlank()) {
            profile.withEmail(getUserName(activity))
        }

        header.updateProfile(profile)

        DrawerData.totalIssues().subscribe {
            issues.withBadge("${it.issues}")
            drawer.updateItem(issues)
        }.addTo(disposables)


       ApiRateLimit.observable().subscribe {

            this.currentRateLimit = it
            rate.withBadge("${it.remaining}/${it.limit}")
            update(rate)
        }.addTo(disposables)


        drawer.addStickyFooterItem(status)
        LocalBroadcastManager.getInstance(activity).registerReceiver(statusReceiver, NotificationService.Companion.filter())
        NotificationReceiver.Companion.apiStatus().subscribe({
            when (it) {
                GithubStatus.UNKNOWN -> status.withBadge("Unknown").withBadgeStyle(blueStyle)
                GithubStatus.GOOD -> status.withBadge(R.string.github_status_good).withBadgeStyle(greenStyle)
                GithubStatus.MINOR -> status.withBadge(R.string.github_status_minor).withBadgeStyle(yellowStyle)
                GithubStatus.MAJOR -> status.withBadge(R.string.github_status_major).withBadgeStyle(redStyle)
            }

            drawer.updateStickyFooterItem(status)
        }, {
            throw it
        }).addTo(disposables)

        DrawerData.fetch(activity)
    }

    @OnLifecycleEvent(android.arch.lifecycle.Lifecycle.Event.ON_DESTROY)
    fun onDestroy () {
        disposables.dispose()
        android.support.v4.content.LocalBroadcastManager.getInstance(activity).unregisterReceiver(statusReceiver)
    }
}

