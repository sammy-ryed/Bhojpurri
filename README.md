# Bhojpurri Billa

A Java Swing desktop application for English to Bhojpuri translation with speech recognition and text-to-speech capabilities, featuring Billu the Cat as an animated mascot.

## Overview

Bhojpurri Billa is a desktop translation assistant that records English speech, transcribes it using AI-powered speech recognition, translates it to Bhojpuri or other languages, and plays back the translation using text-to-speech. The application features an animated cat mascot (Billu) that provides visual feedback during operation.

## Features

- **Speech Recognition**: Records English audio and transcribes it using Groq's Whisper API (whisper-large-v3-turbo model)
- **Multi-language Translation**: Translates English text to Bhojpuri and multiple other languages using OpenL Translate API
- **Text-to-Speech**: Converts translated text to speech using Google TTS API
- **Animated Mascot**: Interactive cat animation that responds to application state
- **Translation History**: Stores all translations in a MySQL database with timestamp tracking
- **Audio Playback**: Built-in audio player with replay functionality for TTS output
- **Database Management**: Reset and manage translation history through the UI

## Supported Languages

- Bhojpuri (Primary)
- Hindi
- Urdu
- Arabic
- Bengali
- Gujarati
- Marathi
- Tamil
- Telugu
- And more...

## Technology Stack

- **Java**: 11 or higher
- **Build Tool**: Maven 3.x
- **Framework**: Java Swing (GUI)
- **Database**: MySQL 8.x
- **APIs**:
  - Groq Whisper API (Speech-to-Text)
  - OpenL Translate API (Translation)
  - Google TTS API (Text-to-Speech)
- **Audio Libraries**: 
  - mp3spi (MP3 playback support)
  - jlayer (Audio codec)
  - tritonus-share (Audio utilities)

## Dependencies

Key Maven dependencies:

- `org.json:json` - JSON processing
- `com.googlecode.soundlibs:mp3spi` - MP3 audio support
- `com.mysql:mysql-connector-j` - MySQL database driver
- `org.slf4j:slf4j-api` - Logging framework

## Project Structure

```
bhojpuri-billa/
├── src/main/java/com/bhojpurri/
│   ├── MainApp.java           # Application entry point
│   ├── BilluUI.java            # Main UI and event handling
│   ├── Translator.java         # Speech transcription and translation
│   ├── TTSManager.java         # Text-to-speech management
│   ├── SpeechRecorder.java     # Audio recording functionality
│   ├── AudioPlayer.java        # Audio playback handler
│   ├── CatAnimator.java        # Animated mascot controller
│   ├── DatabaseManager.java    # MySQL database operations
│   └── EnvLoader.java          # Environment variable loader
├── catndog assit/png/          # Cat animation sprites
│   ├── cat/                    # Cat sprite images
│   └── dog/                    # Dog sprite images (unused)
├── audio_recordings/           # Recorded audio files storage
├── tts_output/                 # Generated TTS audio files
├── pom.xml                     # Maven configuration
├── run.bat                     # Windows launch script
└── run.sh                      # Linux/Mac launch script
```

## Prerequisites

### Required Software

1. **Java Development Kit (JDK)**: Version 11 or higher
   - Download from: https://adoptium.net/ or https://www.oracle.com/java/

2. **Apache Maven**: Version 3.x
   - Download from: https://maven.apache.org/download.cgi

3. **MySQL Server**: Version 8.x or higher
   - Download from: https://dev.mysql.com/downloads/mysql/

### Database Setup

1. Install and start MySQL server
2. Create a database for the application:
   ```sql
   CREATE DATABASE bhojpurri_db;
   ```
3. The application will automatically create the required tables on first run

### Environment Variables

Create a `.env` file in the project root directory with the following variables:

```env
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/bhojpurri_db
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password

# API Keys
GROQ_API_KEY=your_groq_api_key
```

**Note**: If `.env` file is not provided, the application will use default configuration values.

#### API Key Setup

1. **Groq API Key** (Required for speech transcription):
   - Sign up at: https://console.groq.com/
   - Generate an API key
   - Add it to your `.env` file or the application will use the hardcoded fallback

2. **OpenL Translate API** (Pre-configured):
   - The application includes a pre-configured API key
   - For production use, obtain your own key from RapidAPI

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/sammy-ryed/Bhojpurri.git
cd Bhojpurri
```

### Step 2: Configure Database

Update the `.env` file with your MySQL credentials (or use default configuration).

### Step 3: Build the Project

```bash
mvn clean package
```

This will:
- Compile the source code
- Run tests
- Package the application into an executable JAR file
- Create a shaded JAR with all dependencies in `target/` directory

## Running the Application

### Windows

Double-click `run.bat` or execute from command prompt:

```cmd
run.bat
```

### Linux/Mac

Make the script executable and run:

```bash
chmod +x run.sh
./run.sh
```

### Manual Execution

Run with Maven:

```bash
mvn clean compile exec:java
```

Or run the JAR directly:

```bash
java -jar target/bhojpurri-app-1.0.0-shaded.jar
```

## Usage

### Recording and Translation

1. **Select Target Language**: Choose your desired translation language from the dropdown menu
2. **Record Audio**: Click the "Start Recording" button and speak in English
3. **Stop Recording**: Click "Stop Recording" when finished
4. **Automatic Processing**: The application will:
   - Transcribe your speech to English text
   - Translate the text to the selected language
   - Generate and play audio of the translation
   - Save the translation to the database

### Additional Features

- **Replay Audio**: Click the "Replay" button to hear the last translation again
- **View History**: Click "Show History" to view all past translations
- **Reset Database**: Click "Reset DB" to clear all translation history
- **Mascot Animation**: Watch Billu the Cat animate during different application states

## Configuration

### Audio Settings

The application uses the system's default microphone. Audio is recorded in:
- Format: WAV
- Sample Rate: 16kHz
- Channels: Mono
- Bit Depth: 16-bit

### Database Schema

The application creates a `translations` table with the following structure:

```sql
CREATE TABLE translations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    source_text TEXT,
    translated_text TEXT,
    source_language VARCHAR(50),
    target_language VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Troubleshooting

### Common Issues

**1. Database Connection Failed**
- Ensure MySQL server is running
- Verify database credentials in `.env` file
- Check if the database exists

**2. Audio Not Recording**
- Grant microphone permissions to Java
- Check if default microphone is properly configured
- Ensure audio drivers are up to date

**3. SSL Handshake Errors**
- The application includes SSL certificate relaxation for API calls
- If issues persist, check firewall settings

**4. Maven Build Fails**
- Ensure Java 11 or higher is installed
- Clear Maven cache: `mvn clean`
- Delete `.m2/repository` folder and rebuild

**5. UTF-8 Encoding Issues**
- The application sets UTF-8 encoding automatically
- For console output, ensure terminal supports UTF-8

## Development

### Building from Source

```bash
# Compile only
mvn clean compile

# Run tests
mvn test

# Package without running tests
mvn package -DskipTests

# Create executable JAR with dependencies
mvn clean package
```

### Project Configuration

- **Main Class**: `com.bhojpurri.MainApp`
- **Java Version**: 11
- **Maven Compiler**: 3.11.0
- **Output JAR**: `target/bhojpurri-app-1.0.0-shaded.jar`

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is provided as-is for educational and personal use.

## Credits

- **API Providers**:
  - Groq (Whisper Speech Recognition)
  - OpenL Translate (Translation Service)
  - Google TTS (Text-to-Speech)
- **Audio Libraries**: mp3spi, jlayer, tritonus-share
- **Logging**: SLF4J

## Support

For issues, questions, or feature requests, please open an issue on the GitHub repository.

## Changelog

### Version 1.0.0
- Initial release
- Speech-to-text transcription using Groq Whisper API
- Multi-language translation support
- Text-to-speech output
- Animated cat mascot
- MySQL database integration
- Translation history management
- Audio playback and replay functionality

## Roadmap

Future enhancements may include:
- Offline translation support
- Additional language support
- Voice customization options
- Export translation history
- Mobile application version
- Improved UI themes
- Real-time translation mode

---

Built with care for the Bhojpuri-speaking community and language enthusiasts worldwide.
