package gitlin.kothub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import gitlin.kothub.github.OAuthValues
import gitlin.kothub.github.api.userSummary
import gitlin.kothub.github.requestAccessToken
import gitlin.kothub.utilities.editSharedPreferences
import gitlin.kothub.utilities.set
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

		val code = intent.data.getQueryParameter("code")

		requestAccessToken(code, { error, token ->
			if (error == null) {
				Log.d("OAuthToken", token)
				OAuthValues.isLoggedIn = true

				OAuthValues.GITHUB_TOKEN = token!!

				editSharedPreferences(getString(R.string.github_preferences_file)) {
					set(getString(R.string.oauth_github_token) to token)
				}

				userSummary { error, summary ->
					if (summary == null) {
						Log.d("USERSUMMARY", "ERROR")
						Log.d("USERSUMMARY", error?.response?.httpResponseMessage ?: "NO ERROR??")
					}
					else {
						Log.d("USERSUMMARY", summary.toString())
						followers.text = summary.followers.toString()
//						Log.d("USERSUMMARY", summary.location)
					}
				}

			} else {
				// Handle error
			}
		})
    }
}
