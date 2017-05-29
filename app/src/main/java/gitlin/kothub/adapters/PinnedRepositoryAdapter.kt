package gitlin.kothub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import gitlin.kothub.R
import gitlin.kothub.github.api.data.PinnedRepository
import gitlin.kothub.utilities.value

class PinnedRepositoryAdapter(context: Context, list: MutableList<PinnedRepository>): ArrayAdapter<PinnedRepository>(context, 0, list) {


    private class ViewHolder(val name: TextView, val description: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val repo = getItem(position)
        val (view, viewHolder) =
            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.repository_list_view, parent, false)

                val name = view.findViewById(R.id.repository) as TextView
                val description = view.findViewById(R.id.description) as TextView
                val viewHolder = ViewHolder(name, description)

                view.tag = viewHolder

                Pair(view, viewHolder)
            }
            else {
                Pair(convertView, convertView.tag as ViewHolder)
            }


        viewHolder.name.value = repo.name
        viewHolder.description.value = repo.description
        return view
    }

}