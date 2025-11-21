package com.havantam.modularwhitelist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Whitelist Manager
 * Manages PowerKeeper whitelist logic for Xiaomi/Redmi devices
 * Ported from MODULAR_WHITELIST.bat by Ha Van Tam (babyinmyl0v3) - ae vOz
 */
class WhitelistManager(private val shizukuHelper: ShizukuHelper) {

    companion object {
        private const val TAG = "WhitelistManager"
    }

    /**
     * App data class
     */
    private data class AppInfo(
        val packageName: String,
        val services: String = "",
        val processes: String = ""
    )

    /**
     * Complete app database from batch script
     */
    private val apps = listOf(
        // Google Mobile Services (Critical)
        AppInfo(
            "com.google.android.gms",
            "com.google.android.gms.gcm.GcmService,com.google.android.gms.gcm.nts.SchedulerService,com.google.android.gms.chimera.PersistentApiService,com.google.android.gms.measurement.AppMeasurementService,com.google.android.gms.measurement.AppMeasurementJobService,com.google.android.gms.fcm.FcmSdkGmsTaskService,com.google.android.gms.mdm.receivers.MdmDeviceAdminReceiver,com.google.android.gms.auth.account.authenticator.GoogleAccountAuthenticatorService,com.google.android.gms.chimera.PersistentIntentOperationService,com.google.android.gms.chimera.PersistentDirectBootAwareApiService,com.google.android.gms.chimera.PersistentBoundBrokerService,com.google.android.gms.chimera.PersistentInternalBoundBrokerService",
            "com.google.android.gms,com.google.android.gms.persistent,com.google.android.gms:gcm,com.google.android.gms:chimera,com.google.android.gms:unstable"
        ),
        AppInfo("com.google.android.gsf", "com.google.android.gsf.gservices.GservicesValueService", "com.google.android.gsf"),
        AppInfo("com.google.android.gsf.login", "com.google.android.gsf.login.LoginService", "com.google.android.gsf.login"),
        AppInfo("com.google.android.syncadapters.contacts", "com.google.android.syncadapters.contacts.ContactsSyncAdapterService", "com.google.android.syncadapters.contacts"),
        AppInfo("com.google.android.syncadapters.calendar", "com.google.android.syncadapters.calendar.CalendarSyncAdapterService", "com.google.android.syncadapters.calendar"),
        
        // Xiaomi Services
        AppInfo("com.xiaomi.xmsf", "com.xiaomi.xmsf.push.service.XMPushService,com.xiaomi.xmsf.push.service.MIPushNotificationService,com.xiaomi.xmsf.push.service.HttpService", "com.xiaomi.xmsf,com.xiaomi.xmsf:push"),
        AppInfo("com.xiaomi.xmsfkeeper", "com.xiaomi.xmsfkeeper.XmsfKeeperService", "com.xiaomi.xmsfkeeper"),
        
        // Banking Apps (Vietnam)
        AppInfo("com.agribank.emobile", "com.agribank.emobile.push.FCMService,com.agribank.emobile.job.JobService,com.agribank.emobile.services.SyncService", "com.agribank.emobile,com.agribank.emobile:push"),
        AppInfo("com.vnpay.bidv", "com.vnpay.bidv.push.FCMService,com.vnpay.bidv.service.SyncService", "com.vnpay.bidv,com.vnpay.bidv:push"),
        AppInfo("com.mbbank.mbbank", "com.mbbank.mbbank.push.FCMService,com.mbbank.mbbank.service.SyncService", "com.mbbank.mbbank,com.mbbank.mbbank:push"),
        AppInfo("com.shb.SHBMBanking", "com.shb.SHBMBanking.push.FCMService,com.shb.SHBMBanking.service.SyncService", "com.shb.SHBMBanking,com.shb.SHBMBanking:push"),
        AppInfo("vn.com.techcombank.bb.app", "vn.com.techcombank.bb.app.push.FCMMessagingService,vn.com.techcombank.bb.app.service.SyncService", "vn.com.techcombank.bb.app,vn.com.techcombank.bb.app:push"),
        AppInfo("com.tpb.mb.gprsandroid", "com.tpb.mb.gprsandroid.push.TPBankFCMService,com.tpb.mb.gprsandroid.service.SyncService", "com.tpb.mb.gprsandroid,com.tpb.mb.gprsandroid:push"),
        AppInfo("com.vcb", "com.vcb.mobilebanking.push.FCMService,com.vcb.mobilebanking.service.SyncService", "com.vcb,com.vcb:push"),
        AppInfo("com.vietinbank.ipay", "com.vietinbank.ipay.push.FCMService,com.vietinbank.ipay.service.SyncService", "com.vietinbank.ipay,com.vietinbank.ipay:push"),
        AppInfo("com.bplus.vtpay", "com.bplus.vtpay.push.VTPayFCMService,com.bplus.vtpay.service.SyncService", "com.bplus.vtpay,com.bplus.vtpay:push"),
        AppInfo("com.vib.myvib", "com.vib.myvib.push.MyVibFCMService,com.vib.myvib.service.SyncService", "com.vib.myvib,com.vib.myvib:push"),
        AppInfo("com.vnpay.app", "com.vnpay.app.push.FCMService,com.vnpay.app.service.SyncService", "com.vnpay.app,com.vnpay.app:push"),
        AppInfo("vn.com.vpbank.mobile", "vn.com.vpbank.mobile.push.FCMService,vn.com.vpbank.mobile.service.SyncService", "vn.com.vpbank.mobile,vn.com.vpbank.mobile:push"),
        
        // Browsers
        AppInfo("com.android.chrome", "org.chromium.chrome.browser.services.gcm.ChromeGcmListenerService,org.chromium.chrome.browser.notifications.NotificationService,org.chromium.chrome.browser.customtabs.CustomTabsConnectionService,org.chromium.content.app.SandboxedProcessService0,org.chromium.content.app.PrivilegedProcessService4", "com.android.chrome,com.android.chrome:background,com.android.chrome:sandboxed_process0,com.android.chrome:privileged_process4"),
        AppInfo("com.chrome.beta", "org.chromium.chrome.browser.services.gcm.ChromeGcmListenerService", "com.chrome.beta"),
        
        // Social & Messaging
        AppInfo("com.discord", "com.discord.firebase.messaging.DiscordFirebaseMessagingService,com.discord.service.SyncService", "com.discord,com.discord:push"),
        AppInfo("com.facebook.katana", "com.facebook.push.fcm.FcmListenerService,com.facebook.mqttlite.MqttService,com.facebook.rti.mqtt.common.service.MqttService,com.facebook.rti.mqtt.common.service.MqttServiceV2,com.facebook.common.executors.WakingExecutorService", "com.facebook.katana,com.facebook.katana:push"),
        AppInfo("com.facebook.orca", "com.facebook.push.fcm.FcmListenerService,com.facebook.mqttlite.MqttService,com.facebook.rti.mqtt.common.service.MqttService,com.facebook.rti.mqtt.common.service.MqttServiceV2,com.facebook.mqtt.service.MqttServiceV2,com.facebook.push.mqtt.service.MqttPushHelperService,com.facebook.push.fcm.GetFcmTokenRegistrarGCMService,com.facebook.rti.push.service.FbnsService,com.facebook.pushlite.PushLiteLollipopJobService,com.facebook.video.heroplayer.service.HeroKeepAliveService,com.facebook.common.executors.WakingExecutorService,com.facebook.rtc.notification.RtcNotificationForegroundService", "com.facebook.orca,com.facebook.orca:push,com.facebook.orca:service,com.facebook.orca:background"),
        AppInfo("com.instagram.android", "com.instagram.push.fbns.InstagramPushService,com.instagram.service.SyncService", "com.instagram.android,com.instagram.android:push"),
        AppInfo("com.twitter.android", "com.twitter.android.push.TwitterFirebaseMessagingService,com.twitter.android.service.SyncService", "com.twitter.android,com.twitter.android:push"),
        AppInfo("com.linkedin.android", "com.linkedin.android.push.LinkedInFirebaseMessagingService,com.linkedin.android.service.SyncService", "com.linkedin.android,com.linkedin.android:push"),
        AppInfo("jp.naver.line.android", "jp.naver.line.android.service.PushService,jp.naver.line.android.service.SyncService", "jp.naver.line.android,jp.naver.line.android:push"),
        AppInfo("org.telegram.messenger", "org.telegram.messenger.FirebaseMessagingService,org.telegram.messenger.NotificationsService", "org.telegram.messenger,org.telegram.messenger:push"),
        AppInfo("org.thunderdog.challegram", "org.thunderdog.challegram.service.PushService,org.thunderdog.challegram.service.SyncService", "org.thunderdog.challegram"),
        AppInfo("com.viber.voip", "com.viber.voip.messages.core.push.ViberPushService,com.viber.voip.service.SyncService", "com.viber.voip,com.viber.voip:push"),
        AppInfo("com.whatsapp", "com.whatsapp.push.FcmListenerService,com.whatsapp.service.SyncService", "com.whatsapp,com.whatsapp:push"),
        AppInfo("com.whatsapp.w4b", "com.whatsapp.w4b.push.FcmListenerService", "com.whatsapp.w4b,com.whatsapp.w4b:push"),
        AppInfo("com.tencent.mm", "com.tencent.mm.plugin.push.service.PushService,com.tencent.mm.service.SyncService", "com.tencent.mm,com.tencent.mm:push"),
        AppInfo("com.zing.zalo", "com.zing.zalo.service.ZaloFirebaseMessagingService,com.zing.zalo.service.SyncService,com.zing.zalo.service.ZaloBackgroundService,com.zing.zalo.service.ZaloKeepAliveService,com.zing.zalo.service.PlatformService,com.zing.zalo.receiver.ZaloNotificationReceiver,com.zing.zalo/com.google.firebase.messaging.FirebaseMessagingService", "com.zing.zalo,com.zing.zalo:push,com.zing.zalo:service,com.zing.zalo:background"),
        AppInfo("com.zing.zalo.lite", "com.zing.zalo.lite.push.ZaloLiteFCMService", "com.zing.zalo.lite"),
        AppInfo("com.zing.zalo.pc", "com.zing.zalo.pc.service.ZaloPcPushService", "com.zing.zalo.pc"),
        AppInfo("com.skype.raider", "com.skype.raider.service.PushService,com.skype.raider.service.SyncService", "com.skype.raider,com.skype.raider:push"),
        
        // E-commerce & Delivery
        AppInfo("com.shopee.vn", "com.shopee.app.push.fcm.ShopeeFcmService,com.shopee.app.service.SyncService", "com.shopee.vn,com.shopee.vn:push"),
        AppInfo("com.shopee.lite", "com.shopee.lite.push.ShopeeLiteFCMService", "com.shopee.lite"),
        AppInfo("com.lazada.android", "com.lazada.android.push.LazadaFcmService,com.lazada.android.service.SyncService", "com.lazada.android,com.lazada.android:push"),
        AppInfo("com.grabtaxi.passenger", "com.grabtaxi.passenger.push.GrabFirebaseMessagingService,com.grabtaxi.passenger.service.SyncService", "com.grabtaxi.passenger,com.grabtaxi.passenger:push"),
        AppInfo("com.grabtaxi.passenger.lite", "com.grabtaxi.passenger.lite.push.GrabLiteFCMService", "com.grabtaxi.passenger.lite"),
        AppInfo("com.mservice.momotransfer", "com.mservice.momotransfer.push.MoMoFirebaseMessagingService,com.mservice.momotransfer.service.SyncService", "com.mservice.momotransfer,com.mservice.momotransfer:push"),
        
        // Entertainment
        AppInfo("com.fptplay", "com.fptplay.service.PushService,com.fptplay.service.SyncService", "com.fptplay,com.fptplay:push"),
        AppInfo("com.vtvgo", "com.vtvgo.service.PushService,com.vtvgo.service.SyncService", "com.vtvgo,com.vtvgo:push"),
        AppInfo("com.zhiliaoapp.musically", "com.ss.android.ugc.aweme.push.AwemeFcmService,com.ss.android.ugc.aweme.service.SyncService", "com.zhiliaoapp.musically,com.zhiliaoapp.musically:push"),
        AppInfo("com.google.android.youtube", "com.google.android.apps.youtube.app.notification.NotificationService,com.google.android.apps.youtube.app.background.service.YouTubeBackgroundService", "com.google.android.youtube,com.google.android.youtube:background"),
        AppInfo("com.google.android.apps.youtube.mango", "com.google.android.apps.youtube.mango.notification.NotificationService", "com.google.android.apps.youtube.mango"),
        
        // Microsoft Apps
        AppInfo("com.microsoft.office.outlook", "com.microsoft.office.outlook.notifications.OutlookFirebaseMessagingService,com.microsoft.office.outlook.sync.OutlookSyncService", "com.microsoft.office.outlook,com.microsoft.office.outlook:push"),
        AppInfo("com.microsoft.outlooklite", "com.microsoft.outlooklite.push.OutlookLiteFCMService", "com.microsoft.outlooklite"),
        AppInfo("com.microsoft.office.outlook.beta", "com.microsoft.office.outlook.notifications.OutlookFirebaseMessagingService", "com.microsoft.office.outlook.beta"),
        AppInfo("com.microsoft.teams", "com.microsoft.teams.services.push.TeamsFirebaseMessagingService,com.microsoft.teams.services.sync.TeamsSyncService", "com.microsoft.teams,com.microsoft.teams:push"),
        AppInfo("com.microsoft.teams.lite", "com.microsoft.teams.lite.push.TeamsLiteFCMService", "com.microsoft.teams.lite"),
        AppInfo("com.microsoft.teams.beta", "com.microsoft.teams.services.push.TeamsFirebaseMessagingService", "com.microsoft.teams.beta"),
        
        // Google Apps
        AppInfo("com.google.android.gm", "com.google.android.gm.notification.GmailFirebaseMessagingService,com.google.android.gm.ItemListsService,com.google.android.gm.provider.MailSyncAdapterService,com.google.android.gm.provider.GmailPop3SyncAdapterService,com.google.android.gm.provider.GmailImapSyncAdapterService,com.google.android.gm.GmailIntentService,com.google.android.gm.GmailNotificationActionIntentService,com.google.android.libraries.hub.firebase.FirebaseMessagingServiceImpl,com.google.firebase.messaging.FirebaseMessagingService,com.android.email.service.Pop3Service,com.android.email.service.ImapService,com.android.email.service.AttachmentService,com.android.email.service.AccountService,com.android.email.service.PolicyService", "com.google.android.gm,com.google.android.gm:sync,com.google.android.gm:push,com.google.android.gm:exchange"),
        AppInfo("com.google.android.gm.lite", "com.google.android.gm.lite.notification.GmailFirebaseMessagingService", "com.google.android.gm.lite"),
        AppInfo("com.google.android.apps.photos", "com.google.android.libraries.notifications.platform.entrypoints.firebase.FirebaseMessagingServiceImpl,com.google.android.libraries.social.async.BackgroundTaskJobService,androidx.work.impl.background.systemjob.SystemJobService", "com.google.android.apps.photos"),
        AppInfo("com.google.android.projection.gearhead", "com.google.android.gms.car.CarFirebaseMessagingService", "com.google.android.projection.gearhead"),
        AppInfo("com.google.android.ext.services", "", ""),
        
        // Health
        AppInfo("com.huawei.health", "com.huawei.bone.ui.setting.NotificationPushListener", "com.huawei.health")
    )

    /**
     * Apply whitelist to all apps
     */
    suspend fun applyWhitelist(onProgress: (String) -> Unit): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            onProgress("[INFO] Bắt đầu whitelist ${apps.size} ứng dụng...")
            onProgress("")

            // Step 1: Build master lists
            val allPackages = apps.map { it.packageName }
            val allServices = apps.filter { it.services.isNotEmpty() }.map { it.services }
            val allProcesses = apps.filter { it.processes.isNotEmpty() }.flatMap { it.processes.split(",") }

            onProgress("[1/5] Chuẩn bị danh sách...")
            onProgress("  → ${allPackages.size} packages")
            onProgress("  → ${allServices.size} service groups")
            onProgress("  → ${allProcesses.size} processes")
            onProgress("")

            // Step 2: Apply PowerKeeper settings
            onProgress("[2/5] Cấu hình PowerKeeper...")
            applyPowerKeeperSettings(onProgress)
            onProgress("")

            // Step 3: Whitelist packages
            onProgress("[3/5] Whitelist packages...")
            val packageList = allPackages.joinToString(",")
            executeSettingCommand("system", "power_pkg_white_list", packageList, onProgress)
            onProgress("")

            // Step 4: Configure autostart
            onProgress("[4/5] Cấu hình autostart...")
            applyAutostartSettings(allPackages, onProgress)
            onProgress("")

            // Step 5: Apply per-app settings
            onProgress("[5/5] Cấu hình từng ứng dụng...")
            apps.forEachIndexed { index, app ->
                onProgress("  [${index + 1}/${apps.size}] ${app.packageName}")
                applyAppSpecificSettings(app, onProgress)
            }
            onProgress("")

            onProgress("[✓] Hoàn tất whitelist!")
            Result.success(Unit)

        } catch (e: Exception) {
            onProgress("[ERROR] ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Apply PowerKeeper global settings
     */
    private suspend fun applyPowerKeeperSettings(onProgress: (String) -> Unit) {
        val settings = mapOf(
            "system" to mapOf(
                "power_supersave_mode_open" to "false",
                "POWER_HIDE_MODE_ENABLED" to "false"
            ),
            "secure" to mapOf(
                "POWER_SAVE_MODE_OPEN" to "0"
            ),
            "global" to mapOf(
                "EXTREME_POWER_MODE_ENABLE" to "false"
            )
        )

        settings.forEach { (namespace, settingsMap) ->
            settingsMap.forEach { (key, value) ->
                executeSettingCommand(namespace, key, value, onProgress, quiet = true)
            }
        }
        onProgress("  ✓ PowerKeeper settings applied")
    }

    /**
     * Apply autostart settings
     */
    private suspend fun applyAutostartSettings(packages: List<String>, onProgress: (String) -> Unit) {
        val autostartList = packages.joinToString(";") { "$it,10000" }
        executeSettingCommand("secure", "AutoStart_Enable_AppList", autostartList, onProgress, quiet = true)
        onProgress("  ✓ Autostart configured")
    }

    /**
     * Apply app-specific settings
     */
    private suspend fun applyAppSpecificSettings(app: AppInfo, onProgress: (String) -> Unit) {
        // Configure per-app settings
        val appSettings = mapOf(
            "autostart" to "true",
            "background_start_enable" to "true",
            "powerkeeper_exclude" to "true"
        )

        appSettings.forEach { (setting, value) ->
            val key = "${app.packageName}_$setting"
            executeSettingCommand("secure", key, value, onProgress, quiet = true)
        }
    }

    /**
     * Execute a settings command
     */
    private suspend fun executeSettingCommand(
        namespace: String,
        key: String,
        value: String,
        onProgress: (String) -> Unit,
        quiet: Boolean = false
    ) {
        val command = "settings put $namespace $key \"$value\""
        val (exitCode, output) = shizukuHelper.executeCommand(command)

        if (!quiet) {
            if (exitCode == 0) {
                onProgress("  ✓ $namespace.$key = $value")
            } else {
                onProgress("  ✗ Failed: $namespace.$key")
                if (output.isNotEmpty()) {
                    onProgress("    $output")
                }
            }
        }
    }
}
