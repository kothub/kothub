package gitlin.kothub.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import gitlin.kothub.R
import gitlin.kothub.github.api.data.Organization
import gitlin.kothub.utilities.value
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.organization_view_list.view.*

class OrganizationSummaryAdapter(
        private val context: Context,
        val organizations: List<Organization>
): RecyclerView.Adapter<OrganizationSummaryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.organization_view_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = organizations.size


    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val org = organizations[position]
        vh.name.value = org.name
        Picasso.with(vh.avatarUrl.context).load(org.avatarUrl).into(vh.avatarUrl)
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.organization
        val avatarUrl: ImageView = view.organizationImage
    }
}