package gitlin.kothub.ui.repo


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity

import gitlin.kothub.R
import gitlin.kothub.ui.drawer.AppDrawer
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class RepositoryActivity : LifecycleAppCompatActivity(), AnkoLogger {

    private var pagerAdapter: RepositoryPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
     //   setSupportActionBar(toolbar)

        val owner = intent.getStringExtra("owner")
        val name = intent.getStringExtra("name")

        toolbar.title = "$owner/$name"

        val drawer = AppDrawer(this, toolbar)

        pagerAdapter = RepositoryPagerAdapter(supportFragmentManager, this)
        viewpager.adapter = pagerAdapter

        repository_tabs.setupWithViewPager(viewpager)

        info(owner)
        info(name)


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
//        viewpager.setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
//            override fun onPageSelected(position: Int) {
//                actionBar.setSelectedNavigationItem(position)
//            }
//        })

        // For each of the sections in the app, add a tab to the action bar.
//        for (i in 0..pagerAdapter!!.count - 1) {
//            // Create a tab with text corresponding to the page title defined by
//            // the adapter. Also specify this Activity object, which implements
//            // the TabListener interface, as the callback (listener) for when
//            // this tab is selected.
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText(pagerAdapter!!.getPageTitle(i))
//                            .setTabListener(this))
//        }

    }

    inner class RepositoryPagerAdapter(val fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {

        val PAGE_COUNT = 3

        override fun getCount(): Int = PAGE_COUNT

        override fun getItem(position: Int): Fragment = RepositoryFragment.newInstance()

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Tree"
                1 -> return "Issues"
                2 -> return "Pull Requests"
            }
            return null
        }
    }



}
