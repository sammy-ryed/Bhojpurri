package com.bhojpurri;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages Text-to-Speech conversion with debugging and fallback support.
 */
public class TTSManager {
    private static final Logger logger = LoggerFactory.getLogger(TTSManager.class);
    
    // API Configuration
    private static final String SPEECHIFY_API_KEY = "leP8fg7FfN3g6CJEnyLW_lBalB1EUsCriNk9sRThK3M=";
    private static final String[] SPEECHIFY_ENDPOINTS = {
        "https://api.sws.speechify.com/v1/audio",
        "https://api.sws.speechify.com/v1/audio/speech",
        "https://audio.api.speechify.com/v1/audio"
    };
    
    private final HttpClient httpClient;
    private final AudioPlayer audioPlayer;
    private final Path outputDirectory;

    public TTSManager() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(30))
            .build();
        this.audioPlayer = new AudioPlayer();
        
        // Create output directory for TTS audio files
        this.outputDirectory = Paths.get("tts_output");
        try {
            if (!Files.exists(outputDirectory)) {
                Files.createDirectories(outputDirectory);
                System.out.println("📁 Created directory: " + outputDirectory.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to create TTS output directory", e);
        }
    }

    /**
     * Converts text to speech - tries Speechify first, then falls back to Google TTS (FREE).
     */
    public void speak(String text) throws IOException, InterruptedException {
        if (text == null || text.trim().isEmpty()) {
            System.out.println("⚠️ Empty text provided for TTS");
            return;
        }

        System.out.println("\n🔊 Starting TTS for: " + text);
        logger.info("Converting text to speech: {}", text);

        boolean success = false;
        Exception lastException = null;

        // Try Speechify API endpoints
        for (String endpoint : SPEECHIFY_ENDPOINTS) {
            System.out.println("🔹 Trying Speechify endpoint: " + endpoint);
            logger.info("Trying Speechify endpoint: {}", endpoint);
            
            try {
                if (trySpeechifyTTS(text, endpoint)) {
                    System.out.println("✅ Speechify TTS successful!");
                    success = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Speechify TTS failed: " + e.getMessage());
                logger.warn("Speechify TTS failed with endpoint {}: {}", endpoint, e.getMessage());
                lastException = e;
            }
        }

        // Fallback to Google TTS if Speechify failed
        if (!success) {
            System.out.println("🔄 Speechify failed - using FREE Google TTS fallback...");
            logger.info("Falling back to Google TTS");
            try {
                useGoogleTTS(text);
                System.out.println("✅ Google TTS successful!");
                success = true;
            } catch (Exception e) {
                System.out.println("❌ Google TTS also failed: " + e.getMessage());
                logger.error("Google TTS failed", e);
                lastException = e;
            }
        }

        // If everything failed, throw error
        if (!success) {
            System.out.println("❌ All TTS methods failed!");
            logger.error("All TTS methods failed");
            String errorMsg = lastException != null ? lastException.getMessage() : "Unknown error";
            throw new IOException("TTS conversion failed: " + errorMsg, lastException);
        }
    }

    /**
     * Attempts to use Speechify API.
     */
    private boolean trySpeechifyTTS(String text, String endpoint) throws IOException, InterruptedException {
        Path outputFile = outputDirectory.resolve("output.mp3");

        try {
            // Prepare JSON body - different endpoints need different field names
            JSONObject requestBody = new JSONObject();
            
            // Try both "input" and "text" field names
            if (endpoint.contains("/speech")) {
                requestBody.put("input", text); // /v1/audio/speech uses "input"
                requestBody.put("voice_id", "hi-IN-SwaraNeural");
            } else {
                requestBody.put("text", text); // Other endpoints use "text"
                requestBody.put("voice_id", "hi-IN");
            }
            requestBody.put("audio_format", "mp3");
            
            System.out.println("   Request body: " + requestBody.toString());

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + SPEECHIFY_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .timeout(java.time.Duration.ofSeconds(30))
                .build();

            HttpResponse<Path> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofFile(outputFile)
            );

            int statusCode = response.statusCode();
            System.out.println("   Response status: " + statusCode);

            if (statusCode == 200 || statusCode == 201) {
                if (Files.exists(outputFile) && Files.size(outputFile) > 0) {
                    System.out.println("   File size: " + Files.size(outputFile) + " bytes");
                    System.out.println("   Playing: " + outputFile.toAbsolutePath());
                    audioPlayer.play(outputFile.toString());
                    return true;
                }
            } else {
                String errorMsg = "Status " + statusCode;
                try {
                    errorMsg = Files.readString(outputFile);
                    System.out.println("   Error response: " + errorMsg);
                } catch (Exception ignored) {}
            }
            
            Files.deleteIfExists(outputFile);
            return false;

        } catch (Exception e) {
            Files.deleteIfExists(outputFile);
            throw e;
        }
    }

    /**
     * Google TTS fallback - FREE and reliable!
     * Uses Google Translate's text-to-speech API (no key needed).
     */
    private void useGoogleTTS(String text) throws IOException, InterruptedException {
        Path outputFile = outputDirectory.resolve("tts_output.mp3");

        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            // Use Hindi voice for Bhojpuri (closest match)
            String url = "https://translate.google.com/translate_tts?ie=UTF-8&q=" + encodedText + "&tl=hi&client=tw-ob";
            
            System.out.println("   Google TTS URL: " + url);
            logger.info("Using Google TTS with URL: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .GET()
                .timeout(java.time.Duration.ofSeconds(30))
                .build();

            HttpResponse<Path> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofFile(outputFile)
            );

            if (response.statusCode() == 200 && Files.exists(outputFile) && Files.size(outputFile) > 0) {
                System.out.println("   File size: " + Files.size(outputFile) + " bytes");
                System.out.println("   Saved to: " + outputFile.toAbsolutePath());
                System.out.println("   Playing audio...");
                audioPlayer.play(outputFile.toString());
                System.out.println("   ✅ Playback completed");
            } else {
                Files.deleteIfExists(outputFile);
                throw new IOException("Google TTS failed with status: " + response.statusCode());
            }

        } catch (Exception e) {
            Files.deleteIfExists(outputFile);
            throw e;
        }
    }

    /**
     * Test method to verify Speechify API connectivity.
     */
    public static void testSpeechifyConnection() {
        System.out.println("\n🧪 Testing Speechify API...");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sws.speechify.com/v1/audio"))
                .header("Authorization", "Bearer " + SPEECHIFY_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"voice_id\":\"hi-IN\",\"text\":\"Namaste duniya\"}"))
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            
            if (response.statusCode() == 200) {
                System.out.println("✅ Speechify API is working!");
            } else {
                System.out.println("⚠️ Speechify API returned error code");
            }
        } catch (Exception e) {
            System.out.println("❌ Speechify API test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cleans up old TTS audio files.
     */
    public void cleanupOldFiles() {
        try {
            long currentTime = System.currentTimeMillis();
            long oneHourAgo = currentTime - (60 * 60 * 1000);

            Files.list(outputDirectory)
                .filter(path -> path.toString().endsWith(".mp3"))
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toMillis() < oneHourAgo;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        logger.debug("Deleted old TTS file: {}", path);
                    } catch (IOException e) {
                        logger.warn("Failed to delete old file: {}", path);
                    }
                });
        } catch (IOException e) {
            logger.warn("Error during cleanup", e);
        }
    }
}
