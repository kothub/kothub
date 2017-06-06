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

inline fun AppCompatActivity.removeFragment (tag: String) {

    val ft = supportFragmentManager.beginTransaction()
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    if (fragment != null) {
        Log.i("Fragment", "not null")
        ft.remove(fragment).commit()
    }
//    ft.commit()
}