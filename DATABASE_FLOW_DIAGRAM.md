# 🗄️ Database Integration Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    BHOJPURI BILLA - DATA FLOW                          │
└─────────────────────────────────────────────────────────────────────────┘

                           👤 USER SPEAKS
                                 ↓
                        ┌────────────────┐
                        │   SPACE KEY    │ ← Press and hold
                        │   PRESSED      │
                        └────────┬───────┘
                                 ↓
                    ┌────────────────────────┐
                    │   SpeechRecorder.java  │
                    │   Records microphone   │
                    │   Saves to WAV file    │
                    └────────┬───────────────┘
                             ↓
            ┌────────────────────────────────────┐
            │  audio_recordings/recording_xxx.wav │ ← 335KB WAV file
            └────────┬───────────────────────────┘
                     ↓
            ┌────────────────────┐
            │  Translator.java   │
            │  Groq Whisper API  │
            │  (Speech-to-Text)  │
            └────────┬───────────┘
                     ↓
            "Hello, how are you?" ← English transcription
                     ↓
            ┌────────────────────┐
            │  Translator.java   │
            │  OpenL API         │
            │  (Translation)     │
            └────────┬───────────┘
                     ↓
            "नमस्कार, कैसे बानी?" ← Bhojpuri translation (Devanagari)
                     ↓
            ┌────────────────────┐
            │  TTSManager.java   │
            │  Google TTS API    │
            │  (Text-to-Speech)  │
            └────────┬───────────┘
                     ↓
            ┌────────────────────────────┐
            │  tts_output/tts_output.mp3 │ ← 43KB MP3 file
            └────────┬───────────────────┘
                     ↓
            ┌────────────────────┐
            │  AudioPlayer.java  │
            │  Plays audio       │
            └────────┬───────────┘
                     ↓
            👂 USER HEARS BHOJPURI
                     ↓
            ┌─────────────────────────────────────────────────────┐
            │            🆕 DatabaseManager.java                  │
            │         SAVES EVERYTHING TO MySQL                   │
            └─────────────────────────────────────────────────────┘
                     ↓
    ┌────────────────────────────────────────────────────────────┐
    │                     MySQL Database                         │
    │                   bhojpuri_billa                          │
    ├────────────────────────────────────────────────────────────┤
    │                                                            │
    │  📋 Table: translations                                    │
    │  ┌──────────────────────────────────────────────────────┐ │
    │  │ id: 123                                              │ │
    │  │ audio_file_path: "C:\...\recording_xxx.wav"         │ │
    │  │ audio_file_size: 335916 bytes                       │ │
    │  │ english_text: "Hello, how are you?"                 │ │
    │  │ bhojpuri_text: "नमस्कार, कैसे बानी?"                │ │
    │  │ tts_file_path: "C:\...\tts_output.mp3"             │ │
    │  │ created_at: 2025-10-17 10:50:00                     │ │
    │  └──────────────────────────────────────────────────────┘ │
    │                                                            │
    │  📊 Table: usage_stats                                     │
    │  ┌──────────────────────────────────────────────────────┐ │
    │  │ date: 2025-10-17                                     │ │
    │  │ total_recordings: 15                                 │ │
    │  │ total_translations: 15                               │ │
    │  │ total_audio_size: 5242880 bytes (5 MB)              │ │
    │  └──────────────────────────────────────────────────────┘ │
    │                                                            │
    └────────────────────────────────────────────────────────────┘
                     ↓
            💾 DATA SAVED FOREVER
         (until you delete it manually)


═══════════════════════════════════════════════════════════════════════

                        🔍 HOW TO VIEW YOUR DATA

Option 1: MySQL Command Line
────────────────────────────────────────────────────────────────────────
mysql -u root -pkali
USE bhojpuri_billa;
SELECT * FROM translations ORDER BY created_at DESC LIMIT 10;


Option 2: MySQL Workbench (GUI)
────────────────────────────────────────────────────────────────────────
1. Open MySQL Workbench
2. Connect to localhost:3306
3. Username: root, Password: kali
4. Browse database: bhojpuri_billa
5. Open tables and view data


Option 3: In the App (Automatic)
────────────────────────────────────────────────────────────────────────
When you use the app, you'll see:

    English: Hello, how are you?
    Bhojpuri: नमस्कार, कैसे बानी?
    💾 Saved to database (ID: 123)  ← NEW!
    ---


═══════════════════════════════════════════════════════════════════════

                    📁 FILE LOCATIONS

Audio Recordings:
    C:\Users\lenovo\Desktop\Bhojpuri Billa\audio_recordings\
    └── recording_1760638069701.wav (335KB)
    └── recording_1760638070123.wav (412KB)
    └── ...

TTS Output:
    C:\Users\lenovo\Desktop\Bhojpuri Billa\tts_output\
    └── tts_output.mp3 (43KB) ← Overwritten each time

Database:
    MySQL Server (localhost:3306)
    └── bhojpuri_billa
        ├── translations (all your conversations)
        └── usage_stats (daily statistics)


═══════════════════════════════════════════════════════════════════════

                    🔧 TECHNICAL DETAILS

Database Connection:
────────────────────────────────────────────────────────────────────────
URL:      jdbc:mysql://localhost:3306/bhojpuri_billa
User:     root
Password: kali
Charset:  utf8mb4_unicode_ci (supports Devanagari: नमस्कार)
Driver:   com.mysql.cj.jdbc.Driver (MySQL Connector/J 8.2.0)


Tables Schema:
────────────────────────────────────────────────────────────────────────
translations:
  - id (INT, AUTO_INCREMENT, PRIMARY KEY)
  - audio_file_path (VARCHAR(500))
  - audio_file_size (BIGINT)
  - english_text (TEXT, utf8mb4)
  - bhojpuri_text (TEXT, utf8mb4)
  - tts_file_path (VARCHAR(500))
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
  - Indexes: created_at, audio_file_path

usage_stats:
  - id (INT, AUTO_INCREMENT, PRIMARY KEY)
  - date (DATE, UNIQUE)
  - total_recordings (INT)
  - total_translations (INT)
  - total_audio_size (BIGINT)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)


═══════════════════════════════════════════════════════════════════════

                    📊 EXAMPLE QUERIES

Count total translations:
────────────────────────────────────────────────────────────────────────
SELECT COUNT(*) FROM translations;


Find specific phrase:
────────────────────────────────────────────────────────────────────────
SELECT english_text, bhojpuri_text 
FROM translations 
WHERE english_text LIKE '%hello%';


Today's activity:
────────────────────────────────────────────────────────────────────────
SELECT total_recordings, total_translations, 
       total_audio_size / 1024 / 1024 AS total_mb
FROM usage_stats 
WHERE date = CURDATE();


Last 10 translations:
────────────────────────────────────────────────────────────────────────
SELECT id, english_text, bhojpuri_text, created_at
FROM translations 
ORDER BY created_at DESC 
LIMIT 10;


Total audio stored:
────────────────────────────────────────────────────────────────────────
SELECT SUM(audio_file_size) / 1024 / 1024 AS total_mb
FROM translations;


═══════════════════════════════════════════════════════════════════════

                    🎯 QUICK START

Step 1: Start MySQL
────────────────────────────────────────────────────────────────────────
net start MySQL80


Step 2: Test Database (Optional)
────────────────────────────────────────────────────────────────────────
mvn clean compile
mvn exec:java -Dexec.mainClass="com.bhojpurri.TestDatabase"


Step 3: Run App
────────────────────────────────────────────────────────────────────────
.\run-jar.bat


Step 4: Use It!
────────────────────────────────────────────────────────────────────────
Press SPACE → Speak → Release → Done!
Everything is automatically saved to MySQL!


═══════════════════════════════════════════════════════════════════════

                    ✅ BENEFITS

✓ Track your progress
✓ Review past conversations
✓ Search translations
✓ See usage statistics
✓ Export/backup your data
✓ All stored locally
✓ Full Devanagari support (नमस्कार)


═══════════════════════════════════════════════════════════════════════
```
