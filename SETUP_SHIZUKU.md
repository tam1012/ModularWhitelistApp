# Hướng dẫn cài đặt và sử dụng Shizuku

## Shizuku là gì?

**Shizuku** là một framework cho phép các app thường sử dụng quyền ADB mà không cần root. Nó hoạt động bằng cách tạo một "dịch vụ" chạy với quyền ADB, và các app có thể gọi dịch vụ này để thực thi commands.

**Ưu điểm:**
- ✅ Không cần root
- ✅ Quyền bền vững (không mất khi reboot nếu dùng Wireless ADB)
- ✅ An toàn - User phải cấp quyền cho từng app

## Cài đặt Shizuku

### Bước 1: Download Shizuku App

**Option A: Google Play Store** (Khuyến nghị)
```
https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api
```

**Option B: GitHub Releases**
```
https://github.com/RikkaApps/Shizuku/releases
```
Download file `.apk` mới nhất và cài đặt.

### Bước 2: Kích hoạt Shizuku qua ADB

#### Requirements:
- PC với ADB
- USB cable
- USB Debugging enabled trên điện thoại

#### Steps:

**1. Bật USB Debugging:**
   - Vào Settings → About Phone
   - Tap 7 lần vào "MIUI Version" để bật Developer Mode
   - Vào Settings → Additional Settings → Developer Options
   - Bật "USB Debugging"

**2. Kết nối điện thoại với PC:**
   ```bash
   adb devices
   ```
   Điện thoại sẽ hiện popup xin phép → **Allow**

**3. Mở Shizuku app** trên điện thoại

**4. Trong Shizuku app:**
   - Tap vào "Start via Wireless ADB" (hoặc "Start via USB")
   - Shizuku sẽ hiển thị 1 command dạng:
     ```bash
     adb shell sh /storage/emulated/0/Android/data/moe.shizuku.privileged.api/start.sh
     ```

**5. Copy command đó và chạy trên PC:**
   ```bash
   # Example command (có thể khác trên máy bạn)
   adb shell sh /storage/emulated/0/Android/data/moe.shizuku.privileged.api/start.sh
   ```

**6. Shizuku sẽ start!**
   - Shizuku app sẽ hiển thị "Shizuku is running"
   - Icon notification sẽ xuất hiện

## ✅ Verification

Sau khi start Shizuku:
1. Mở Shizuku app
2. Status phải là **"Shizuku is running"** (màu xanh)
3. Có thể thấy danh sách "Authorized apps" (nếu đã cấp quyền cho app nào)

## 🔄 Shizuku & Reboot

### Nếu dùng USB ADB:
- ❌ Shizuku sẽ **STOP** khi reboot
- 🔁 Phải chạy lại command start qua ADB sau mỗi lần reboot

### Nếu dùng Wireless ADB:
- ✅ Shizuku có thể **TỰ ĐỘNG START** sau reboot (nếu cấu hình đúng)
- Cần enable "Start Shizuku on Boot" trong Shizuku settings

**Cách chuyển sang Wireless ADB:**
```bash
# Kết nối qua USB trước
adb tcpip 5555

# Sau đó ngắt USB, dùng WiFi
adb connect PHONE_IP:5555

# Example:
adb connect 192.168.1.100:5555
```

## 🔐 Cấp quyền cho MODULAR WHITELIST app

1. Sau khi Shizuku đang chạy
2. Mở app **MODULAR WHITELIST**
3. App sẽ tự động request quyền Shizuku
4. Popup sẽ hiện → **Allow**
5. Shizuku app sẽ list "com.havantam.modularwhitelist" trong Authorized apps

## ❓ Troubleshooting

### "Shizuku is not running"
**Giải pháp:** Chạy lại command start qua ADB (Bước 2.5 ở trên)

### "Permission denied"
**Giải pháp:** 
- Kiểm tra USB Debugging đã bật chưa
- Thử revoke và allow lại trên điện thoại
- Chạy `adb kill-server` và `adb start-server`

### "Could not find start script"
**Giải pháp:**
- Mở Shizuku app lần đầu
- Shizuku sẽ tự tạo script file
- Sau đó chạy lại command

### App không detect được Shizuku
**Giải pháp:**
- Kiểm tra Shizuku app version (cần >= 13.0)
- Reinstall Shizuku
- Reinstall MODULAR WHITELIST app

### Shizuku stop sau khi ngắt USB
**Giải pháp:**
- Đây là bình thường với USB ADB mode
- Chuyển sang Wireless ADB để Shizuku persist sau reboot

## 📱 Shizuku Settings (Nâng cao)

Trong Shizuku app:

**Start on boot:**
- Enable để Shizuku tự start khi điện thoại bật (chỉ với Wireless ADB)

**Enhanced mode:**
- Enable để cải thiện performance (cần root)

**Safemode:**
- Disable để cho phép tất cả apps request permission

## 🔗 Resources

- **Shizuku GitHub:** https://github.com/RikkaApps/Shizuku
- **Shizuku Docs:** https://shizuku.rikka.app/
- **ADB Download:** https://developer.android.com/tools/releases/platform-tools

---

## 🎯 Next Steps

Sau khi Shizuku đã running:
1. Mở MODULAR WHITELIST app
2. Cấp quyền Shizuku
3. Nhấn "Apply Whitelist"
4. Done! ✅

---

*Hướng dẫn by Ha Van Tam - babyinmyl0v3 - ae vOz*
