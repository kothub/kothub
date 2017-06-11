package gitlin.kothub.ui.repo

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import gitlin.kothub.R
import gitlin.kothub.github.api.data.RepositoryReadme
import gitlin.kothub.github.api.data.RepositorySummary
import gitlin.kothub.utilities.get
import gitlin.kothub.utilities.markdown.renderMarkdown
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.fragment_repository.*
import kotlinx.android.synthetic.main.repository_header.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread


class RepositoryFragment : LifecycleFragment(), AnkoLogger {

    lateinit var model: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity).get<RepositoryViewModel>()
        model.loadReadme(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.readme.observe(this, Observer {
            if (it != null) updateView(it)
        })

        readme.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun updateView (repo: RepositoryReadme) {

        doAsync {
            val html = renderMarkdown(context, repo.value, model.nameWithOwner, "master")

            uiThread {
               readme.text = html
            }
        }
    }

    companion object {

        fun newInstance (): RepositoryFragment {

            val fragment = RepositoryFragment()
            return fragment
        }
    }
}