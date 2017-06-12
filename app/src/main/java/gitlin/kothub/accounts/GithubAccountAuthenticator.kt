package gitlin.kothub.accounts

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import org.jetbrains.anko.intentFor

class GithubAccountAuthenticator(val context: Context): AbstractAccountAuthenticator(context) {

    override fun getAuthTokenLabel(authTokenType: String?): String? {
        return null
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? {
        return null
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? {
        return null
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {

        val manager = AccountManager.get(context)
        val token = manager.peekAuthToken(account, authTokenType)

        if (!token.isEmpty()) {
            val result = Bundle()
            with(result) {
                putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
                putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
                putString(AccountManager.KEY_AUTHTOKEN, token)
            }

            return result
        }

        val intent = context.intentFor<LoginActivity>(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response,
                LoginActivity.ARG_ACCOUNT_TYPE to account?.type,
                LoginActivity.ARG_AUTH_TYPE to authTokenType
        )

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        val result = Bundle()
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? {
        return null
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        val intent = context.intentFor<LoginActivity>(
                LoginActivity.ARG_ACCOUNT_TYPE to accountType,
                LoginActivity.ARG_AUTH_TYPE to authTokenType,
                LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT to true,
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response
        )

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

}