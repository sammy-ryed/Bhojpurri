# Bhojpuri Billa - Gamified Language Learning Platform# 🐱 Bhojpurri - Billu the Cat



A desktop application featuring an animated cat mascot (Billu) that helps users learn and practice multiple languages through real-time speech recognition, translation, and text-to-speech playback.A Java Swing desktop application featuring an **animated cat mascot** that provides real-time **multi-language translation** (33+ languages) with speech input and output.



## Project Overview---



Bhojpuri Billa transforms language learning into an engaging, gamified experience by providing immediate audio feedback with visual animations. Users speak into their microphone, and Billu the Cat listens, translates, and speaks back in the target language with synchronized animations.## Features



## Key Features### **Animated Cat Mascot (Talking Tom Style!)**

- **400x400px** sprite-based animations

### Real-Time Speech Recognition- **4 Animation States**: Idle, Listening (ears covered!), Thinking, Speaking (mouth moves!)

- Powered by Groq Whisper Turbo (whisper-large-v3-turbo model)- **10 FPS** smooth animation using javax.swing.Timer

- 8x faster than standard Whisper models- Cat reacts to your actions in real-time!

- Transcribes audio to English in under 2 seconds

- High accuracy across various accents and dialects###  **Multi-Language Support (33+ Languages!)**

- Select output language from dropdown menu

### Multi-Language Support- **Indian Languages** (9): Bhojpuri, Hindi, Bengali, Tamil, Telugu, Marathi, Gujarati, Kannada, Punjabi

- Support for 33+ languages including:- **European Languages** (13): Spanish, French, German, Italian, Portuguese, Russian, Turkish, Dutch, Polish, Swedish, Norwegian, Danish, Finnish

  - Indian Regional Languages: Hindi, Bhojpuri, Urdu, Tamil, Telugu, Marathi, Bengali, Gujarati, Kannada, Malayalam, Punjabi- **Asian Languages** (7): Japanese, Korean, Chinese, Thai, Vietnamese, Indonesian, Malay

  - European Languages: Spanish, French, German, Italian, Portuguese, Russian, Dutch, Polish, Swedish- **Middle Eastern Languages** (3): Arabic, Greek, Hebrew

  - Asian Languages: Japanese, Chinese (Simplified & Traditional), Korean, Vietnamese, Thai, Indonesian- **South Asian**: Urdu

  - Middle Eastern Languages: Arabic, Turkish, Persian- Language-aware Text-to-Speech (speaks in correct language!)

  - And more: Swahili, Filipino, Hebrew, Ukrainian

### **Lightning-Fast Performance**

### Natural Text-to-Speech- **Whisper Turbo Model** (8x faster than standard!)

- Dual TTS Provider System:- **2-5 second** total processing time (down from 7-15 seconds!)

  - Primary: ElevenLabs TTS for premium natural-sounding voices- Optimized timeouts and streamlined API calls

  - Fallback: Google TTS (free, reliable backup)

- Adjustable playback speed optimized for language learners (70% speed)### 🎙️ **Voice Recording**

- Crystal-clear pronunciation for better learning retention- Hold SPACEBAR to record

- **44.1kHz CD-quality** audio

### Animated Mascot System- WAV format for best compatibility

- Billu the Cat with 46 hand-crafted animation frames

- 5 distinct animation states:### **Smart Translation**

  - IDLE: Waiting for input (10 frames)- **Groq Whisper API** for speech-to-text (FREE!)

  - LISTENING: Recording audio (8 frames, paw over ear)- **OpenL Translate API** for 33+ language translation

  - THINKING: Processing translation (10 frames, walking)- Auto-detects English speech

  - SPEAKING: Playing translated audio (8 frames, mouth moving)

  - ERROR: System error indicator (10 frames)### **Multi-Language Text-to-Speech**

- Smooth 10 FPS animation with pre-loaded sprite optimization- **Google TTS** with language mapping

- Speaks in selected language (Bhojpuri→Hindi, etc.)

### Translation History- Reliable free fallback

- MySQL database storage with UTF-8 MB4 encoding

- View complete translation history in table format### **MySQL Database Storage**

- Replay any previous translation with cat animation- Auto-saves all translations

- Sortable by date, language, or content- Stores: audio location, file size, English text, translated text, target language, timestamp

- One-click database reset functionality- UTF-8 support for all languages



### Audio Playback Controls### **Modern Split-Pane UI**

- Replay last translation for pronunciation practice- **Left Panel** (500px): Animated cat (400x400) + status

- Adjustable audio timing (150ms pre-delay, 1000ms post-delay)- **Right Panel**: Translation console + language dropdown

- Complete playback without cutting start/end syllables- **900x600** window with clean layout

- JLayer AdvancedPlayer with event tracking

---

## Technical Specifications

## Architecture

### Architecture

- Language: Java 11```

- Build Tool: Maven 3.6+┌─────────────────────────────────────────────────┐

- GUI Framework: Java Swing with JSplitPane layout│         Bhojpurri - Billu the Cat               │

- Window Size: 900x600 pixels (500px cat panel + console)├──────────────────┬──────────────────────────────┤

- Animation Engine: javax.swing.Timer at 10 FPS│                  │  📝 Translation Console       │

│                  │  🌍 Output Language: [🇮🇳 Bho]│

### APIs and Libraries│       🐱        │ ┌────────────────────────────┐│

- Groq Whisper API: Speech-to-text transcription│    (400x400)    │ │ English: Hello!            ││

- OpenL Translate API: Multi-language translation│  Animated Cat   │ │ Bhojpuri: नमस्ते!          ││

- ElevenLabs TTS API: Premium text-to-speech│                  │ │                            ││

- Google TTS API: Free fallback text-to-speech│    Status Text   │ │ (Translation output)       ││

- JLayer MP3 Library: Audio playback engine│                  │ └────────────────────────────┘│

- MySQL Connector/J: Database connectivity├──────────────────┴──────────────────────────────┤

│ Hold SPACE to record | Release to translate     │

### Database Schema└─────────────────────────────────────────────────┘

- Table: translations```

  - Columns: id, created_at, audio_file_path, audio_size_bytes, english_text, translated_text, language_code, tts_file_path

  - Encoding: UTF-8 MB4 (supports all scripts including Devanagari, Arabic, CJK)### Core Classes:

  - Auto-increment reset capability- **MainApp.java**: Application entry point

- **BilluUI.java**: UI with cat animations and language selector (408 lines)

### System Requirements- **CatAnimator.java**: Sprite-based animation engine (239 lines)

- Operating System: Windows 10/11 (with PowerShell)- **SpeechRecorder.java**: Microphone audio recording (44.1kHz WAV)

- Java: JDK 11 or higher- **Translator.java**: Groq Whisper + OpenL Translate integration (286 lines)

- Maven: 3.6 or higher- **TTSManager.java**: Multi-language TTS with language mapping (289 lines)

- MySQL: 8.0 or higher- **AudioPlayer.java**: Audio playback (MP3/WAV support)

- Microphone: Any standard USB or built-in microphone- **DatabaseManager.java**: MySQL storage with language tracking (302 lines)

- Internet: Required for API calls (STT, translation, TTS)

---

## Installation

## Prerequisites

### Prerequisites Setup

- **Java 11** or higher

1. **Install Java 11 JDK**- **Maven 3.6+**

   - Download from: https://adoptium.net/- **MySQL 8.0+** (for database storage)

   - Verify installation: `java -version`- **Microphone** and **speakers**

- **Internet connection** (for API calls)

2. **Install Maven**- **Windows OS** (PowerShell scripts included)

   - Download from: https://maven.apache.org/download.cgi

   - Add to PATH environment variable---

   - Verify installation: `mvn -version`

## Quick Start

3. **Install MySQL Server**

   - Download from: https://dev.mysql.com/downloads/mysql/### 1. **Clone Repository**

   - Create database: `bhojpuri_billa````powershell

   - Default credentials: root / password (or configure your own)cd "C:\Users\lenovo\Desktop\Bhojpuri Billa"

```

### API Keys Configuration

### 2. **Setup MySQL Database**

1. **Get Groq API Key** (Required - FREE)```powershell

   - Sign up at: https://console.groq.com/# Start MySQL

   - Navigate to API Keys sectionnet start MySQL80

   - Create new API key

   - Free tier: 14,400 requests/day# Create database and table (auto-created on first run)

# Database: bhojpuri_billa

2. **Get ElevenLabs API Key** (Optional - Premium)# Table: translations (with target_language column)

   - Sign up at: https://elevenlabs.io/```

   - Navigate to Profile > API Keys

   - Create new API key### 3. **Configure API Keys**

   - Free tier: 10,000 characters/month

   - Note: If unavailable, Google TTS will be used automatically**File**: `src/main/java/com/bhojpurri/Translator.java`

```java

3. **Create .env File**// Line 24: Add your Groq API key (FREE at https://console.groq.com/)

   - Copy `.env.example` to `.env` (if provided)private static final String GROQ_API_KEY = "gsk_your_actual_key_here";

   - Or create new `.env` in project root:```

   ```

   GROQ_API_KEY=your_groq_api_key_here**File**: `src/main/java/com/bhojpurri/TTSManager.java`

   ELEVENLABS_API_KEY=your_elevenlabs_api_key_here```java

   ```// Google TTS works automatically (no key needed!)

// ElevenLabs is optional (currently disabled)

### Build and Run```



1. **Clone Repository**### 4. **Build & Run**

   ```bash```powershell

   git clone <repository-url># Build

   cd "Bhojpuri Billa"mvn clean install

   ```

# Run

2. **Build Project**mvn exec:java

   ```bash

   mvn clean package# Or use launcher

   ```.\run.bat

```

3. **Run Application**

   - Windows: `.\run.bat`---

   - Linux/Mac: `./run.sh`

## How to Use

## Usage Guide

1. **Launch** - App opens with animated cat in Idle state

### Basic Workflow2. **Select Language** - Choose output language from dropdown (top-right)

3. **Press & Hold SPACE** - Cat covers ears! 🎧 Speak in English

1. **Launch Application**4. **Release SPACE** - Cat starts thinking (processing starts)

   - Run `.\run.bat` to start the application5. **Wait 2-5 seconds** - Cat speaks! Translation plays in selected language

   - Billu appears in IDLE state, ready to listen6. **Repeat** - Press SPACE again for another translation



2. **Record Audio**### Example Workflow:

   - Press and hold SPACEBAR to start recording```

   - Speak clearly into your microphoneYOU: "Hello, how are you?"

   - Billu's animation changes to LISTENING state  ↓ (Select Hindi from dropdown)

APP: "नमस्ते, आप कैसे हैं?"

3. **Stop Recording**  ↓ (TTS speaks in Hindi)

   - Release SPACEBAR to stop recording Plays Hindi audio

   - Billu transitions to THINKING state

   - Processing begins automaticallyYOU: "Good morning!"

  ↓ (Select Spanish from dropdown)  

4. **Receive Translation**APP: "¡Buenos días!"

   - Transcribed English text appears in console  ↓ (TTS speaks in Spanish)

   - Translated text appears in selected language Plays Spanish audio

   - Billu speaks the translation (SPEAKING state)```

   - Audio plays with cat's mouth animation

---

5. **Replay or Continue**

   - Click "Replay Last" button to hear translation again##  Supported Languages (33 Total!)

   - Click "History" to view all past translations

   - Record new audio to continue learning| Region | Languages (Flag - Code) |

|--------|-------------------------|

### UI Controls| **Indian** | 🇮🇳 Bhojpuri (bho), 🇮🇳 Hindi (hi), 🇮🇳 Bengali (bn), 🇮🇳 Tamil (ta), 🇮🇳 Telugu (te), 🇮🇳 Marathi (mr), 🇮🇳 Gujarati (gu), 🇮🇳 Kannada (kn), 🇮🇳 Punjabi (pa) |

| **European** | 🇪🇸 Spanish (es), 🇫🇷 French (fr), 🇩🇪 German (de), 🇮🇹 Italian (it), 🇵🇹 Portuguese (pt), 🇷🇺 Russian (ru), 🇹🇷 Turkish (tr), 🇳🇱 Dutch (nl), 🇵🇱 Polish (pl), 🇸🇪 Swedish (sv), 🇳🇴 Norwegian (no), 🇩🇰 Danish (da), 🇫🇮 Finnish (fi) |

- **Spacebar**: Hold to record, release to process| **Asian** | 🇯🇵 Japanese (ja), 🇰🇷 Korean (ko), 🇨🇳 Chinese (zh), 🇹🇭 Thai (th), 🇻🇳 Vietnamese (vi), 🇮🇩 Indonesian (id), 🇲🇾 Malay (ms) |

- **Language Dropdown**: Select target translation language| **Middle Eastern** | 🇸🇦 Arabic (ar), 🇬🇷 Greek (el), 🇮🇱 Hebrew (he) |

- **Replay Last Button**: Replay most recent translation| **South Asian** | 🇵🇰 Urdu (ur) |

- **History Button**: Open translation history dialog

- **Reset DB Button**: Clear all stored translations (with confirmation)**Note**: TTS uses intelligent language mapping (e.g., Bhojpuri→Hindi for better voice quality)



### Tips for Best Results---



- Speak clearly and at normal pace## Performance Optimizations

- Minimize background noise during recording

- Use short phrases (5-15 words) for best accuracy| Optimization | Before | After | Improvement |

- Replay translations multiple times for pronunciation practice|--------------|--------|-------|-------------|

- Try different languages to compare expressions| **Whisper Model** | whisper-large-v3 | **whisper-large-v3-turbo** | **8x faster!** |

| **Transcription Timeout** | 60s | **15s** |  4x faster |

## Project Structure| **Translation Timeout** | 30s | **10s** |  3x faster |

| **Connection Timeout** | 30s | **10s** |  3x faster |

```| **Total Time** | 7-15s | **2-5s** |  **3-5x overall!** |

Bhojpuri Billa/

├── src/**Result**: You'll notice the speed difference immediately! 

│   └── main/

│       └── java/---

│           └── com/

│               └── bhojpurri/##  Database Schema

│                   ├── MainApp.java          # Application entry point

│                   ├── BilluUI.java          # Main GUI with cat panel**Database**: `bhojpuri_billa` (utf8mb4)

│                   ├── CatAnimator.java      # Animation state machine

│                   ├── SpeechRecorder.java   # Audio recording handler**Table**: `translations`

│                   ├── Translator.java       # STT + translation logic```sql

│                   ├── TTSManager.java       # Text-to-speech generatorCREATE TABLE translations (

│                   ├── AudioPlayer.java      # MP3 playback engine    id INT PRIMARY KEY AUTO_INCREMENT,

│                   ├── DatabaseManager.java  # MySQL operations    audio_file VARCHAR(255),

│                   └── EnvLoader.java        # .env file parser    audio_size BIGINT,

├── cat/                                      # 46 PNG animation frames    english_text TEXT,

│   ├── Dead1.png - Dead10.png               # ERROR state (10 frames)    translated_text TEXT CHARACTER SET utf8mb4,

│   ├── Fall1.png - Fall8.png                # LISTENING state (8 frames)    target_language VARCHAR(10) DEFAULT 'bho',

│   ├── Idle1.png - Idle10.png               # IDLE state (10 frames)    tts_file VARCHAR(255),

│   ├── Jump1.png - Jump8.png                # SPEAKING state (8 frames)    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

│   └── Walk1.png - Walk10.png               # THINKING state (10 frames)    INDEX idx_language (target_language)

├── audio_recordings/                         # Recorded WAV files (timestamped));

├── tts_output/                               # Generated TTS MP3 files```

├── .env                                      # API keys (not in git)

├── .gitignore                                # Git exclusions**View Your Data**:

├── pom.xml                                   # Maven build configuration```sql

├── run.bat                                   # Windows launcherUSE bhojpuri_billa;

├── run.sh                                    # Linux/Mac launcherSELECT * FROM translations ORDER BY created_at DESC;

└── README.md                                 # This file```

```

---

## Educational Value

##  Troubleshooting

### For Students

- Removes public speaking anxiety through private practice###  SSL Handshake Error with Groq API

- Provides unlimited pronunciation repetition```

- Enables 24/7 language learning without human tutorjavax.net.ssl.SSLHandshakeException: Remote host terminated the handshake

- Gamifies learning through visual feedback and rewards```

- Supports self-paced progress tracking

**Fixes**:

### For Teachers1. **Turn Off VPN/Proxy** - Many VPNs block SSL handshakes

- Acts as homework supplement tool2. **Check Firewall** - Allow `java.exe` and `javaw.exe` through Windows Firewall

- Improves student pronunciation outside classroom3. **Test Connection**: `curl https://api.groq.com/openai/v1/models`

- Provides usage analytics through database logs4. **Try Different Network** - Switch to mobile hotspot

- Encourages consistent daily practice5. **Corporate/School Network?** - Ask IT to whitelist `api.groq.com`

- Reduces classroom time spent on basic pronunciation

###  Transcription Still Slow?

### For Communities- Check internet speed (run speed test)

- Preserves regional languages (Bhojpuri, Tamil, Telugu, etc.)- Disable VPN (adds latency)

- Creates digital linguistic heritage records- Ensure you're using Whisper Turbo model (`whisper-large-v3-turbo`)

- Makes language learning accessible to rural areas

- Bridges generational language gaps### 🔇 No Sound from Recording?

- Promotes cultural preservation through technology- Check microphone permissions

- Play WAV file manually: `start audio_recordings\recording_*.wav`

## Technical Challenges Solved- Ensure microphone is default device



### Audio Playback Issues### TTS Not Working?

**Problem**: First and last syllables being cut during playback  - Check console for error messages

**Solution**: Implemented 150ms pre-delay (sound system initialization) and 1000ms post-delay (buffer flush) with JLayer AdvancedPlayer event listeners- Verify internet connection

- Google TTS should work automatically (no API key needed)

### Multi-Script Display Support- Test MP3 playback: `start tts_output\fallback.mp3`

**Problem**: Urdu, Arabic, Hebrew, Devanagari not rendering in console  

**Solution**: UTF-8 MB4 encoding in MySQL, dynamic font detection, Unicode-aware text handling### 🗄️ Database Connection Failed?

```powershell

### Animation Performance# Start MySQL

**Problem**: 46 frames causing lag and stuttering  net start MySQL80

**Solution**: Pre-load all sprites at startup, use javax.swing.Timer for consistent 10 FPS, optimize image scaling

# Test connection

### Database Scalabilitymysql -u root -pkali

**Problem**: Proper encoding for all language scripts  ```

**Solution**: MySQL UTF-8 MB4 with proper collation, indexed queries, auto-increment reset capability

###  Cat Animations Not Showing?

### API Reliability- Check sprite assets in: `catndog assit/png/cat/`

**Problem**: Single TTS provider causing failures  - Ensure PNG sequences exist: Idle(10), Hurt(10), Walk(10), Jump(8)

**Solution**: Dual-provider architecture (ElevenLabs primary, Google TTS fallback) with automatic failover- Check console for animation errors



### HTTP Caching Issues---

**Problem**: Old translations being replayed from cache  

**Solution**: Cache-busting timestamps in URLs, forced file deletion with wait periods##  Project Structure



## Future Enhancements```

Bhojpuri Billa/

### Planned Features├── pom.xml                          # Maven configuration

- Achievement badges system (bronze/silver/gold levels)├── README.md                        # This file

- XP points and user leaderboards├── run.bat                          # Windows launcher

- Quiz mode with multiple-choice questions├── run.sh                           # Linux/Mac launcher

- Conversation mode (multi-turn dialogues)├── audio_recordings/                # Recorded WAV files

- Advanced AI context awareness (GPT integration)├── tts_output/                      # Generated TTS audio

- Social features (share progress with friends)├── catndog assit/

- Mobile application (Android/iOS versions)│   └── png/

- Offline mode (cached translations)│       └── cat/                     # Cat sprite animations

- Custom voice training (user's own voice)│           ├── Idle (10 frames)

- Language pairing recommendations│           ├── Hurt (10 frames)

│           ├── Walk (10 frames)

### Technical Roadmap│           └── Jump (8 frames)

- Migrate to Spring Boot for better scalability└── src/

- Add RESTful API for mobile clients    └── main/

- Implement Redis caching layer        └── java/

- Add WebSocket for real-time updates            └── com/

- Integrate analytics dashboard                └── bhojpurri/

- Support custom animation themes                    ├── MainApp.java         # Entry point

- Add audio waveform visualization                    ├── BilluUI.java         # UI + language selector

- Implement cloud database option (AWS RDS)                    ├── CatAnimator.java     # Animation engine

                    ├── SpeechRecorder.java  # Audio recording

## Troubleshooting                    ├── Translator.java      # STT + translation

                    ├── TTSManager.java      # Multi-language TTS

### Application won't start                    ├── AudioPlayer.java     # Audio playback

- Verify Java 11+ is installed: `java -version`                    └── DatabaseManager.java # MySQL storage

- Check if port 3306 (MySQL) is available

- Ensure .env file exists with valid API keys```



### No audio recording---

- Check microphone permissions in Windows Settings

- Verify default recording device is enabled## 🔧 Testing

- Try different USB port for external microphone

### Test Individual Components:

### Translation errors

- Verify Groq API key is valid and has quota remaining**Test 1: Database Connection**

- Check internet connection stability```powershell

- Ensure audio file was created (check audio_recordings folder)mvn clean compile

mvn exec:java -Dexec.mainClass="com.bhojpurri.TestDatabase"

### Database connection failed```

- Start MySQL service: `net start MySQL80`Expected: ` ALL DATABASE TESTS PASSED!`

- Verify database 'bhojpuri_billa' exists

- Check MySQL credentials in DatabaseManager.java**Test 2: Recording Quality**

```powershell

### Cat animation not visible# After recording, play the WAV file manually

- Ensure 'cat' folder exists with all 46 PNG filesstart audio_recordings\recording_*.wav

- Verify image files are not corrupted```

- Check console for image loading errorsExpected: You should hear your voice clearly (44.1kHz quality)



## Performance Metrics**Test 3: TTS Output**

```powershell

- Average Response Time: Under 7 seconds (record to translate to speak)# After TTS generation, play the audio

- Speech Recognition Accuracy: 95%+ for clear audiostart tts_output\fallback.mp3

- Animation Frame Rate: Smooth 10 FPS```

- Database Query Time: Under 50ms for history retrievalExpected: You should hear speech in selected language

- Audio Playback Latency: Under 200ms initialization

**Test 4: Network Connectivity**

## Contributing```powershell

# Test Groq API access

This is an educational project developed for academic purposes. Contributions, suggestions, and feedback are welcome.curl https://api.groq.com/openai/v1/models

```

## LicenseExpected: Returns list of available models



This project is developed for educational purposes. All third-party APIs (Groq, ElevenLabs, OpenL) have their own terms of service and usage limits.---



## Credits##  API Keys & Services



**Development Team**### Required APIs:

- Project conceptualization and implementation

- Cat animation design and sprite creation1. **Groq Whisper API** (Speech-to-Text)

- UI/UX design and gamification elements   - **Cost**: **FREE!**

- Database architecture and optimization   - **Signup**: https://console.groq.com/

   - **Model**: `whisper-large-v3-turbo`

**Third-Party Services**   - **Speed**: 8x faster than standard

- Groq Cloud: Whisper Turbo speech recognition   - **Accuracy**: Excellent (same as OpenAI Whisper)

- OpenL Translate: Multi-language translation API   - **Configuration**: Add key to `Translator.java` line 24

- ElevenLabs: Premium text-to-speech synthesis

- Google TTS: Fallback text-to-speech service2. **OpenL Translate API** (Translation)

   - **Cost**: **FREE!**

## Contact and Support   - **Languages**: 33+ supported

   - **Already configured** in code

For issues, questions, or feature requests, please refer to the project repository or contact the development team.

3. **Google TTS** (Text-to-Speech)

---   - **Cost**: **FREE!**

   - **No API key needed**

**Version**: 1.0.0     - **Automatic fallback**

**Last Updated**: November 2025     - **Language mapping** built-in

**Java Version**: 11+  

**Build Tool**: Maven 3.6+---


## Known Issues

### SSL Errors with Groq API
- **Issue**: Network/firewall blocking SSL handshake
- **Status**: Not a code issue - network-level blocking
- **Workaround**: Try different network, disable VPN, check firewall

### ElevenLabs TTS Disabled
- **Issue**: Free tier blocked due to abuse detection
- **Status**: Google TTS works as reliable fallback
- **Impact**: None - Google TTS provides excellent quality

### Bhojpuri TTS Voice
- **Note**: Uses Hindi voice (closest match available)
- **Reason**: Google TTS doesn't have native Bhojpuri voice
- **Quality**: Excellent Hindi pronunciation

---

## Console Output Examples

### Successful Translation:
```
 Recording saved at: C:\...\audio_recordings\recording_123.wav
   File size: 124044 bytes
   
Starting transcription with Groq Whisper...
Transcription: "Hello, how are you?"

Translating to Hindi (hi)...
Translation: "नमस्ते, आप कैसे हैं?"

Starting TTS (Language: hi)...
Google TTS successful!
   File: C:\...\tts_output\fallback.mp3
   
Saved translation to database (ID: 42, Language: hi)

Done! Press SPACE to continue.
```

### With Errors:
```
Groq API Error: SSL handshake failed
Transcription unavailable - check network

Trying translation anyway...
Translation: "नमस्कार"

TTS fallback to Google...
Audio played successfully
```

---

## Deployment

### Build JAR with Dependencies:
```powershell
mvn clean package
mvn dependency:copy-dependencies
```

### Run Standalone JAR:
```powershell
java -cp "target/bhojpurri-app-1.0.0.jar;target/dependency/*" com.bhojpurri.MainApp
```

### Distribution Package:
1. Copy `target/bhojpurri-app-1.0.0.jar`
2. Copy `target/dependency/*` folder
3. Copy `catndog assit/` folder (sprite assets)
4. Copy `run.bat` launcher
5. Include this README.md

---

## Future Enhancements

- [ ] Add more cat animation states
- [ ] Support for direct audio file translation
- [ ] Export translations to PDF/CSV
- [ ] Offline mode with cached translations
- [ ] Custom voice selection
- [ ] Real-time translation (no recording needed)
- [ ] Multi-user support with profiles
- [ ] Android/iOS mobile version

---

## License

This project is for educational purposes. Please respect the terms of service of all APIs used.

---

## Credits

- **Groq** - For free Whisper API access
- **OpenL Translate** - For multi-language translation
- **Google TTS** - For reliable text-to-speech
- **Cat Sprites** - From `catndog assit` assets

---

## Support

If you encounter issues:
1. Check the **Troubleshooting** section above
2. Verify all **Prerequisites** are installed
3. Ensure **API keys** are correctly configured
4. Check **console output** for detailed error messages
5. Test **individual components** using the testing section

---

## Enjoy Your Multi-Language Translation App!

**Press SPACE and start talking! The cat is listening!** 

│                   ├── SpeechRecorder.java  # Audio recording
│                   ├── Translator.java      # Translation API
│                   ├── TTSManager.java      # Text-to-speech
│                   └── AudioPlayer.java     # Audio playback
├── audio_recordings/                # Generated audio recordings
└── tts_output/                     # Generated TTS audio files
