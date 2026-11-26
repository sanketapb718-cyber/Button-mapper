package com.tradewithsanket.powermasterpro

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var status: TextView
    private val dpm by lazy { getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager }
    private val admin by lazy { ComponentName(this, LockAdminReceiver::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 48, 36, 36)
        }
        root.addView(TextView(this).apply {
            text = "POWER MASTER PRO V4"
            textSize = 26f
        })
        root.addView(TextView(this).apply {
            text = "Volume Up = Screen Lock"
            textSize = 17f
            setPadding(0, 12, 0, 24)
        })
        status = TextView(this).apply { textSize = 14f; setPadding(0, 8, 0, 18) }
        root.addView(status)
        root.addView(makeButton("1. Enable Screen Lock Permission") { enableAdmin() })
        root.addView(makeButton("2. Open Accessibility Settings") {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        })
        root.addView(TextView(this).apply {
            text = "Turn on Power Master Pro in Accessibility. Then press Volume Up once to lock the screen. Android may not allow the app to wake the screen with Volume Up while the display is fully off. Some banking apps may restrict apps that use Accessibility; if PhonePe or Paytm has trouble, turn this service off."
            textSize = 13f
            setPadding(0, 24, 0, 0)
        })
        setContentView(root)
        refresh()
    }

    private fun makeButton(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        isAllCaps = false
        setOnClickListener { action() }
    }

    private fun enableAdmin() {
        if (dpm.isAdminActive(admin)) {
            Toast.makeText(this, "Screen lock permission is already enabled", Toast.LENGTH_SHORT).show()
            refresh()
            return
        }
        startActivity(
            Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                .putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
                .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Allows Volume Up to lock the screen.")
        )
    }

    private fun refresh() {
        status.text = "Screen-lock permission: ${if (dpm.isAdminActive(admin)) "ON" else "OFF"}\n\nEnable the Accessibility service below to detect Volume Up."
    }

    override fun onResume() {
        super.onResume()
        if (::status.isInitialized) refresh()
    }
}
