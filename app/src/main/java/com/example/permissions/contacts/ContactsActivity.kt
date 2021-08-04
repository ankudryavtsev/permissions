package com.example.permissions.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissions.R
import com.example.permissions.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {
    private lateinit var statusView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                Toast.makeText(this, "YOU SHOULD TRUST ME", Toast.LENGTH_LONG).show()
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(PERMISSION),
                REQUEST_CODE
            )
        }
        statusView = binding.textView
        updateStatusView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            updateStatusView()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun updateStatusView() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
        statusView.setText(if (permissionGranted) R.string.permission_granted else R.string.permission_denied)
    }

    companion object {
        private const val PERMISSION = Manifest.permission.READ_CONTACTS
        private const val REQUEST_CODE = 44

        fun newIntent(context: Context) = Intent(context, ContactsActivity::class.java)
    }
}