package gitlin.kothub.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gitlin.kothub.R
import gitlin.kothub.github.api.data.PinnedRepository
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.repository_list_view.view.*

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
        val repository = repositories[position]

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


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.repository
        val description = view.description
        val languageName = view.languageName
        val languageColor = view.languageColorShape
        val stargazers = view.stargazers
        val forks = view.forks
    }
}