package gitlin.kothub.utilities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log

inline fun AppCompatActivity.createFragment (savedInstance: Bundle?, placeholder: Int, tag: String? = null, body: FragmentTransaction.() -> Fragment) {
    if (savedInstance == null) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(placeholder, ft.body(), tag)
        ft.commit()
    }
}

