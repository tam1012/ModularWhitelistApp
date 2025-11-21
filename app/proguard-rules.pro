# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in [sdk]/tools/proguard/proguard-android.txt

# Keep Shizuku classes
-keep class rikka.shizuku.** { *; }
-keep class moe.shizuku.** { *; }

# Keep app classes
-keep class com.havantam.modularwhitelist.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Don't warn about missing classes
-dontwarn rikka.shizuku.**
-dontwarn moe.shizuku.**
