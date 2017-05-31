package gitlin.kothub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import gitlin.kothub.R
import gitlin.kothub.github.api.data.Notification
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.notification_list_view.view.*

class NotificationAdapter(context: Context, list: MutableList<Notification>): ArrayAdapter<Notification>(context, 0, list) {


    private class ViewHolder(val name: TextView, val subject: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val notification = getItem(position)
        val (view, viewHolder) =
                if (convertView == null) {
                    val inflater = LayoutInflater.from(context)
                    val view = inflater.inflate(R.layout.notification_list_view, parent, false)

                    val name = view.repository
                    val description = view.subject
                    val viewHolder = ViewHolder(name, description)

                    view.tag = viewHolder

                    Pair(view, viewHolder)
                }
                else {
                    Pair(convertView, convertView.tag as ViewHolder)
                }


        viewHolder.name.value = notification.repository.name
        viewHolder.subject.value = notification.subject.title
        return view
    }

}