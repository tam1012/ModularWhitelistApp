package com.havantam.modularwhitelist

import android.content.Context
import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Shizuku Helper
 * Wrapper for Shizuku API to execute shell commands with ADB permissions
 * Author: Ha Van Tam (babyinmyl0v3) - ae vOz
 */
class ShizukuHelper(private val context: Context) {

    companion object {
        private const val SHIZUKU_PACKAGE = "moe.shizuku.privileged.api"
        private const val REQUEST_CODE_PERMISSION = 1001
    }

    private var permissionCallback: ((Boolean) -> Unit)? = null

    private val permissionListener = Shizuku.OnRequestPermissionResultListener { _, grantResult ->
        permissionCallback?.invoke(grantResult == PackageManager.PERMISSION_GRANTED)
        permissionCallback = null
    }

    init {
        // Register permission listener if Shizuku is available
        if (isShizukuRunning()) {
            try {
                Shizuku.addRequestPermissionResultListener(permissionListener)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Check if Shizuku app is installed
     */
    fun isShizukuInstalled(): Boolean {
        return try {
            context.packageManager.getPackageInfo(SHIZUKU_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Check if Shizuku service is running
     */
    fun isShizukuRunning(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Check if we have Shizuku permission
     */
    fun hasPermission(): Boolean {
        return if (isShizukuRunning()) {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }

    /**
     * Request Shizuku permission
     */
    fun requestPermission(callback: (Boolean) -> Unit) {
        if (!isShizukuRunning()) {
            callback(false)
            return
        }

        if (hasPermission()) {
            callback(true)
            return
        }

        permissionCallback = callback
        Shizuku.requestPermission(REQUEST_CODE_PERMISSION)
    }

    /**
     * Put a setting value using Shizuku permissions
     * Returns true if successful
     */
    fun putSetting(namespace: String, key: String, value: String): Boolean {
        if (!hasPermission()) {
            return false
        }

        return try {
            when (namespace.lowercase()) {
                "system" -> android.provider.Settings.System.putString(
                    context.contentResolver, key, value
                )
                "secure" -> android.provider.Settings.Secure.putString(
                    context.contentResolver, key, value
                )
                "global" -> android.provider.Settings.Global.putString(
                    context.contentResolver, key, value
                )
                else -> false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get a setting value
     */
    fun getSetting(namespace: String, key: String): String? {
        return try {
            when (namespace.lowercase()) {
                "system" -> android.provider.Settings.System.getString(
                    context.contentResolver, key
                )
                "secure" -> android.provider.Settings.Secure.getString(
                    context.contentResolver, key
                )
                "global" -> android.provider.Settings.Global.getString(
                    context.contentResolver, key
                )
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Cleanup resources
     */
    fun cleanup() {
        try {
            if (isShizukuRunning()) {
                Shizuku.removeRequestPermissionResultListener(permissionListener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
