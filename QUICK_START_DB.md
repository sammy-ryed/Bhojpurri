# 🎯 Quick Start Guide - MySQL Integration

## What's New?

Your Bhojpuri Billa app now **automatically saves everything to MySQL database**!

## What Gets Saved?

Every time you use the app:
- ✅ Audio file location
- ✅ Audio file size
- ✅ English transcription
- ✅ Bhojpuri translation (in Devanagari: नमस्कार, कैसे बानी?)
- ✅ TTS output file location
- ✅ Timestamp

## Quick Setup (3 Steps)

### 1. Start MySQL
```powershell
net start MySQL80
```

### 2. Test Database (Optional but recommended)
```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="com.bhojpurri.TestDatabase"
```

You should see: `✅ ALL DATABASE TESTS PASSED!`

### 3. Run the App
```powershell
.\run-jar.bat
```

That's it! 🎉

## How to See Your Data

### Option 1: MySQL Command Line
```powershell
mysql -u root -pkali
```

```sql
USE bhojpuri_billa;

-- See all your translations
SELECT * FROM translations ORDER BY created_at DESC;

-- See today's stats
SELECT * FROM usage_stats WHERE date = CURDATE();
```

### Option 2: MySQL Workbench (GUI)
1. Open MySQL Workbench
2. Connect with:
   - Host: `localhost`
   - Port: `3306`
   - User: `root`
   - Password: `kali`
3. Select database: `bhojpuri_billa`
4. Browse tables: `translations`, `usage_stats`

### Option 3: In the App
When you translate, you'll see:
```
English: Hello, how are you?
Bhojpuri: नमस्कार, कैसे बानी?
💾 Saved to database (ID: 123)
---
```

## Database Schema

### `translations` Table
```
id                  INT (auto)
audio_file_path     VARCHAR(500)    - Where your recording is saved
audio_file_size     BIGINT          - Size in bytes
english_text        TEXT            - What you said in English
bhojpuri_text       TEXT            - Bhojpuri translation (Devanagari)
tts_file_path       VARCHAR(500)    - Where TTS audio is saved
created_at          TIMESTAMP       - When you said it
updated_at          TIMESTAMP       - Last modified
```

### `usage_stats` Table
```
id                  INT (auto)
date                DATE            - Which day
total_recordings    INT             - How many times you spoke
total_translations  INT             - How many translations
total_audio_size    BIGINT          - Total audio data (bytes)
created_at          TIMESTAMP
updated_at          TIMESTAMP
```

## Example Queries

### Count your translations
```sql
SELECT COUNT(*) FROM translations;
```

### Find specific translation
```sql
SELECT english_text, bhojpuri_text 
FROM translations 
WHERE english_text LIKE '%hello%';
```

### See how much you've used the app
```sql
SELECT date, total_recordings 
FROM usage_stats 
ORDER BY date DESC;
```

### Total audio data stored
```sql
SELECT SUM(audio_file_size) / 1024 / 1024 AS total_mb 
FROM translations;
```

## Files Changed

New files:
- ✅ `DatabaseManager.java` - Handles all database operations
- ✅ `TestDatabase.java` - Test program to verify database
- ✅ `DATABASE_README.md` - Full documentation
- ✅ `QUICK_START_DB.md` - This file!

Modified files:
- ✅ `BilluUI.java` - Now saves to database after each translation
- ✅ `TTSManager.java` - Returns TTS file path
- ✅ `pom.xml` - Added MySQL connector dependency

## Troubleshooting

### MySQL not starting?
```powershell
# Check if running
sc query MySQL80

# Start it
net start MySQL80
```

### Wrong password?
```sql
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'kali';
FLUSH PRIVILEGES;
```

### Can't connect?
Check if MySQL is running on port 3306:
```powershell
netstat -an | findstr 3306
```

## What If I Don't Have MySQL?

The app will still work for recording/transcription/translation! You'll just see an error message about database connection failing, but everything else works fine.

To install MySQL:
1. Download: https://dev.mysql.com/downloads/installer/
2. Install MySQL Community Server
3. Set root password to: `kali`
4. Run: `net start MySQL80`

## Your Data is Safe

- 🔒 All data stored **locally** on your computer
- 🔒 No data sent to external servers (except API calls)
- 🔒 You can backup/export anytime
- 🔒 Delete anytime: `DROP DATABASE bhojpuri_billa;`

## Backup Your Translations

Save all your data:
```powershell
mysqldump -u root -pkali bhojpuri_billa > my_backup.sql
```

Restore later:
```powershell
mysql -u root -pkali bhojpuri_billa < my_backup.sql
```

---

**That's it!** Now every conversation you have with Billu the Cat is saved forever! 🐱💾
