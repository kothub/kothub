package gitlin.kothub.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.iconics.IconicsDrawable
import gitlin.kothub.R
import gitlin.kothub.github.api.data.Notification
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.notification_list_view.view.*

class NotificationAdapter(private val context: Context, val notifications: List<Notification>): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.notification_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val notif = notifications[position]

        viewHolder.name.value = notif.repository.full_name
        viewHolder.description.value = notif.subject.title
        viewHolder.subjectIcon.icon = getSubjectIcon(notif.subject.type)
    }

    override fun getItemCount(): Int = notifications.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.repository
        val description = view.subject
        val subjectIcon = view.subjectIcon
    }

    fun getSubjectIcon(type: String): IconicsDrawable {
        val subjectType = SubjectType.valueOf(type.toUpperCase())

        return IconicsDrawable(context, subjectType.icon)
    }

    enum class SubjectType(val icon: String) {
        ISSUE("oct_issue_opened"),
        PULLREQUEST("oct_git_pull_request")
    }
}