package gitlin.kothub.accounts

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthenticatorService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        val authenticator = GithubAccountAuthenticator(this)
        return authenticator.iBinder
    }
}
