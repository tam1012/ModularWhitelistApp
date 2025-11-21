# MODULAR WHITELIST - Android App

![Build Status](https://github.com/YOUR_USERNAME/ModularWhitelistApp/workflows/Build%20APK/badge.svg)

**Xiaomi PowerKeeper Whitelist Tool** - Chạy trực tiếp trên điện thoại với Shizuku!

*By Ha Van Tam (babyinmyl0v3) - ae vOz - Telegram: @ThongThaiTuaThanTien*

---

## ✨ Tính năng

- ✅ **Chạy trực tiếp trên Android** - Không cần PC
- ✅ **Whitelist 60+ apps** tự động (GMS, Banking, Social, E-commerce)
- ✅ **PowerKeeper optimization** - Tắt các chế độ tiết kiệm pin cực đoan
- ✅ **Autostart configuration** - Tự động khởi động apps
- ✅ **Material Design 3** - Giao diện hiện đại
- ✅ **Realtime logging** - Theo dõi quá trình whitelist

## 📋 Yêu cầu

1. **Android 7.0+** (API 24+)
2. **Shizuku app** - Cài từ [Play Store](https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api) hoặc [GitHub](https://github.com/RikkaApps/Shizuku/releases)
3. **ADB** - Để kích hoạt Shizuku lần đầu (chỉ 1 lần)

## 🚀 Hướng dẫn sử dụng

### Bước 1: Tải APK

**Option A: Download từ GitHub Actions** (Khuyến nghị)
1. Vào tab [Actions](https://github.com/YOUR_USERNAME/ModularWhitelistApp/actions)
2. Click vào workflow run mới nhất có ✅
3. Scroll xuống phần **Artifacts**
4. Download `MODULAR_WHITELIST-debug-apk`

**Option B: Build từ source** (Xem `BUILD_INSTRUCTIONS.md`)

### Bước 2: Cài đặt Shizuku

» Xem hướng dẫn chi tiết tại [`SETUP_SHIZUKU.md`](SETUP_SHIZUKU.md)

**Tóm tắt:**
```bash
# Kết nối điện thoại qua USB, bật USB Debugging
adb shell sh /storage/emulated/0/Android/data/moe.shizuku.privileged.api/start.sh
```

Sau khi setup 1 lần, Shizuku sẽ tự chạy khi khởi động điện thoại (nếu dùng Wireless ADB).

### Bước 3: Chạy app

1. Cài APK đã download
2. Mở app **MODULAR WHITELIST**
3. App sẽ tự request quyền Shizuku → **Allow**
4. Nhấn nút **"Apply Whitelist"**
5. Đợi ~30 giây → Hoàn tất!

## 📱 Screenshots

```
┌────────────────────────────┐
│  MODULAR WHITELIST         │
│  by Ha Van Tam - ae vOz    │
├────────────────────────────┤
│  Status: ✅ Sẵn sàng       │
│                            │
│  [Apply Whitelist]         │
│                            │
│  Log Output:               │
│  ┌──────────────────────┐ │
│  │ [1/5] Chuẩn bị...    │ │
│  │ [2/5] PowerKeeper... │ │
│  │ ...                  │ │
│  └──────────────────────┘ │
└────────────────────────────┘
```

## 🏗️ Build APK tự động với GitHub Actions

### Setup GitHub Repository

1. **Tạo repository mới** trên GitHub
   ```
   Repository name: ModularWhitelistApp
   Public/Private: Tùy chọn
   ```

2. **Push code lên GitHub**
   ```bash
   cd c:\Users\tam10\Documents\Gemini\ADB\ModularWhitelistApp
   git init
   git add .
   git commit -m "Initial commit - MODULAR WHITELIST Android App"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/ModularWhitelistApp.git
   git push -u origin main
   ```

3. **GitHub Actions tự động build**
   - Sau khi push, vào tab **Actions**
   - Workflow "Build APK" sẽ chạy tự động
   - Đợi ~3-5 phút
   - Download APK từ **Artifacts**

### Trigger build thủ công

Vào Actions → Build APK → **Run workflow** → Run

## 🔧 Whitelisted Apps

App bao gồm whitelist cho **60+ apps**:

**Google Services:**
- Google Play Services (GMS)
- Google Play Store
- Gmail, Google Photos
- Chrome, YouTube

**Banking (Vietnam):**
- Agribank, BIDV, MBBank, Techcombank
- TPBank, Vietcombank, VietinBank
- VPBank, VIB, SHB, VNPay

**Social & Messaging:**
- Facebook, Messenger, Instagram
- WhatsApp, Telegram, Zalo
- LINE, Viber, Discord, Twitter

**E-commerce:**
- Shopee, Lazada, Grab, MoMo

**Others:**
- Microsoft Outlook, Teams
- Xiaomi Services (XMSF)

*Xem danh sách đầy đủ trong `WhitelistManager.kt`*

## ⚙️ PowerKeeper Settings

App tự động cấu hình:

**System settings:**
- Tắt Super Power Save Mode
- Tắt Hide Mode
- Whitelist packages

**Secure settings:**
- Cấu hình Autostart
- Background start enable
- PowerKeeper exclusions

## 🛠️ Development

**Tech stack:**
- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Dependencies:** Shizuku API 13.1.5, Material Components
- **Build:** Gradle 8.2, Kotlin 1.9.20

**Project structure:**
```
ModularWhitelistApp/
├── app/src/main/java/com/havantam/modularwhitelist/
│   ├── MainActivity.kt          # UI logic
│   ├── ShizukuHelper.kt         # Shizuku wrapper
│   └── WhitelistManager.kt      # Whitelist logic (60+ apps)
├── .github/workflows/build.yml  # GitHub Actions
└── README.md                    # This file
```

## ⚠️ Lưu ý

- **Shizuku required:** App cần Shizuku để chạy commands với quyền ADB
- **MIUI limitations:** Một số settings có thể bị MIUI chặn ngay cả với Shizuku
- **Cloud sync:** MIUI có thể sync settings từ cloud, whitelist có thể bị override
- **China ROM:** Một số tính năng có thể không work trên China ROM

## 📄 License

MIT License - Free to use and modify

## 🙏 Credits

- **Original batch script:** MODULAR_WHITELIST.bat
- **Shizuku:** [RikkaApps/Shizuku](https://github.com/RikkaApps/Shizuku)
- **Author:** Ha Van Tam - babyinmyl0v3 - ae vOz

## 📞 Contact

- **Telegram:** @ThongThaiTuaThanTien
- **Forum:** ae vOz

---

**⭐ Nếu app hữu ích, hãy star repo này!**
