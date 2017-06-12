package gitlin.kothub.ui.settings


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import gitlin.kothub.R
import gitlin.kothub.utilities.createFragment
import kotlinx.android.synthetic.main.toolbar.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        createFragment(savedInstanceState, R.id.preferences_placeholder) {
            SettingsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
    }
}

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstance: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}
