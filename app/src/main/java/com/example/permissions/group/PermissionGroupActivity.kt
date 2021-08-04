package com.example.permissions.group

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.permissions.databinding.ActivityPermissionGroupBinding

class PermissionGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPermissionGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter =
            PermissionGroupAdapter(
                packageManager.getAllPermissionGroups(0).map {
                    PermissionGroupItem(
                        it.descriptionRes,
                        it.loadDescription(packageManager),
                        it.priority
                    )
                }.sortedBy { it.priority })
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, PermissionGroupActivity::class.java)
    }
}