package com.bhojpurri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ServiceLoader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles audio playback for TTS output.
 * Supports both WAV and MP3 formats.
 */
public class AudioPlayer {
    private static final Logger logger = LoggerFactory.getLogger(AudioPlayer.class);
    
    private Clip currentClip;
    
    static {
        // Check if mp3spi is available
        ServiceLoader<AudioFileReader> readers = ServiceLoader.load(AudioFileReader.class);
        boolean mp3SpiFound = false;
        System.out.println("\n🔍 Checking audio codec providers:");
        for (AudioFileReader reader : readers) {
            System.out.println("   - " + reader.getClass().getName());
            if (reader.getClass().getName().toLowerCase().contains("mp3") || 
                reader.getClass().getName().toLowerCase().contains("mpeg")) {
                mp3SpiFound = true;
            }
        }
        if (!mp3SpiFound) {
            System.out.println("⚠️  WARNING: MP3 codec (mp3spi) not found! MP3 playback will fail.");
            System.out.println("   Run with: mvn exec:java -Dexec.classpathScope=runtime");
        } else {
            System.out.println("✅ MP3 codec loaded successfully");
        }
    }

    /**
     * Plays an audio file through the system speakers.
     * Supports WAV and MP3 formats (requires mp3spi library for MP3).
     * 
     * @param filePath Path to the audio file to play
     * @throws RuntimeException if playback fails
     */
    public void play(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            logger.error("Invalid file path provided for playback");
            return;
        }

        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            logger.error("Audio file does not exist: {}", filePath);
            throw new RuntimeException("Audio file not found: " + filePath);
        }

        logger.info("Playing audio file: {} ({} bytes)", filePath, audioFile.length());
        
        // For MP3 files, always use JLayer for best compatibility
        if (filePath.toLowerCase().endsWith(".mp3")) {
            try {
                playWithJLayer(audioFile);
                return;
            } catch (Exception e) {
                logger.error("JLayer MP3 playback failed", e);
                throw new RuntimeException("MP3 playback failed: " + e.getMessage(), e);
            }
        }

        // For WAV files, use javax.sound
        try {
            // Stop any currently playing audio
            stopCurrentPlayback();

            // Get audio input stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            
            // Get audio format
            AudioFormat format = audioStream.getFormat();
            logger.debug("Audio format: {}", format);

            // Handle compressed formats (like MP3)
            AudioFormat decodedFormat = getDecodedFormat(format);
            if (decodedFormat != null) {
                logger.debug("Decoding audio from {} to PCM", format.getEncoding());
                audioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            }

            // Get a clip resource
            DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
            
            if (!AudioSystem.isLineSupported(info)) {
                logger.error("Audio line not supported for format: {}", audioStream.getFormat());
                throw new LineUnavailableException("Audio line not supported");
            }

            currentClip = (Clip) AudioSystem.getLine(info);
            currentClip.open(audioStream);

            // Start playback immediately
            currentClip.start();
            logger.info("Audio playback started");

            // Drain ensures all data is played before continuing
            currentClip.drain();
            
            // Wait a bit after draining to ensure audio buffer is fully played
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Cleanup
            currentClip.stop();
            currentClip.close();
            audioStream.close();
            
            logger.info("Audio playback finished successfully");

        } catch (UnsupportedAudioFileException e) {
            logger.error("Unsupported audio format", e);
            throw new RuntimeException("Unsupported audio format: " + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("I/O error during audio playback", e);
            throw new RuntimeException("Audio playback I/O error: " + e.getMessage(), e);
        } catch (LineUnavailableException e) {
            logger.error("Audio line unavailable", e);
            throw new RuntimeException("Cannot access audio output: " + e.getMessage(), e);
        }
    }

    /**
     * Fallback MP3 playback using JLayer AdvancedPlayer with playback events.
     * This is used for all MP3 files for best compatibility.
     */
    private void playWithJLayer(File audioFile) throws Exception {
        logger.info("Playing MP3 with JLayer: {}", audioFile.getAbsolutePath());
        
        FileInputStream fis = null;
        javazoom.jl.player.advanced.AdvancedPlayer player = null;
        
        try {
            fis = new FileInputStream(audioFile);
            player = new javazoom.jl.player.advanced.AdvancedPlayer(fis);
            
            System.out.println("🔊 Starting JLayer playback...");
            
            // Track if playback completed
            final boolean[] playbackComplete = {false};
            final int[] totalFrames = {0};
            
            // Add playback listener to track when playback actually finishes
            player.setPlayBackListener(new javazoom.jl.player.advanced.PlaybackListener() {
                @Override
                public void playbackStarted(javazoom.jl.player.advanced.PlaybackEvent evt) {
                    System.out.println("   ▶️ Playback started at frame " + evt.getFrame());
                }
                
                @Override
                public void playbackFinished(javazoom.jl.player.advanced.PlaybackEvent evt) {
                    totalFrames[0] = evt.getFrame();
                    playbackComplete[0] = true;
                    System.out.println("   ⏹️ Playback finished - decoded " + evt.getFrame() + " frames");
                }
            });
            
            // Small delay before starting to let sound system initialize
            // This prevents cutting the first syllable
            Thread.sleep(150);
            
            // Play the entire file - this blocks until decoding is complete
            player.play();
            
            // Wait for the playback listener to confirm completion
            Thread.sleep(100);
            
            if (!playbackComplete[0]) {
                logger.warn("⚠️ Playback listener did not fire - waiting additional time");
                // Give it more time if listener didn't fire
                Thread.sleep(500);
            }
            
            System.out.println("✅ JLayer playback finished (frames=" + totalFrames[0] + ")");
            logger.info("JLayer playback finished successfully");
            
            // CRITICAL: Extended delay to ensure sound card buffers are completely flushed
            // Without this, the last word/syllable gets cut off
            Thread.sleep(1000);
            
        } finally {
            if (player != null) {
                player.close();
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    /**
     * Gets the decoded PCM format for compressed audio formats.
     * 
     * @param baseFormat The original audio format
     * @return Decoded PCM format, or null if already PCM
     */
    private AudioFormat getDecodedFormat(AudioFormat baseFormat) {
        AudioFormat.Encoding encoding = baseFormat.getEncoding();
        
        // Check if decoding is needed
        if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) || 
            encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            return null; // Already PCM
        }

        // Return decoded format for compressed formats
        return new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            baseFormat.getSampleRate(),
            16, // Sample size in bits
            baseFormat.getChannels(),
            baseFormat.getChannels() * 2, // Frame size
            baseFormat.getSampleRate(),
            false // Little endian
        );
    }

    /**
     * Stops any currently playing audio.
     */
    public void stopCurrentPlayback() {
        if (currentClip != null && currentClip.isRunning()) {
            logger.info("Stopping current audio playback");
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    /**
     * Checks if audio is currently playing.
     * 
     * @return true if audio is playing, false otherwise
     */
    public boolean isPlaying() {
        return currentClip != null && currentClip.isRunning();
    }
}
