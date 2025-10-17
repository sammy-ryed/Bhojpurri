package com.bhojpurri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main UI class for Bhojpurri application.
 * Manages the graphical interface and handles user interactions.
 */
public class BilluUI {
    private static final Logger logger = LoggerFactory.getLogger(BilluUI.class);
    
    private JFrame frame;
    private JLabel statusLabel;
    private JLabel mascotLabel;
    private JTextArea outputArea;
    private SpeechRecorder recorder;
    private Translator translator;
    private TTSManager ttsManager;
    private volatile boolean isProcessing = false;

    // Animation states
    private static final String IDLE_STATE = "😺";
    private static final String LISTENING_STATE = "🎙️ 😸";
    private static final String THINKING_STATE = "🧠 🤔";
    private static final String SPEAKING_STATE = "🔊 😻";

    public BilluUI() {
        recorder = new SpeechRecorder();
        translator = new Translator();
        ttsManager = new TTSManager();
    }

    public void createAndShowGUI() {
        frame = new JFrame("🐱 Bhojpurri - Billu the Cat");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Top panel - Mascot
        JPanel mascotPanel = new JPanel();
        mascotPanel.setBackground(new Color(240, 248, 255));
        mascotLabel = new JLabel(IDLE_STATE);
        mascotLabel.setFont(new Font("SansSerif", Font.PLAIN, 80));
        mascotPanel.add(mascotLabel);

        // Center panel - Status and output
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(new Color(240, 248, 255));

        statusLabel = new JLabel("Press and hold SPACE to talk...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setForeground(new Color(70, 130, 180));

        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        
        // Use a font that supports Devanagari script (Hindi/Bhojpuri)
        // Try multiple fonts in order of preference
        Font devanagariFont = null;
        String[] fontNames = {"Nirmala UI", "Mangal", "Arial Unicode MS", "Segoe UI", "Dialog"};
        for (String fontName : fontNames) {
            Font testFont = new Font(fontName, Font.PLAIN, 16);
            if (testFont.canDisplay('\u0915')) { // Test with Devanagari character 'क'
                devanagariFont = testFont;
                logger.info("Using font '{}' for Devanagari text display", fontName);
                break;
            }
        }
        if (devanagariFont == null) {
            devanagariFont = new Font("Dialog", Font.PLAIN, 16);
            logger.warn("No Devanagari-capable font found, using Dialog as fallback");
        }
        outputArea.setFont(devanagariFont);
        
        outputArea.setBackground(Color.WHITE);
        outputArea.setBorder(BorderFactory.createLineBorder(new Color(176, 196, 222), 2));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        centerPanel.add(statusLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel - Instructions
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(240, 248, 255));
        JLabel instructionLabel = new JLabel("Hold SPACE to record | Release to translate");
        instructionLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        instructionPanel.add(instructionLabel);

        mainPanel.add(mascotPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(instructionPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Key listener for spacebar
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (e.getID() == KeyEvent.KEY_PRESSED && !isProcessing) {
                        handleSpacePressed();
                    } else if (e.getID() == KeyEvent.KEY_RELEASED && !isProcessing) {
                        handleSpaceReleased();
                    }
                    return true; // Consume event
                }
                return false;
            }
        });

        logger.info("GUI created and displayed successfully");
    }

    private void handleSpacePressed() {
        logger.info("Space key pressed - starting recording");
        updateUI(LISTENING_STATE, "🎙️ Listening... (Recording)", null);
        recorder.startRecording();
    }

    private void handleSpaceReleased() {
        logger.info("Space key released - stopping recording");
        isProcessing = true;
        updateUI(THINKING_STATE, "🧠 Processing...", "Recording stopped. Transcribing and translating...");
        
        String filePath = recorder.stopRecording();
        
        // Process recording asynchronously
        CompletableFuture.runAsync(() -> processRecording(filePath))
            .exceptionally(ex -> {
                logger.error("Error during processing", ex);
                SwingUtilities.invokeLater(() -> {
                    updateUI(IDLE_STATE, "⚠️ Error occurred", "Error: " + ex.getMessage());
                    isProcessing = false;
                });
                return null;
            });
    }

    private void processRecording(String filePath) {
        try {
            logger.info("Processing recording from: {}", filePath);
            
            // Step 1: Transcribe to English
            logger.info("Transcribing audio to English text...");
            String englishText = translator.transcribeToEnglish(filePath);
            logger.info("Transcribed text: {}", englishText);
            
            SwingUtilities.invokeLater(() -> 
                appendOutput("English: " + englishText)
            );

            // Step 2: Translate to Bhojpuri
            logger.info("Translating English to Bhojpuri...");
            String bhojpuriText = translator.translateToBhojpuri(englishText);
            logger.info("Translated text: {}", bhojpuriText);
            
            SwingUtilities.invokeLater(() -> {
                appendOutput("Bhojpuri: " + bhojpuriText);
                updateUI(SPEAKING_STATE, "🔊 Speaking...", null);
            });

            // Step 3: Convert to speech and play
            logger.info("Converting Bhojpuri text to speech...");
            ttsManager.speak(bhojpuriText);
            logger.info("Speech playback completed");

            // Reset UI
            SwingUtilities.invokeLater(() -> {
                updateUI(IDLE_STATE, "✅ Done! Press SPACE to talk again...", null);
                appendOutput("---");
                isProcessing = false;
            });

        } catch (Exception ex) {
            logger.error("Error processing recording", ex);
            SwingUtilities.invokeLater(() -> {
                updateUI(IDLE_STATE, "⚠️ Error: " + ex.getMessage(), 
                    "Failed to process. Please try again.");
                isProcessing = false;
            });
        }
    }

    private void updateUI(String mascotState, String status, String output) {
        mascotLabel.setText(mascotState);
        statusLabel.setText(status);
        if (output != null) {
            appendOutput(output);
        }
    }

    private void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
}
