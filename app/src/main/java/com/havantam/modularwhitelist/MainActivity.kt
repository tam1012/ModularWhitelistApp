package com.havantam.modularwhitelist

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * MODULAR WHITELIST - Main Activity
 * Author: Ha Van Tam (babyinmyl0v3) - ae vOz
 * Telegram: @ThongThaiTuaThanTien
 */
class MainActivity : AppCompatActivity() {

    private lateinit var applyButton: Button
    private lateinit var logTextView: TextView
    private lateinit var logScrollView: ScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var statusTextView: TextView

    private lateinit var shizukuHelper: ShizukuHelper
    private lateinit var whitelistManager: WhitelistManager

    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        applyButton = findViewById(R.id.applyButton)
        logTextView = findViewById(R.id.logTextView)
        logScrollView = findViewById(R.id.logScrollView)
        progressBar = findViewById(R.id.progressBar)
        statusTextView = findViewById(R.id.statusTextView)

        // Initialize helpers
        shizukuHelper = ShizukuHelper(this)
        whitelistManager = WhitelistManager(shizukuHelper)

        // Setup button click
        applyButton.setOnClickListener {
            if (!isRunning) {
                checkShizukuAndApply()
            }
        }

        // Check Shizuku on start
        checkShizukuStatus()
    }

    private fun checkShizukuStatus() {
        when {
            !shizukuHelper.isShizukuInstalled() -> {
                updateStatus("❌ Shizuku chưa được cài đặt")
                appendLog("[ERROR] Shizuku app chưa được cài đặt!")
                appendLog("[INFO] Vui lòng cài Shizuku từ Play Store hoặc GitHub")
                appendLog("[INFO] Link: https://github.com/RikkaApps/Shizuku/releases")
                applyButton.isEnabled = false
            }
            !shizukuHelper.isShizukuRunning() -> {
                updateStatus("⚠️ Shizuku chưa chạy")
                appendLog("[WARNING] Shizuku đã cài nhưng chưa được kích hoạt")
                appendLog("[INFO] Vui lòng mở Shizuku app và làm theo hướng dẫn")
                applyButton.isEnabled = false
            }
            !shizukuHelper.hasPermission() -> {
                updateStatus("🔐 Cần cấp quyền Shizuku")
                appendLog("[INFO] Đang yêu cầu quyền Shizuku...")
                shizukuHelper.requestPermission { granted ->
                    if (granted) {
                        updateStatus("✅ Sẵn sàng")
                        appendLog("[OK] Đã cấp quyền Shizuku thành công!")
                        applyButton.isEnabled = true
                    } else {
                        updateStatus("❌ Bị từ chối quyền")
                        appendLog("[ERROR] Quyền Shizuku bị từ chối")
                        applyButton.isEnabled = false
                    }
                }
            }
            else -> {
                updateStatus("✅ Sẵn sàng")
                appendLog("[OK] Shizuku đã sẵn sàng!")
                applyButton.isEnabled = true
            }
        }
    }

    private fun checkShizukuAndApply() {
        if (!shizukuHelper.hasPermission()) {
            appendLog("[ERROR] Không có quyền Shizuku!")
            updateStatus("❌ Lỗi quyền")
            return
        }

        applyWhitelist()
    }

    private fun applyWhitelist() {
        isRunning = true
        applyButton.isEnabled = false
        progressBar.visibility = View.VISIBLE
        updateStatus("🔄 Đang whitelist...")

        lifecycleScope.launch {
            try {
                appendLog("\n" + "=".repeat(50))
                appendLog("[START] Bắt đầu whitelist...")
                appendLog("=".repeat(50) + "\n")

                val result = whitelistManager.applyWhitelist { message ->
                    // Update progress on UI thread
                    lifecycleScope.launch(Dispatchers.Main) {
                        appendLog(message)
                    }
                }

                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        appendLog("\n" + "=".repeat(50))
                        appendLog("[SUCCESS] Hoàn tất whitelist!")
                        appendLog("=".repeat(50))
                        updateStatus("✅ Hoàn tất")
                    } else {
                        val error = result.exceptionOrNull()?.message ?: "Unknown error"
                        appendLog("\n[ERROR] Lỗi: $error")
                        updateStatus("❌ Lỗi")
                    }

                    isRunning = false
                    applyButton.isEnabled = true
                    progressBar.visibility = View.GONE
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    appendLog("\n[FATAL ERROR] ${e.message}")
                    updateStatus("❌ Lỗi nghiêm trọng")
                    isRunning = false
                    applyButton.isEnabled = true
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun appendLog(message: String) {
        logTextView.append("$message\n")
        // Auto-scroll to bottom
        logScrollView.post {
            logScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun updateStatus(status: String) {
        statusTextView.text = status
    }

    override fun onDestroy() {
        super.onDestroy()
        shizukuHelper.cleanup()
    }
}
