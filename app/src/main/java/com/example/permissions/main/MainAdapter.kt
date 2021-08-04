package com.example.permissions.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.permissions.R
import com.example.permissions.databinding.MainListItemBinding

class MainAdapter(private val itemList: List<MainItem>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(itemList[position])
    }

    override fun getItemCount() = itemList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mainListItemBinding = MainListItemBinding.bind(itemView)

        fun bindView(item: MainItem) {
            mainListItemBinding.labelView.setText(item.labelRes)
            itemView.setOnClickListener { item.callback.invoke() }
        }
    }
}

data class MainItem(@StringRes val labelRes: Int, val callback: () -> Unit)