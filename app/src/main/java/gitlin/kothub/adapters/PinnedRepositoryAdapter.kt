package gitlin.kothub.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import gitlin.kothub.R
import gitlin.kothub.github.api.data.PinnedRepository
import gitlin.kothub.ui.repo.RepositoryActivity
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.repository_list_view.view.*
import org.jetbrains.anko.intentFor

class PinnedRepositoryAdapter(
        private val context: Context,
        val repositories: List<PinnedRepository>
): RecyclerView.Adapter<PinnedRepositoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.repository_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = repositories.size


    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val repository: PinnedRepository = repositories[position]

        vh.name.value = repository.name
        vh.description.value = repository.description

        if (repository.language != null) {
            vh.languageName.value = repository.language.name

            val color = Color.parseColor(repository.language.color)
            val gradient = GradientDrawable()
            with(gradient) {
                setColor(color)
                shape = GradientDrawable.OVAL
                cornerRadius = 0f
            }

            vh.languageColor.background = gradient
        }
        
        vh.forks.value = repository.forks
        vh.stargazers.value = repository.stargazers
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val name: TextView = view.repository
        val description: TextView = view.description
        val languageName: TextView = view.languageName
        val languageColor: LinearLayout = view.languageColorShape
        val stargazers: TextView = view.stargazers
        val forks: TextView = view.forks


        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            Log.i("ViewHolder", "CLICKED")

            val position = adapterPosition
            val repo = this@PinnedRepositoryAdapter.repositories[position]
            val context = this@PinnedRepositoryAdapter.context
            val intent = context.intentFor<RepositoryActivity>(
                "name" to repo.name,
                "owner" to repo.owner
            )

            context.startActivity(intent)
        }
    }
}