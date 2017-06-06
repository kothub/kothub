package gitlin.kothub.ui.repo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gitlin.kothub.R
import kotlinx.android.synthetic.main.fragment_repository.*


class RepositoryFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        section_label.text = "${Math.random()}"
    }

    companion object {
        fun newInstance (): RepositoryFragment {
            return RepositoryFragment()
        }
    }
}