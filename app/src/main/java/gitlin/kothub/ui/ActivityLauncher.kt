package gitlin.kothub.ui
import android.content.Context
import gitlin.kothub.R
import org.jetbrains.anko.*


fun UserProfileActivity.getProfileName (): String? = intent.getStringExtra(getString(R.string.profile_intent_login))


object ActivityLauncher {

    /**
     * Starts the profile activity, pass the name of the user to fetch. If null
     * the activity is supposed to fetch the viewer (logged in user)
     */
    fun startUserProfileActivity (context: Context, name: String? = null) {

        val key = context.getString(R.string.profile_intent_login)
        val value = name

        val intent = context.intentFor<UserProfileActivity>(key to value).singleTop().clearTask()
        context.startActivity(intent)
    }

    fun startViewerProfileActivity (context: Context) {
        val intent = context.intentFor<ViewerProfileActivity>().singleTop().clearTask()
        context.startActivity(intent)
    }
}