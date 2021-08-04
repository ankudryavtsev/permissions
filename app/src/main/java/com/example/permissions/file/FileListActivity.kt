package com.example.permissions.file

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.permissions.databinding.ActivityFileListBinding
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class FileListActivity : AppCompatActivity() {
    private lateinit var pathFile: File
    private lateinit var adapter: FileListAdapter
    private var external: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pathFile = intent.getSerializableExtra(PATH_EXTRA) as? File
            ?: throw IllegalArgumentException("Path must not be empty")
        external = intent.getBooleanExtra(EXTERNAL_EXTRA, false)
        val binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pathView.text = pathFile.absolutePath
        if (external) {
            binding.statusView.text = "State = ${Environment.getExternalStorageState(pathFile)}"
        } else {
            binding.statusView.visibility = View.GONE
        }

        val layoutManager = LinearLayoutManager(this@FileListActivity)
        adapter = FileListAdapter(pathFile)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
        binding.addButton.setOnClickListener { addFile() }
    }

    private fun addFile() {
        val fileName = "${SimpleDateFormat.getTimeInstance().format(Date())}.txt"
        try {
            File(pathFile, fileName).createNewFile()
        } catch (exception: IOException) {
            Log.e(TAG, "addFile: createNewFile Error", exception)
            Toast.makeText(this, "addFile: createNewFile Error", Toast.LENGTH_LONG).show()
        }

        adapter.updateFiles()
    }

    companion object {
        private const val TAG = "FileListActivity"
        private const val PATH_EXTRA = "path"
        private const val EXTERNAL_EXTRA = "external"

        fun newIntent(context: Context, pathFile: File, external: Boolean) =
            Intent(context, FileListActivity::class.java).apply {
                putExtra(PATH_EXTRA, pathFile)
                putExtra(EXTERNAL_EXTRA, external)
            }
    }
}