package com.example.permissions.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.permissions.R
import com.example.permissions.contacts.ContactsActivity
import com.example.permissions.databinding.ActivityMainBinding
import com.example.permissions.file.FileListActivity
import com.example.permissions.group.PermissionGroupActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter = MainAdapter(
            listOf(
                MainItem(R.string.contacts) {
                startActivity(
                    ContactsActivity.newIntent(this)
                )
            },
                MainItem(R.string.permission_groups) {
                    startActivity(
                        PermissionGroupActivity.newIntent(this)
                    )
                },
                MainItem(R.string.internal_storage) {
                    startActivity(FileListActivity.newIntent(this, filesDir, false))
                },
                MainItem(R.string.cache) {
                    startActivity(FileListActivity.newIntent(this, cacheDir, false))
                }
            ) + getExternalFilesDirs(null).map {
                MainItem(R.string.external_storage) {
                    startActivity(FileListActivity.newIntent(this, it, true))
                }
            } + listOf(
                MainItem(R.string.documents) {
                    startActivity(
                        FileListActivity.newIntent(
                            this,
                            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!,
                            true
                        )
                    )
                }
            )
        )
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
    }
}