# 🚀 Bhojpurri App - Quick Start Guide

## Running the Application

```powershell
cd "C:\Users\lenovo\Desktop\Bhojpuri Billa"
mvn exec:java
```

## How to Use

1. **Click on the app window** to give it focus
2. **Press and HOLD SPACEBAR** to record your voice
3. **Release SPACEBAR** to process and translate
4. **Listen** to the Bhojpuri translation in Hindi speech
5. **Repeat** - Press SPACE again for another translation

## What You'll See

```
😺 Idle → Press SPACE
🎙️ 😸 Listening → Speak now!
🧠 🤔 Processing → Translating...
🔊 😻 Speaking → Listen to translation
✅ Done → Press SPACE again
```

## Testing Individual Components

### Test 1: Check Recording Quality
```powershell
# After recording, manually play the WAV file
start audio_recordings\recording_[timestamp].wav
```
**Expected:** You should hear your voice clearly

### Test 2: Test Speechify API
```powershell
java -cp "target/classes;$env:USERPROFILE\.m2\repository\*" com.bhojpurri.TestSpeechify
```
**Expected:** Shows which endpoints work

### Test 3: Check TTS Output
```powershell
# After TTS generation, play the MP3
start tts_output\output.mp3       # Speechify output
start tts_output\fallback.mp3     # Google TTS output
```
**Expected:** You should hear Hindi speech

## Debugging Console Output

Watch for these messages:

✅ **Success Messages:**
```
🎤 Recording saved at: ...
✅ Speechify TTS successful!
✅ Google TTS fallback successful!
```

⚠️ **Warning Messages:**
```
⚠️ Speechify TTS failed: [reason]
🔄 Falling back to Google TTS...
```

❌ **Error Messages:**
```
❌ All TTS methods failed: [reason]
```

## File Locations

- **Recordings:** `audio_recordings/recording_*.wav`
- **TTS Output:** `tts_output/output.mp3` or `fallback.mp3`
- **Logs:** Console output

## Troubleshooting

### No Sound from Recording?
- Check microphone permissions
- Try playing the WAV file manually
- Ensure microphone is selected as default device

### TTS Not Working?
- Check console for error messages
- Verify internet connection
- Try manual MP3 playback

### App Won't Start?
```powershell
# Clean rebuild
mvn clean install
mvn exec:java
```

## API Keys (Already Configured)

- **OpenL Translate:** ✅ Active
- **Speechify TTS:** ✅ Active (with corrected format)
- **Google TTS:** ✅ No key needed (free fallback)

## Next Steps

1. ✅ Test the full workflow (record → translate → speak)
2. ✅ Verify recording quality with manual playback
3. ✅ Check TTS output with manual playback
4. 📝 For production: Integrate real Speech-to-Text API

---

**Made with ❤️ for Bhojpuri language preservation**
