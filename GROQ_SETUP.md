# 🚀 Groq Whisper API Setup - FREE Speech-to-Text!

## ✅ Status: Code is Ready - Just Need Your API Key!

---

## Step 1: Get Your FREE Groq API Key

1. **Go to**: https://console.groq.com/
2. **Sign up** (it's FREE!)
3. **Click** on "API Keys" in the left menu
4. **Create** a new API key
5. **Copy** the API key (looks like: `gsk_xxxxxxxxxxxxxxxxxxxxx`)

---

## Step 2: Add API Key to Your Code

**File**: `src/main/java/com/bhojpurri/Translator.java`

**Find this line** (around line 24):
```java
private static final String GROQ_API_KEY = "YOUR_GROQ_API_KEY_HERE";
```

**Replace with** your actual key:
```java
private static final String GROQ_API_KEY = "gsk_your_actual_key_here";
```

---

## Step 3: Rebuild and Run

```powershell
# Rebuild
mvn clean install

# Run (with MP3 codec)
mvn exec:java "-Dexec.classpathScope=runtime"
```

---

## 🎯 What Will Happen

```
1. Press SPACE → ✅ Recording starts
2. Speak clearly → ✅ Voice captured  
3. Release SPACE → ✅ File saved
4. Groq Whisper → ✅ YOUR VOICE TRANSCRIBED!
5. OpenL Translate → ✅ Translated to Bhojpuri
6. Speechify TTS → ⚠️ May fail (404 errors)
```

---

## 📊 Groq Whisper Features

| Feature | Details |
|---------|---------|
| **Cost** | 🆓 **FREE!** |
| **Model** | `whisper-large-v3` (best accuracy) |
| **Speed** | Very fast (Groq is known for speed) |
| **Languages** | 97+ languages (auto-detected) |
| **Accuracy** | Excellent (same as OpenAI Whisper) |
| **Limits** | Check Groq console for current limits |

---

## 🔧 Troubleshooting

### Error: "Groq API key not configured"
✅ **Fix**: Add your API key to line 24 of `Translator.java`

### Error: "401 Unauthorized"  
✅ **Fix**: Check your API key is correct and active

### Error: "429 Too Many Requests"
✅ **Fix**: You hit the free tier limit - wait or upgrade

### Transcription is blank or wrong
✅ **Fix**: 
- Speak louder and clearer
- Check microphone is working (test the WAV file)
- Ensure good audio quality

---

## 🎉 After Setup

Your complete workflow will be:

```
YOU SPEAK → Groq Whisper (FREE) → OpenL Translate (FREE) → Speechify TTS
   🎤            🤖 Transcribe         🌐 Translate         🔊 Speak
```

---

## 💡 Optional: Change Whisper Model

In `Translator.java` line 26, you can use:

```java
private static final String GROQ_MODEL = "whisper-large-v3";        // Best accuracy
private static final String GROQ_MODEL = "whisper-large-v3-turbo";  // Faster
private static final String GROQ_MODEL = "distil-whisper-large-v3-en"; // English only, fastest
```

---

## 🆘 Need Help?

1. **Groq Docs**: https://console.groq.com/docs
2. **Groq Discord**: Check Groq website for community link
3. **Check Console**: https://console.groq.com/ - see your usage

---

## ✅ Quick Checklist

- [ ] Created Groq account at https://console.groq.com/
- [ ] Generated API key
- [ ] Added API key to `Translator.java` line 24
- [ ] Ran `mvn clean install`
- [ ] Ran `mvn exec:java "-Dexec.classpathScope=runtime"`
- [ ] Tested: Press SPACE, speak, release SPACE
- [ ] Verified transcription appears in console

---

**That's it! You now have FREE, professional-grade speech-to-text!** 🎉

