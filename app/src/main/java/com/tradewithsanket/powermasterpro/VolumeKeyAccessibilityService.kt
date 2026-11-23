package com.tradewithsanket.powermasterpro

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.view.accessibility.AccessibilityEvent

class VolumeKeyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP &&
            event.action == KeyEvent.ACTION_UP &&
            event.repeatCount == 0
        ) {
            val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val admin = ComponentName(this, LockAdminReceiver::class.java)
            if (dpm.isAdminActive(admin)) {
                dpm.lockNow()
                return true
            }
        }
        return false
    }
}
