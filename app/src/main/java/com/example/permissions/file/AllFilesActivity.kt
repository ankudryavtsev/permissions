package com.example.permissions.file

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.permissions.BuildConfig
import com.example.permissions.databinding.ActivityAllFilesBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AllFilesActivity : AppCompatActivity() {
    private lateinit var adapter: FileListAdapter
    private lateinit var binding: ActivityAllFilesBinding
    private val pathFile = Environment.getExternalStorageDirectory()
    private var rootDocumentFile: DocumentFile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllFilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pathView.text = pathFile.absolutePath
        binding.statusView.text = "State = ${Environment.getExternalStorageState()}"
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
        adapter = FileListAdapter()
        binding.recyclerView.adapter = adapter
        binding.addButton.setOnClickListener { addFile() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VOLUME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data?.data?.let {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    rootDocumentFile = DocumentFile.fromTreeUri(this, it)
                }
                listFiles()
            } else {
                requestPermission()
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listFiles()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    Toast.makeText(this, "PLZ GIVE PERMISSION", Toast.LENGTH_LONG).show()
                }
                requestPermission()
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            listFiles()
        } else {
            requestPermission()
        }
    }

    private fun listFiles() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            rootDocumentFile?.let {
                adapter.updateFiles(it)
            }
        } else {
            adapter.updateFiles(pathFile)
        }
    }

    private fun addFile() {
        val fileName = "${SimpleDateFormat.getTimeInstance().format(Date())}.txt"
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            rootDocumentFile?.let {
                it.createFile("text/plain", fileName)
                adapter.updateFiles(it)
            }
        } else {
            try {
                File(pathFile, fileName).createNewFile()
            } catch (exception: IOException) {
                Log.e(TAG, "addFile: createNewFile Error", exception)
                Toast.makeText(this, "addFile: createNewFile Error", Toast.LENGTH_LONG).show()
            }

            adapter.updateFiles(pathFile)
        }
    }

    private fun requestPermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                )
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                val storageManager = getSystemService(StorageManager::class.java)
                val intent =
                    storageManager.getStorageVolume(pathFile)?.createOpenDocumentTreeIntent()
                intent?.let { startActivityForResult(it, VOLUME_REQUEST_CODE) }
            }
            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

    private fun checkPermission() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> rootDocumentFile != null && hasPermissionToAccessSdCard(
            pathFile
        )
        else -> ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "AllFilesActivity"
        private const val REQUEST_CODE = 333
        private const val VOLUME_REQUEST_CODE = 334

        fun newIntent(context: Context) =
            Intent(context, AllFilesActivity::class.java)
    }
}