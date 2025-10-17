# 🐱 Bhojpurri - Billu the Cat

A Java Swing desktop application that provides real-time English to Bhojpuri translation with speech input and output.

## Features

- 🎙️ **Voice Recording**: Hold spacebar to record your English speech
- 🧠 **Smart Translation**: Automatic transcription and translation to Bhojpuri
- 🔊 **Text-to-Speech**: Hear the Bhojpuri translation in Hindi voice
- 😺 **Animated Mascot**: Billu the cat shows different states (listening, thinking, speaking)
- 🎨 **Modern UI**: Clean and intuitive Java Swing interface

## Architecture

The application is organized into the following classes:

- **MainApp.java**: Application entry point and initialization
- **BilluUI.java**: User interface and animation management
- **SpeechRecorder.java**: Microphone audio recording (WAV format)
- **Translator.java**: OpenL Translate API integration for transcription and translation
- **TTSManager.java**: Speechify TTS API integration for speech synthesis
- **AudioPlayer.java**: Audio playback for TTS output (supports MP3 and WAV)

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Microphone and speakers
- Internet connection (for API calls)

## API Configuration

The application uses two APIs via RapidAPI:

1. **OpenL Translate API**: For English to Bhojpuri translation
2. **Speechify TTS API**: For text-to-speech conversion

API keys are configured in:
- `Translator.java`: OpenL API key
- `TTSManager.java`: Speechify API key

⚠️ **Important**: Replace the API keys with your own valid keys before running.

## Installation

1. Clone or download this repository
2. Navigate to the project directory:
   ```bash
   cd "Bhojpuri Billa"
   ```

3. Build the project with Maven:
   ```bash
   mvn clean install
   ```

## Running the Application

### Using Maven (Recommended):
```bash
mvn exec:java
```

### Using JAR with dependencies:
```bash
mvn clean package
mvn dependency:copy-dependencies
java -cp "target/bhojpurri-app-1.0.0.jar;target/dependency/*" com.bhojpurri.MainApp
```

### Using Windows Launcher:
```bash
.\run.bat
```

## Usage

1. **Launch the application** - The main window will appear with Billu the cat mascot
2. **Press and hold SPACEBAR** - Start speaking in English
3. **Release SPACEBAR** - Stop recording and begin processing
4. **Wait for translation** - The app will:
   - Transcribe your English speech
   - Translate it to Bhojpuri
   - Convert Bhojpuri text to Hindi speech
   - Play the audio through your speakers
5. **Repeat** - Press spacebar again to record another phrase

## Project Structure

```
Bhojpuri Billa/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── bhojpurri/
│                   ├── MainApp.java         # Entry point
│                   ├── BilluUI.java         # UI and event handling
│                   ├── SpeechRecorder.java  # Audio recording
│                   ├── Translator.java      # Translation API
│                   ├── TTSManager.java      # Text-to-speech
│                   └── AudioPlayer.java     # Audio playback
├── audio_recordings/                # Generated audio recordings
└── tts_output/                     # Generated TTS audio files
```

## Dependencies

- **org.json**: JSON processing for API requests/responses
- **mp3spi**: MP3 audio format support
- **slf4j**: Logging framework
- **Java Sound API**: Audio recording and playback

## Error Handling

The application includes comprehensive error handling for:
- Microphone access issues
- Network connectivity problems
- API failures
- Audio playback errors
- File I/O errors

All errors are logged and displayed in the UI with user-friendly messages.

## Known Limitations

1. **Speech-to-Text**: Currently uses a placeholder implementation. For production use, integrate with:
   - Google Cloud Speech-to-Text
   - OpenAI Whisper API
   - Azure Speech Services

2. **API Rate Limits**: Free tier APIs may have usage limits

3. **Audio Format**: Recording is in WAV format; conversion to other formats may be needed for some STT services

## Troubleshooting

### No microphone detected
- Ensure a microphone is connected and enabled in system settings
- Grant microphone permissions to Java

### API errors
- Verify API keys are valid and active
- Check internet connection
- Ensure RapidAPI subscription is active

### Audio playback issues
- Check speaker/audio output settings
- Ensure mp3spi dependency is included
- Verify audio drivers are up to date

## Future Enhancements

- [ ] Integrate real Speech-to-Text API
- [ ] Add support for multiple languages
- [ ] Offline translation mode
- [ ] Custom voice selection
- [ ] Audio quality settings
- [ ] History of translations
- [ ] Export translations to file

## License

This project is for educational purposes. Please ensure you comply with the terms of service for all APIs used.

## Credits

- **OpenL Translate API**: Translation services
- **Speechify TTS API**: Text-to-speech synthesis
- **Java Sound API**: Audio processing

---

Made with ❤️ for Bhojpuri language preservation
