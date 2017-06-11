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
import android.text.Spanned
import android.text.method.LinkMovementMethod
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.squareup.picasso.Picasso

import gitlin.kothub.R
import gitlin.kothub.github.api.RepositoryService
import gitlin.kothub.github.api.data.RepositoryReadme
import gitlin.kothub.github.api.data.RepositorySummary
import gitlin.kothub.github.api.getService
import gitlin.kothub.ui.LifecycleAppCompatActivity
import gitlin.kothub.utilities.get
import gitlin.kothub.utilities.markdown.renderMarkdown
import gitlin.kothub.utilities.noTitle
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.repository_header.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class RepositoryViewModel(): ViewModel() {

    val repo = MutableLiveData<RepositorySummary>()
    val readme = MutableLiveData<Spanned>()

    lateinit var owner: String
    lateinit var name: String

    val nameWithOwner: String get() = "$owner/$name"

    fun loadRepository(context: Context) {

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

    fun loadReadme(context: Context) {

        if (readme.value != null) {
            return
        }

        context
            .getService<RepositoryService>()
            .readme(owner, name)
            .subscribe(
                {
                    this.readme.value = it
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

        toolbar.noTitle()
        supportActionBar!!.title = " "

        pagerAdapter = RepositoryPagerAdapter(supportFragmentManager, this)
        viewpager.adapter = pagerAdapter

        repository_tabs.setupWithViewPager(viewpager)

        model = ViewModelProviders.of(this).get<RepositoryViewModel>()
        model.owner = intent.getStringExtra("owner")
        model.name = intent.getStringExtra("name")
        model.loadRepository(this)
        model.repo.observe(this, Observer {
            if (it != null) updateHeaderView(it)
        })

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }



//        fab.setImageDrawable(
//                IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_share).color(Color.WHITE)
//        )
    }

    private fun updateHeaderView (repo: RepositorySummary) {

        repoOwner.text = repo.ownerLogin
        repoName.text = repo.name
        Picasso.with(ownerPicture.context).load(repo.ownerAvatarUrl).into(ownerPicture)

        if (repo.language != null) {
//            languageName.text = repo.language.name
//            languageColor.color = Color.parseColor(repo.language.color)
        }

        watchers.value = repo.watchers
        stargazers.value = repo.stargazers
        forks.value = repo.forks
    }

    inner class RepositoryPagerAdapter(val fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {

        val PAGE_COUNT = 4

        override fun getCount(): Int = PAGE_COUNT

        override fun getItem(position: Int): Fragment = RepositoryFragment.newInstance()

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Home"
                1 -> "Files"
                2 -> "Issues"
                3 -> "Pull Requests"
                else -> null
            }
        }
    }



}
