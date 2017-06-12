package gitlin.kothub.accounts


import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import gitlin.kothub.R
import gitlin.kothub.github.api.UserService
import gitlin.kothub.github.api.getService
import gitlin.kothub.ui.user.UserProfileActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor


class LoginActivity: AccountAuthenticatorActivity(), AnkoLogger {


    companion object {
        const val ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE = "AUTH_TYPE"
        const val ARG_ACCOUNT_NAME = "ACCOUNT_NAME"
        const val ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT"
    }


    private lateinit var accountManager: AccountManager
    private lateinit var clientId: String
    private lateinit var clientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        accountManager = AccountManager.get(this)
        clientId = getString(R.string.github_client)
        clientSecret = getString(R.string.github_secret)

        if (clientId == "dummy_client" || clientSecret == "dummy_secret") {
            throw Exception("OAuth not configured on your machine")
        }
    }



    fun openWebView () {
        val intent = intentFor<LoginWebViewActivity>()
        info(authorizeUrl(clientId))
        intent.putExtra("url", authorizeUrl(clientId))
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        setContentView(R.layout.activity_main)
        if (intent != null && requestCode == 0) {
            onLoggedIn(intent.data)
        }
    }

    private fun onLoggedIn(uri: Uri) {
        val code = uri.getQueryParameter("code")

        requestAccessToken(code, clientId, clientSecret)
                .subscribe({ token ->

                    TokenStore.get(this).saveToken(token)
                    onToken(token)
                }, {
                    info(it)
                })
    }


    private fun onToken(token: String) {

        getService<UserService>()
        .getUser()
        .subscribe { user ->

            val account = Account(user.login, getString(R.string.accountType))
            val data = Bundle()

            with(data) {
                putString(USER_PICTURE, user.avatarUrl)
                putString(USER_EMAIL, user.email)
                putString(USER_LOGIN, user.login)
                putString(USER_NAME, user.name)
            }

            accountManager.addAccountExplicitly(account, null, data)
            accountManager.setAuthToken(account, getString(R.string.accountType), token)

            val result = Bundle()
            with(result) {
                putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
                putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                putString(AccountManager.KEY_AUTHTOKEN, token)
            }

            setAccountAuthenticatorResult(result)

            startActivity(intentFor<UserProfileActivity>())
            finish()
        }
    }

    fun onLoginClick(view: View) {
        openWebView()
    }
}
