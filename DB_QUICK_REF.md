# 🎯 MySQL Integration - Quick Reference

## Setup (One Time)
```powershell
.\setup-database.bat
```

## MySQL Credentials
```
User: root
Pass: kali
DB:   bhojpuri_billa
Port: 3306
```

## Start MySQL
```powershell
net start MySQL80
```

## Run App
```powershell
.\run-jar.bat
```

## View Data
```sql
mysql -u root -pkali
USE bhojpuri_billa;
SELECT * FROM translations ORDER BY created_at DESC LIMIT 10;
```

## Common Queries
```sql
-- Count translations
SELECT COUNT(*) FROM translations;

-- Search
SELECT * FROM translations WHERE english_text LIKE '%hello%';

-- Today's stats
SELECT * FROM usage_stats WHERE date = CURDATE();

-- Total audio
SELECT SUM(audio_file_size)/1024/1024 AS mb FROM translations;
```

## What Gets Saved
✅ Audio file path & size
✅ English transcription
✅ Bhojpuri translation (Devanagari)
✅ TTS file path
✅ Timestamp

## Files
- `DatabaseManager.java` - Database operations
- `TestDatabase.java` - Test program
- `setup-database.bat` - Setup helper
- `DATABASE_README.md` - Full docs
- `QUICK_START_DB.md` - User guide

## Troubleshooting
**MySQL not running?**
```powershell
net start MySQL80
```

**Wrong password?**
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'kali';
```

**Test connection?**
```powershell
mvn exec:java -Dexec.mainClass="com.bhojpurri.TestDatabase"
```

## That's It!
Use the app normally - everything saves automatically! 💾
