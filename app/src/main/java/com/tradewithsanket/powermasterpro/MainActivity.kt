package com.tradewithsanket.powermasterpro

import android.Manifest
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
        root.addView(TextView(this).apply { text = "POWER MASTER PRO  V3"; textSize = 26f })
        root.addView(TextView(this).apply {
            text = "Shake to Wake  •  Volume Up to Lock"
            textSize = 16f
            setPadding(0, 12, 0, 24)
        })
        status = TextView(this).apply { textSize = 14f; setPadding(0, 8, 0, 18) }
        root.addView(status)
        root.addView(makeButton("1. Enable Screen Lock Permission (Device Admin)") { enableAdmin() })
        root.addView(makeButton("2. Enable Volume Up Accessibility") {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        })
        root.addView(makeButton("3. Start Shake-to-Wake Service") { startShakeService() })
        root.addView(makeButton("4. Stop Service") {
            stopService(Intent(this, ShakeService::class.java))
            refresh()
        })
        root.addView(makeButton("OPPO Battery Settings") { openBatterySettings() })
        root.addView(TextView(this).apply {
            text = "Important: Android/OPPO may suspend motion sensors while the display is off. Shake-to-Wake depends on device firmware and battery settings; it cannot be guaranteed on every build. The service uses extra battery."
            textSize = 13f
            setPadding(0, 24, 0, 0)
        })
        setContentView(root)
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 44)
        }
        refresh()
    }

    private fun makeButton(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        isAllCaps = false
        setOnClickListener { action() }
    }

    private fun enableAdmin() {
        if (dpm.isAdminActive(admin)) {
            Toast.makeText(this, "Screen lock permission already enabled", Toast.LENGTH_SHORT).show()
            refresh()
            return
        }
        startActivity(
            Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                .putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
                .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Allows Volume Up to lock the screen.")
        )
    }

    private fun startShakeService() {
        val intent = Intent(this, ShakeService::class.java)
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent) else startService(intent)
        Toast.makeText(this, "Shake service start requested", Toast.LENGTH_SHORT).show()
        refresh()
    }

    private fun openBatterySettings() {
        try {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")))
        } catch (_: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun refresh() {
        val adminOn = dpm.isAdminActive(admin)
        status.text = "Screen-lock permission: ${if (adminOn) "ON" else "OFF"}\n\nEnable Power Master Pro in Android Accessibility settings for Volume Up locking. Start the motion service and set app battery use to Unrestricted if OPPO offers that option."
    }

    override fun onResume() {
        super.onResume()
        if (::status.isInitialized) refresh()
    }
}
