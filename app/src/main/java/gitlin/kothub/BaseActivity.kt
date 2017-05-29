package gitlin.kothub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_base.*

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        val drawer = layoutInflater.inflate(R.layout.activity_base, null)
        val activityContainer = activity_content
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(drawer)
        setSupportActionBar(toolbar)
//        setTitle
    }
}
