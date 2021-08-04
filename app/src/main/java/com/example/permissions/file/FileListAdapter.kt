package com.example.permissions.file

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.permissions.R
import com.example.permissions.databinding.FileListItemBinding
import java.io.File

class FileListAdapter(private val rootFile: File) :
    RecyclerView.Adapter<FileListAdapter.ViewHolder>() {

    private var files: Array<File> = rootFile.listFiles() ?: emptyArray()

    fun updateFiles() {
        files = rootFile.listFiles() ?: emptyArray()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.file_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(files[position])
    }

    override fun getItemCount() = files.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FileListItemBinding.bind(itemView)

        fun bindView(file: File) {
            binding.pathView.text = file.absolutePath
        }
    }
}