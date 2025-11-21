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
     * Execute shell command via Shizuku
     * Returns Pair<exitCode, output>
     */
    fun executeCommand(command: String): Pair<Int, String> {
        if (!hasPermission()) {
            return Pair(-1, "ERROR: No Shizuku permission")
        }

        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            
            // Read output
            val output = StringBuilder()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            reader.close()

            // Read error stream
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            var errorLine: String?
            while (errorReader.readLine().also { errorLine = it } != null) {
                output.append("ERROR: ").append(errorLine).append("\n")
            }
            errorReader.close()

            // Wait for completion
            val exitCode = process.waitFor()
            process.destroy()

            Pair(exitCode, output.toString())

        } catch (e: Exception) {
            Pair(-1, "EXCEPTION: ${e.message}")
        }
    }

    /**
     * Execute command and return true if successful (exit code 0)
     */
    fun executeCommandSimple(command: String): Boolean {
        val (exitCode, _) = executeCommand(command)
        return exitCode == 0
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
