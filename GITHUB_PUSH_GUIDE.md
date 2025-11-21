# Hướng dẫn Push Code lên GitHub

## 📦 Yêu cầu

- ✅ Git đã cài (đang cài...)
- ✅ GitHub account (tạo tại https://github.com)

---

## 🚀 Các bước thực hiện

### Bước 1: Tạo Repository trên GitHub

1. **Đăng nhập GitHub:** https://github.com
2. **Tạo repository mới:**
   - Click nút **"+"** (góc trên bên phải) → **"New repository"**
   - Hoặc vào trực tiếp: https://github.com/new

3. **Điền thông tin:**
   ```
   Repository name: ModularWhitelistApp
   Description: MODULAR WHITELIST - Xiaomi PowerKeeper Whitelist Tool for Android
   Public/Private: Public (khuyến nghị để GitHub Actions free)
   
   ❌ KHÔNG tick "Add a README file"
   ❌ KHÔNG tick "Add .gitignore"
   ❌ KHÔNG tick "Choose a license"
   ```

4. **Click "Create repository"**

GitHub sẽ hiển thị 1 trang với commands. **Để trang đó mở**, bạn sẽ cần copy URL.

---

### Bước 2: Cấu hình Git (lần đầu tiên)

Mở **PowerShell** hoặc **Git Bash**, chạy:

```bash
# Cấu hình tên
git config --global user.name "Ha Van Tam"

# Cấu hình email (dùng email GitHub của bạn)
git config --global user.email "your-email@example.com"
```

*(Thay `your-email@example.com` bằng email bạn dùng đăng ký GitHub)*

---

### Bước 3: Initialize Git trong Project

```bash
# Di chuyển vào thư mục project
cd c:\Users\tam10\Documents\Gemini\ADB\ModularWhitelistApp

# Initialize git repository
git init

# Kiểm tra status
git status
```

**Output mong đợi:** Danh sách các files chưa được tracked (màu đỏ)

---

### Bước 4: Add Files vào Git

```bash
# Add tất cả files
git add .

# Kiểm tra lại
git status
```

**Output mong đợi:** Danh sách files màu xanh (ready to commit)

---

### Bước 5: Commit Changes

```bash
git commit -m "Initial commit - MODULAR WHITELIST Android App with Shizuku"
```

**Output mong đợi:** 
```
[master (root-commit) abc1234] Initial commit - MODULAR WHITELIST Android App with Shizuku
 25 files changed, 1500 insertions(+)
 create mode 100644 README.md
 ...
```

---

### Bước 6: Rename Branch sang "main"

```bash
git branch -M main
```

*(GitHub mặc định dùng "main", không phải "master")*

---

### Bước 7: Link với GitHub Repository

```bash
# Thay YOUR_USERNAME bằng GitHub username của bạn
git remote add origin https://github.com/YOUR_USERNAME/ModularWhitelistApp.git

# Kiểm tra
git remote -v
```

**Example:**
```bash
# Nếu username là "babyinmyl0v3"
git remote add origin https://github.com/babyinmyl0v3/ModularWhitelistApp.git
```

**Output mong đợi:**
```
origin  https://github.com/YOUR_USERNAME/ModularWhitelistApp.git (fetch)
origin  https://github.com/YOUR_USERNAME/ModularWhitelistApp.git (push)
```

---

### Bước 8: Push Code lên GitHub

```bash
git push -u origin main
```

**Lần đầu push:** GitHub sẽ yêu cầu đăng nhập:
- Nhập **username**
- Nhập **password** (hoặc **Personal Access Token** nếu bật 2FA)

**Output mong đợi:**
```
Enumerating objects: 30, done.
Counting objects: 100% (30/30), done.
...
To https://github.com/YOUR_USERNAME/ModularWhitelistApp.git
 * [new branch]      main -> main
Branch 'main' set up to track remote branch 'main' from 'origin'.
```

---

## ✅ Verification

### Kiểm tra trên GitHub:

1. Vào repository: `https://github.com/YOUR_USERNAME/ModularWhitelistApp`
2. Bạn sẽ thấy:
   - ✅ Tất cả files đã upload
   - ✅ `README.md` hiển thị ở trang chủ
   - ✅ Commit message: "Initial commit..."

### Kiểm tra GitHub Actions:

1. Vào tab **"Actions"** trên repository
2. Bạn sẽ thấy workflow **"Build APK"** tự động chạy
3. Đợi ~3-5 phút
4. Status: ✅ (màu xanh) = build thành công

---

## 📥 Download APK

Sau khi GitHub Actions build xong:

1. Vào tab **Actions**
2. Click vào workflow run có ✅
3. Scroll xuống phần **Artifacts**
4. Click **"MODULAR_WHITELIST-debug-apk"** để download
5. Giải nén file ZIP → có file `.apk`

---

## 🔄 Update Code sau này

Khi bạn sửa code và muốn push lại:

```bash
# Kiểm tra files thay đổi
git status

# Add files đã thay đổi
git add .

# Commit với message mô tả
git commit -m "Fix: improved whitelist logic"

# Push lên GitHub
git push
```

GitHub Actions sẽ tự động build lại APK mới!

---

## ❓ Troubleshooting

### "Permission denied (publickey)"

**Giải pháp:** Dùng HTTPS thay vì SSH (đã dùng HTTPS ở trên)

### "Authentication failed"

**Giải pháp 1:** Tạo Personal Access Token
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token
3. Chọn scope: `repo` (full control)
4. Copy token
5. Dùng token thay vì password khi push

**Giải pháp 2:** Dùng GitHub Desktop app
- Download: https://desktop.github.com
- Login và push qua GUI

### "fatal: not a git repository"

**Giải pháp:** Chạy `git init` trong thư mục project

### "error: failed to push some refs"

**Giải pháp:** Pull trước khi push
```bash
git pull origin main --rebase
git push
```

---

## 📌 Quick Reference

**Clone về máy khác:**
```bash
git clone https://github.com/YOUR_USERNAME/ModularWhitelistApp.git
```

**Xem history:**
```bash
git log --oneline
```

**Undo commit (chưa push):**
```bash
git reset --soft HEAD~1
```

**Xóa file khỏi Git (giữ ở local):**
```bash
git rm --cached filename
git commit -m "Remove file"
```

---

**🎉 Done! Code của bạn giờ đã trên GitHub và APK sẽ tự động build!**

*Guide by Ha Van Tam - babyinmyl0v3 - ae vOz*
