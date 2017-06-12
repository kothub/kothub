package gitlin.kothub.ui.repo

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gitlin.kothub.R
import gitlin.kothub.utilities.get
import kotlinx.android.synthetic.main.fragment_repository.*
import org.jetbrains.anko.AnkoLogger


class RepositoryFragment : LifecycleFragment(), AnkoLogger {

    lateinit var model: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity).get<RepositoryViewModel>()
        model.loadReadme(context)
    }

    override fun onResume() {
        super.onResume()
        model = ViewModelProviders.of(activity).get<RepositoryViewModel>()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.readme.observe(this, Observer {
            if (it != null) updateView(it)
        })

        readme.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun updateView (repo: Spanned) {
        readme.text = repo
    }

    companion object {

        fun newInstance (): RepositoryFragment {

            val fragment = RepositoryFragment()
            return fragment
        }
    }
}