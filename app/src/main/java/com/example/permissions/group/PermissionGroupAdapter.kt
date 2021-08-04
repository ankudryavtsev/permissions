package com.example.permissions.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.permissions.R
import com.example.permissions.databinding.PermissionGroupItemBinding

class PermissionGroupAdapter(private val permissionGroupInfos: List<PermissionGroupItem>) :
    RecyclerView.Adapter<PermissionGroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.permission_group_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(permissionGroupInfos[position])
    }

    override fun getItemCount() = permissionGroupInfos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PermissionGroupItemBinding.bind(itemView)

        fun bindView(permissionGroupItem: PermissionGroupItem) {
            binding.descriptionView.text = permissionGroupItem.description
            binding.priorityView.text = "${permissionGroupItem.priority}"
        }
    }
}

data class PermissionGroupItem(
    @StringRes val descriptionRes: Int,
    val description: CharSequence?,
    val priority: Int
)