# 🗄️ MySQL Database Integration

## Overview
Bhojpuri Billa now saves all your voice recordings, transcriptions, and translations to a MySQL database!

## Database Details
- **Database Name**: `bhojpuri_billa`
- **MySQL User**: `root`
- **Password**: `kali`
- **Port**: `3306` (default)
- **Charset**: `utf8mb4` (supports Devanagari/Hindi/Bhojpuri text)

## Tables Created

### 1. `translations` Table
Stores all translation records:

| Column | Type | Description |
|--------|------|-------------|
| `id` | INT (PK) | Auto-increment primary key |
| `audio_file_path` | VARCHAR(500) | Path to recorded audio file |
| `audio_file_size` | BIGINT | Size of audio file in bytes |
| `english_text` | TEXT | Transcribed English text |
| `bhojpuri_text` | TEXT | Translated Bhojpuri text (Devanagari) |
| `tts_file_path` | VARCHAR(500) | Path to generated TTS audio |
| `created_at` | TIMESTAMP | When record was created |
| `updated_at` | TIMESTAMP | Last update time |

### 2. `usage_stats` Table
Tracks daily usage statistics:

| Column | Type | Description |
|--------|------|-------------|
| `id` | INT (PK) | Auto-increment primary key |
| `date` | DATE | Date (unique) |
| `total_recordings` | INT | Number of recordings that day |
| `total_translations` | INT | Number of translations that day |
| `total_audio_size` | BIGINT | Total audio data size in bytes |
| `created_at` | TIMESTAMP | When record was created |
| `updated_at` | TIMESTAMP | Last update time |

## Setup Instructions

### 1. Start MySQL Server

**Windows:**
```powershell
net start MySQL80
```

**Linux/Mac:**
```bash
sudo systemctl start mysql
# or
sudo service mysql start
```

### 2. Verify MySQL Credentials

Make sure your MySQL root password is set to `kali`:

```sql
-- Login to MySQL
mysql -u root -p

-- Update password if needed
ALTER USER 'root'@'localhost' IDENTIFIED BY 'kali';
FLUSH PRIVILEGES;
```

### 3. Test Database Connection

Run the test program:

```powershell
# Compile
mvn clean compile

# Run test
mvn exec:java -Dexec.mainClass="com.bhojpurri.TestDatabase"
```

You should see:
```
✅ ALL DATABASE TESTS PASSED!
```

### 4. Run the Main App

```powershell
.\run-jar.bat
```

Now every time you record and translate, it will automatically save to the database!

## Database Features

### Automatic Saving
Every successful translation is automatically saved with:
- Audio file path and size
- English transcription
- Bhojpuri translation (in Devanagari script)
- TTS output file path
- Timestamp

### View in UI
When you use the app, you'll see:
```
English: Hello, how are you?
Bhojpuri: नमस्कार, कैसे बानी?
💾 Saved to database (ID: 123)
---
```

### Query Your Data

You can query your data using MySQL:

```sql
USE bhojpuri_billa;

-- View all translations
SELECT * FROM translations ORDER BY created_at DESC LIMIT 10;

-- View today's stats
SELECT * FROM usage_stats WHERE date = CURDATE();

-- Search translations
SELECT english_text, bhojpuri_text FROM translations 
WHERE english_text LIKE '%hello%' OR bhojpuri_text LIKE '%नमस्कार%';

-- Count total translations
SELECT COUNT(*) as total FROM translations;

-- Get total audio data stored
SELECT SUM(audio_file_size) / 1024 / 1024 as total_mb FROM translations;
```

## DatabaseManager API

The `DatabaseManager` class provides these methods:

```java
DatabaseManager db = new DatabaseManager();

// Save a translation
int id = db.saveTranslation(audioPath, audioSize, englishText, bhojpuriText, ttsPath);

// Get total translations
int total = db.getTotalTranslations();

// Print recent translations
db.printRecentTranslations(10);

// Print today's stats
db.printTodayStats();

// Search translations
db.searchTranslations("hello");

// Close connection
db.close();
```

## Troubleshooting

### Error: "Communications link failure"
**Problem**: MySQL server is not running

**Solution**:
```powershell
net start MySQL80
```

### Error: "Access denied for user 'root'@'localhost'"
**Problem**: Wrong password

**Solution**: Update password in MySQL:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'kali';
```

Or change password in `DatabaseManager.java`:
```java
private static final String DB_PASSWORD = "your_password";
```

### Error: "Unknown database 'bhojpuri_billa'"
**Problem**: Database not created yet

**Solution**: The app will automatically create it on first run!

### Error: "The server time zone value is unrecognized"
**Problem**: MySQL timezone not set

**Solution**: Update connection URL in `DatabaseManager.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
```

## Character Encoding

The database uses `utf8mb4` encoding to properly store:
- ✅ Hindi/Devanagari script (नमस्कार)
- ✅ Bhojpuri text
- ✅ Emojis and special characters

## Security Notes

⚠️ **Important**: This configuration uses hardcoded credentials (`root/kali`) for simplicity. 

For production use, you should:
1. Create a dedicated MySQL user (not root)
2. Use environment variables for credentials
3. Grant only necessary permissions
4. Enable SSL/TLS for connections

```sql
-- Create dedicated user
CREATE USER 'bhojpuri_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON bhojpuri_billa.* TO 'bhojpuri_user'@'localhost';
FLUSH PRIVILEGES;
```

## Data Privacy

All data is stored **locally** on your computer in MySQL. Nothing is sent to external servers (except API calls for transcription/translation/TTS).

## Backup Your Data

To backup your translations:

```powershell
mysqldump -u root -pkali bhojpuri_billa > bhojpuri_backup.sql
```

To restore:
```powershell
mysql -u root -pkali bhojpuri_billa < bhojpuri_backup.sql
```

## What Gets Saved

Every time you press SPACE and speak:
1. ✅ Your audio is recorded → saved to `audio_recordings/`
2. ✅ Groq transcribes it → saved to database
3. ✅ OpenL translates it → saved to database (in Devanagari)
4. ✅ Google TTS generates audio → saved to `tts_output/`
5. ✅ All paths and text saved to MySQL database

You can later review all your conversations and practice sessions! 🎓
