package gitlin.kothub

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import gitlin.kothub.R.layout.activity_main
import gitlin.kothub.accounts.TokenStore
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import gitlin.kothub.receivers.NotificationReceiver
import gitlin.kothub.services.NotificationService
import org.jetbrains.anko.info


class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var notificationReceiver: NotificationReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        setSupportActionBar(toolbar)

        LocalBroadcastManager.getInstance(this).registerReceiver(NotificationReceiver(), NotificationService.filter())
    }

    override fun onStart() {
        super.onStart()

        val authTokenType = getString(R.string.accountType)
        val am = AccountManager.get(this)
        val accounts = am.getAccountsByType(authTokenType)

        if (accounts.size > 0) {
            val account = accounts[0]

            val token = TokenStore.get(this).getToken()
            info(token)

        }
        else {

            am.addAccount(authTokenType, authTokenType, null, null, this, {
                info("Account created")
                info(it)
            }, null)
        }

       // if (OAuthValues.isLoggedIn) {
         //   ActivityLauncher.startUserProfileActivity(this, "Astalaseven")
        //} else {
         //   startActivity(Intent(this, LoginActivity::class.java))
        //}

        //NotificationService.schedule(applicationContext)
    }
}
