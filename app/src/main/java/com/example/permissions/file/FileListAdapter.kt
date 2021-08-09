package com.example.permissions.file

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import com.example.permissions.R
import com.example.permissions.databinding.FileListItemBinding
import java.io.File

class FileListAdapter :
    RecyclerView.Adapter<FileListAdapter.ViewHolder>() {

    private var files: List<String> = emptyList()

    fun updateFiles(rootFile: File) {
        files = listFiles(rootFile)
        notifyDataSetChanged()
    }

    fun updateFiles(rootDocumentFile: DocumentFile) {
        files = rootDocumentFile.listFiles().mapNotNull { it.name }
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

    private fun listFiles(rootFile: File): List<String> =
        rootFile.listFiles()?.map { it.absolutePath } ?: emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FileListItemBinding.bind(itemView)

        fun bindView(filePath: String) {
            binding.pathView.text = filePath
        }
    }
}