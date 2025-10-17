# 🔧 Fix Devanagari Text Display in MySQL

## Problem
When you view the database, Bhojpuri text shows as `???` instead of `नमस्कार, कैसे बानी?`

## Why This Happens
MySQL needs to be configured to use **UTF-8MB4** encoding to properly store and display Devanagari script (Hindi/Bhojpuri characters).

---

## 🚀 Quick Fix (Automatic)

### Method 1: Run the Fix Script
```powershell
.\fix-encoding.bat
```

**This will:**
- ✅ Drop and recreate database with UTF-8MB4
- ✅ Create tables with proper encoding
- ✅ Insert test data (नमस्कार, कैसे बानी?)
- ✅ Verify encoding works

⚠️ **Warning:** This deletes all existing data!

---

## 🔧 Manual Fix

### Step 1: Run SQL Script
```powershell
mysql -u root -pkali < fix_mysql_encoding.sql
```

### Step 2: Verify Encoding
```powershell
mysql -u root -pkali bhojpuri_billa
```

```sql
-- Check test data
SELECT * FROM translations;
```

**You should see:**
```
नमस्कार, कैसे बानी?
```

**NOT:**
```
???
```

---

## 🔍 Check Your Current Encoding

### In MySQL:
```sql
USE bhojpuri_billa;

-- Check database encoding
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'bhojpuri_billa';

-- Check table encoding
SHOW TABLE STATUS WHERE Name = 'translations';

-- Check column encoding
SELECT COLUMN_NAME, CHARACTER_SET_NAME, COLLATION_NAME
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'bhojpuri_billa'
  AND TABLE_NAME = 'translations'
  AND COLUMN_NAME IN ('english_text', 'bhojpuri_text');
```

**Expected Results:**
```
Database: utf8mb4 / utf8mb4_unicode_ci
Table:    utf8mb4 / utf8mb4_unicode_ci
Columns:  utf8mb4 / utf8mb4_unicode_ci
```

---

## 🖥️ Fix MySQL Client Display

### Issue: MySQL Command Line Shows ???
Even with correct encoding, the **Windows Command Prompt** may not display Devanagari.

### Solution 1: Use MySQL Workbench (Recommended)
MySQL Workbench handles UTF-8 properly:
1. Open MySQL Workbench
2. Connect to localhost:3306 (root/kali)
3. Open bhojpuri_billa database
4. View translations table
5. You'll see proper Devanagari! ✅

### Solution 2: Fix Command Prompt
```powershell
# Change code page to UTF-8
chcp 65001

# Then connect to MySQL
mysql -u root -pkali --default-character-set=utf8mb4 bhojpuri_billa
```

```sql
SET NAMES 'utf8mb4';
SELECT * FROM translations;
```

### Solution 3: Export to File
```powershell
mysql -u root -pkali --default-character-set=utf8mb4 bhojpuri_billa -e "SELECT * FROM translations" > output.txt
```

Then open `output.txt` in Notepad++ or VS Code with UTF-8 encoding.

---

## 🎯 What We Fixed in the Code

### Before:
```java
connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
```

### After:
```java
String CONNECTION_PARAMS = "?useUnicode=true&characterEncoding=utf8&useSSL=false";
connection = DriverManager.getConnection(DB_URL + DB_NAME + CONNECTION_PARAMS, DB_USER, DB_PASSWORD);

// Set connection character set
try (Statement stmt = connection.createStatement()) {
    stmt.execute("SET NAMES 'utf8mb4'");
    stmt.execute("SET CHARACTER SET utf8mb4");
    stmt.execute("SET character_set_connection=utf8mb4");
}
```

This ensures:
- ✅ Connection uses UTF-8
- ✅ All queries use UTF-8MB4
- ✅ Data stored correctly
- ✅ Data retrieved correctly

---

## 🧪 Test It

### Step 1: Rebuild App
```powershell
mvn clean package
```

### Step 2: Run App
```powershell
.\run-jar.bat
```

### Step 3: Record Something
Press SPACE, say "Hello, how are you?", release SPACE

### Step 4: Check Database
```sql
USE bhojpuri_billa;
SELECT bhojpuri_text FROM translations ORDER BY created_at DESC LIMIT 1;
```

**You should see:** `नमस्कार, कैसे बानी?` or similar Bhojpuri text

---

## 📊 Verify in Different Tools

### MySQL Command Line:
```powershell
mysql -u root -pkali --default-character-set=utf8mb4 bhojpuri_billa
```
```sql
SELECT * FROM translations;
```

### MySQL Workbench:
1. File → New Query Tab
2. Run: `SELECT * FROM translations;`
3. Results show Devanagari ✅

### DBeaver:
1. Connect to MySQL
2. Open bhojpuri_billa
3. Browse translations table
4. Text displays correctly ✅

### phpMyAdmin:
1. Login to phpMyAdmin
2. Select bhojpuri_billa
3. Browse translations
4. Devanagari displays ✅

---

## 🔧 My.ini Configuration (Optional)

To make UTF-8MB4 default for all MySQL databases:

**Edit:** `C:\ProgramData\MySQL\MySQL Server 8.0\my.ini`

**Add under [mysqld]:**
```ini
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci

[client]
default-character-set=utf8mb4

[mysql]
default-character-set=utf8mb4
```

**Restart MySQL:**
```powershell
net stop MySQL80
net start MySQL80
```

---

## 🐛 Troubleshooting

### Problem: Still seeing ???

**Cause 1: Wrong Encoding on Connection**
```java
// Make sure you rebuilt the app after code changes
mvn clean package
```

**Cause 2: Old Data**
Data inserted before the fix will still be corrupted. Run:
```powershell
.\fix-encoding.bat
```

**Cause 3: Console Display Issue**
The data might be stored correctly, but your console can't display it.
- ✅ Use MySQL Workbench instead
- ✅ Or check in the Java app UI (should display correctly)

**Cause 4: MySQL Client Settings**
```powershell
mysql -u root -pkali --default-character-set=utf8mb4
```

### Problem: "Unknown character set: 'utf8mb4'"

**Solution:** Update MySQL to 5.5.3 or higher
```powershell
mysql --version
```

---

## ✅ Success Checklist

After running the fix, verify:

- [ ] Database encoding: `utf8mb4_unicode_ci`
- [ ] Table encoding: `utf8mb4_unicode_ci`
- [ ] Column encoding: `utf8mb4_unicode_ci`
- [ ] Test data shows: `नमस्कार, कैसे बानी?`
- [ ] Java app compiled with new connection code
- [ ] New recordings save Devanagari correctly
- [ ] MySQL Workbench displays Devanagari ✅

---

## 📝 Files Changed

1. ✅ **DatabaseManager.java** - Added UTF-8 connection parameters
2. ✅ **fix_mysql_encoding.sql** - SQL script to fix encoding
3. ✅ **fix-encoding.bat** - Batch file to run the fix
4. ✅ **FIX_DEVANAGARI_ENCODING.md** - This guide

---

## 🎯 Summary

**Problem:** MySQL showing `???` instead of `नमस्कार`

**Root Cause:** Database/tables not using UTF-8MB4 encoding

**Solution:**
1. Run `.\fix-encoding.bat` (recreates database)
2. Rebuild app: `mvn clean package`
3. Run app: `.\run-jar.bat`
4. Use MySQL Workbench to view data (best display)

**Result:** Devanagari text displays perfectly! ✨

---

**Need help?** Check the data:
```sql
USE bhojpuri_billa;
SELECT 
  bhojpuri_text,
  HEX(bhojpuri_text) as hex_value,
  LENGTH(bhojpuri_text) as byte_length,
  CHAR_LENGTH(bhojpuri_text) as char_length
FROM translations
LIMIT 1;
```

If `hex_value` shows proper UTF-8 bytes but display is still wrong, it's a client display issue, not a storage issue!
