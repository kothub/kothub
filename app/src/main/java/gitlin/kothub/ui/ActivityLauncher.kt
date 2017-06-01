package gitlin.kothub.ui
import android.content.Context
import gitlin.kothub.R
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.noHistory
import org.jetbrains.anko.singleTop


fun ProfileActivity.getProfileName (): String? = intent.getStringExtra(getString(R.string.profile_intent_login))


object ActivityLauncher {

    /**
     * Starts the profile activity, pass the name of the user to fetch. If null
     * the activity is supposed to fetch the viewer (logged in user)
     */
    fun startProfileActivity (context: Context, name: String? = null) {

        val key = context.getString(R.string.profile_intent_login)
        val value = name

        val intent = context.intentFor<ProfileActivity>(key to value).singleTop().noHistory()
        context.startActivity(intent)
    }
}