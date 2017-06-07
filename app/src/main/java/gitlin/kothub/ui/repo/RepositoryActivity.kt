package gitlin.kothub.ui.repo


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.squareup.picasso.Picasso

import gitlin.kothub.R
import gitlin.kothub.accounts.getUserPicture
import gitlin.kothub.github.api.RepositoryService
import gitlin.kothub.github.api.data.RepositorySummary
import gitlin.kothub.github.api.getService
import gitlin.kothub.utilities.LifecycleAppCompatActivity
import gitlin.kothub.utilities.get
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.activity_repository.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class RepositoryViewModel: ViewModel() {

    val repo = MutableLiveData<RepositorySummary>()


    fun loadRepository(context: Context, owner: String, name: String) {

        if (repo.value != null) {
            return
        }

        context
            .getService<RepositoryService>()
            .repository(owner, name)
            .subscribe(
            {
                this.repo.value = it
            },
            {
                throw it
            }
        )
    }
}

class RepositoryActivity : LifecycleAppCompatActivity(), AnkoLogger {

    private var pagerAdapter: RepositoryPagerAdapter? = null

    lateinit var model: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)

        model = ViewModelProviders.of(this).get<RepositoryViewModel>()

        val owner = intent.getStringExtra("owner")
        val name = intent.getStringExtra("name")

        model.loadRepository(this, owner, name)

        model.repo.observe(this, Observer {
            if (it != null) updateView(it)
        })


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        collapsing_toolbar.title = " "
        collapsing_toolbar.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
        var scrollRange = -1
        var showTitle = false
        app_bar_layout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (scrollRange + verticalOffset == 0) {
                val sizeInPixel = this.resources.getDimensionPixelSize(R.dimen.toolbar_elevation);
                toolbar.elevation = sizeInPixel.toFloat()
            }
            else {
                toolbar.elevation = 0f
            }

            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset < 100) {
                collapsing_toolbar.title = "$owner/$name"
                showTitle = true
            } else if(showTitle) {
                collapsing_toolbar.title = " " // carefull there should a space between double quote otherwise it wont work
                toolbar.elevation = 0f
                showTitle = false
            }
        }


        info(owner)
        info(name)



    }


    private fun updateView (repo: RepositorySummary) {

        nameWithOwner.text = repo.nameWithOwner
        Picasso.with(ownerPicture.context).load(repo.ownerAvatarUrl).into(ownerPicture)

        if (repo.language != null) {
            languageName.text = repo.language.name
            languageColor.color = Color.parseColor(repo.language.color)
        }

        stargazers.value = repo.stargazers
        forks.value = repo.forks
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
