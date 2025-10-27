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
    private JComboBox<LanguageOption> languageDropdown; // Language selector
    private SpeechRecorder recorder;
    private Translator translator;
    private TTSManager ttsManager;
    private DatabaseManager dbManager;
    private CatAnimator catAnimator; // Animated cat sprite handler
    private volatile boolean isProcessing = false;
    private String lastTTSFilePath = null; // Store last played TTS file
    private JButton replayButton; // Button to replay last audio
    private JButton historyButton; // Button to show history
    private JButton resetDbButton; // Button to reset database
    
    // Language options for translation
    private static class LanguageOption {
        String name;
        String code;
        String flag;
        
        LanguageOption(String name, String code, String flag) {
            this.name = name;
            this.code = code;
            this.flag = flag;
        }
        
        @Override
        public String toString() {
            return flag + " " + name;
        }
    }

    public BilluUI() {
        recorder = new SpeechRecorder();
        translator = new Translator();
        ttsManager = new TTSManager();
        dbManager = new DatabaseManager();
    }

    public void createAndShowGUI() {
        frame = new JFrame("🐱 Bhojpurri - Billu the Cat");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        // LEFT SIDE - Cat Animation (centered)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 248, 255));
        leftPanel.setPreferredSize(new Dimension(500, 600));
        
        // Cat display area (centered)
        JPanel catPanel = new JPanel();
        catPanel.setLayout(new BoxLayout(catPanel, BoxLayout.Y_AXIS));
        catPanel.setBackground(new Color(240, 248, 255));
        
        // Add vertical glue to center the cat
        catPanel.add(Box.createVerticalGlue());
        
        // Create mascot label for cat sprite (no emoji text!)
        mascotLabel = new JLabel();
        mascotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mascotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        catPanel.add(mascotLabel);
        catPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        
        // Status label below cat
        statusLabel = new JLabel("Press and hold SPACE to talk...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        statusLabel.setForeground(new Color(70, 130, 180));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        catPanel.add(statusLabel);
        
        catPanel.add(Box.createVerticalGlue());
        
        leftPanel.add(catPanel, BorderLayout.CENTER);
        
        // Initialize cat animator (no emoji fallback displayed)
        try {
            catAnimator = new CatAnimator(mascotLabel);
            if (catAnimator.isLoaded()) {
                logger.info("Cat sprite animations loaded successfully!");
                catAnimator.start(); // Start the idle animation
            } else {
                logger.warn("Failed to load cat sprites");
                mascotLabel.setText("");
            }
        } catch (Exception e) {
            logger.error("Error initializing cat animator", e);
            mascotLabel.setText(""); // No emoji placeholder
        }

        // RIGHT SIDE - Console/Output Area
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 248, 255));
        
        // Top area with console title and language selector
        JPanel topRightPanel = new JPanel(new BorderLayout(5, 5));
        topRightPanel.setBackground(new Color(240, 248, 255));
        
        // Console title
        JLabel consoleTitle = new JLabel("📝 Translation Console", SwingConstants.CENTER);
        consoleTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        consoleTitle.setForeground(new Color(70, 130, 180));
        
        // Language selector panel
        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        languagePanel.setBackground(new Color(240, 248, 255));
        
        JLabel languageLabel = new JLabel("🌍 Output Language:");
        languageLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        languageLabel.setForeground(new Color(70, 130, 180));
        
        // Create language dropdown with popular languages
        LanguageOption[] languages = {
            new LanguageOption("Bhojpuri", "bho", "🇮🇳"),
            new LanguageOption("Hindi", "hi", "🇮🇳"),
            new LanguageOption("Spanish", "es", "🇪🇸"),
            new LanguageOption("French", "fr", "🇫🇷"),
            new LanguageOption("German", "de", "🇩🇪"),
            new LanguageOption("Italian", "it", "🇮🇹"),
            new LanguageOption("Portuguese", "pt", "🇵🇹"),
            new LanguageOption("Russian", "ru", "🇷🇺"),
            new LanguageOption("Japanese", "ja", "🇯🇵"),
            new LanguageOption("Korean", "ko", "🇰🇷"),
            new LanguageOption("Chinese", "zh", "🇨🇳"),
            new LanguageOption("Arabic", "ar", "🇸🇦"),
            new LanguageOption("Turkish", "tr", "🇹🇷"),
            new LanguageOption("Dutch", "nl", "🇳🇱"),
            new LanguageOption("Polish", "pl", "🇵🇱"),
            new LanguageOption("Swedish", "sv", "🇸🇪"),
            new LanguageOption("Norwegian", "no", "🇳🇴"),
            new LanguageOption("Danish", "da", "🇩🇰"),
            new LanguageOption("Finnish", "fi", "🇫🇮"),
            new LanguageOption("Greek", "el", "🇬🇷"),
            new LanguageOption("Hebrew", "he", "🇮🇱"),
            new LanguageOption("Thai", "th", "🇹🇭"),
            new LanguageOption("Vietnamese", "vi", "🇻🇳"),
            new LanguageOption("Indonesian", "id", "🇮🇩"),
            new LanguageOption("Malay", "ms", "🇲🇾"),
            new LanguageOption("Bengali", "bn", "🇧🇩"),
            new LanguageOption("Tamil", "ta", "🇮🇳"),
            new LanguageOption("Telugu", "te", "🇮🇳"),
            new LanguageOption("Marathi", "mr", "🇮🇳"),
            new LanguageOption("Gujarati", "gu", "🇮🇳"),
            new LanguageOption("Kannada", "kn", "🇮🇳"),
            new LanguageOption("Punjabi", "pa", "🇮🇳"),
            new LanguageOption("Urdu", "ur", "🇵🇰")
        };
        
        languageDropdown = new JComboBox<>(languages);
        languageDropdown.setFont(new Font("SansSerif", Font.PLAIN, 12));
        languageDropdown.setSelectedIndex(0); // Default to Bhojpuri
        languageDropdown.setToolTipText("Select output language for translation");
        
        // Add action listener to show selected language
        languageDropdown.addActionListener(e -> {
            LanguageOption selected = (LanguageOption) languageDropdown.getSelectedItem();
            if (selected != null) {
                logger.info("Language changed to: {} ({})", selected.name, selected.code);
                statusLabel.setText("Language: " + selected.flag + " " + selected.name + " | Press SPACE to talk...");
            }
        });
        
        languagePanel.add(languageLabel);
        languagePanel.add(languageDropdown);
        
        topRightPanel.add(consoleTitle, BorderLayout.NORTH);
        topRightPanel.add(languagePanel, BorderLayout.CENTER);
        
        // Output text area
        outputArea = new JTextArea(25, 35);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        
        // Use a font that supports Devanagari script (Hindi/Bhojpuri)
        Font devanagariFont = null;
        String[] fontNames = {"Nirmala UI", "Mangal", "Arial Unicode MS", "Segoe UI", "Dialog"};
        for (String fontName : fontNames) {
            Font testFont = new Font(fontName, Font.PLAIN, 14);
            if (testFont.canDisplay('\u0915')) { // Test with Devanagari character 'क'
                devanagariFont = testFont;
                logger.info("Using font '{}' for Devanagari text display", fontName);
                break;
            }
        }
        if (devanagariFont == null) {
            devanagariFont = new Font("Dialog", Font.PLAIN, 14);
            logger.warn("No Devanagari-capable font found, using Dialog as fallback");
        }
        outputArea.setFont(devanagariFont);
        
        outputArea.setBackground(Color.WHITE);
        outputArea.setBorder(BorderFactory.createLineBorder(new Color(176, 196, 222), 2));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        rightPanel.add(topRightPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom buttons panel for right side
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonsPanel.setBackground(new Color(240, 248, 255));
        
        // Replay button
        replayButton = new JButton("🔊 Replay Last");
        replayButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        replayButton.setEnabled(false); // Disabled until first TTS plays
        replayButton.setToolTipText("Replay the last translation audio");
        replayButton.addActionListener(e -> replayLastAudio());
        
        // History button
        historyButton = new JButton("📜 History");
        historyButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        historyButton.setToolTipText("View translation history");
        historyButton.addActionListener(e -> showHistoryDialog());
        
        // Reset database button
        resetDbButton = new JButton("🗑️ Reset DB");
        resetDbButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        resetDbButton.setToolTipText("Clear all database records");
        resetDbButton.setForeground(Color.RED);
        resetDbButton.addActionListener(e -> resetDatabase());
        
        buttonsPanel.add(replayButton);
        buttonsPanel.add(historyButton);
        buttonsPanel.add(resetDbButton);
        
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Bottom panel - Instructions
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(240, 248, 255));
        JLabel instructionLabel = new JLabel("Hold SPACE to record | Release to translate and speak");
        instructionLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        instructionLabel.setForeground(Color.GRAY);
        instructionPanel.add(instructionLabel);

        // Assemble the layout: Cat (LEFT) + Console (RIGHT)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500); // Cat takes 500px width
        splitPane.setEnabled(false); // Lock divider position
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
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
        // Clear previous output for new recording
        clearOutput();
        updateUI("🎙️ Listening... (Recording)", null);
        
        // Change cat animation to LISTENING (paw over ear!)
        if (catAnimator != null) {
            catAnimator.setState(CatAnimator.AnimationState.LISTENING);
        }
        
        recorder.startRecording();
    }

    private void handleSpaceReleased() {
        logger.info("Space key released - stopping recording");
        isProcessing = true;
        updateUI("🧠 Processing...", "Recording stopped. Transcribing and translating...");
        
        // Change cat animation to THINKING
        if (catAnimator != null) {
            catAnimator.setState(CatAnimator.AnimationState.THINKING);
        }
        
        String filePath = recorder.stopRecording();
        
        // Process recording asynchronously
        CompletableFuture.runAsync(() -> processRecording(filePath))
            .exceptionally(ex -> {
                logger.error("Error during processing", ex);
                SwingUtilities.invokeLater(() -> {
                    updateUI("⚠️ Error occurred", "Error: " + ex.getMessage());
                    
                    // Set cat to ERROR state
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.ERROR);
                    }
                    
                    isProcessing = false;
                });
                return null;
            });
    }

    private void processRecording(String filePath) {
        String englishText = null;
        String translatedText = null;
        String ttsPath = null;
        long audioSize = 0;
        
        // Get selected language
        LanguageOption selectedLang = (LanguageOption) languageDropdown.getSelectedItem();
        String targetLangCode = selectedLang != null ? selectedLang.code : "bho";
        String targetLangName = selectedLang != null ? selectedLang.name : "Bhojpuri";
        
        try {
            logger.info("Processing recording from: {}", filePath);
            
            // Get audio file size
            java.io.File audioFile = new java.io.File(filePath);
            audioSize = audioFile.length();
            
            // Step 1: Transcribe to English
            logger.info("Transcribing audio to English text...");
            final String transcribedEnglish = translator.transcribeToEnglish(filePath);
            englishText = transcribedEnglish;
            logger.info("Transcribed text: {}", englishText);
            
            SwingUtilities.invokeLater(() -> 
                appendOutput("English: " + transcribedEnglish)
            );

            // Step 2: Translate to selected language
            logger.info("Translating English to {}...", targetLangName);
            final String translated = translator.translateTo(englishText, targetLangCode);
            translatedText = translated;
            logger.info("Translated text: {}", translatedText);
            
            // For Urdu/Arabic/Hebrew, show note about display
            if (targetLangCode.equals("ur")) {
                // Note: Urdu is being spoken correctly, but displaying romanized version
                System.out.println("\n📝 " + targetLangName + " (Urdu script): " + translated);
                System.out.println("   ℹ️ Note: Urdu text is displayed above (may not render in console)");
                System.out.println("   ℹ️ The audio will speak Urdu correctly!");
            } else {
                System.out.println("\n📝 " + targetLangName + ": " + translated);
            }
            
            final String langName = targetLangName; // For lambda capture
            SwingUtilities.invokeLater(() -> {
                appendOutput(langName + ": " + translated);
                updateUI("🔊 Speaking...", null);
                
                // Change cat animation to SPEAKING (mouth movement!)
                if (catAnimator != null) {
                    catAnimator.setState(CatAnimator.AnimationState.SPEAKING);
                }
            });

            // Step 3: Convert to speech and play
            logger.info("Converting {} text to speech...", targetLangName);
            final String finalTtsPath = ttsManager.speak(translatedText, targetLangCode);
            ttsPath = finalTtsPath;
            lastTTSFilePath = finalTtsPath; // Store for replay functionality
            logger.info("Speech playback completed");
            
            // Enable replay button after first TTS
            SwingUtilities.invokeLater(() -> {
                if (replayButton != null && finalTtsPath != null) {
                    replayButton.setEnabled(true);
                }
            });

            // Step 4: Save to database
            final String finalEnglish = englishText;
            final String finalTranslated = translatedText;
            final long finalAudioSize = audioSize;
            final String finalLangCode = targetLangCode;
            
            int dbId = dbManager.saveTranslation(filePath, finalAudioSize, finalEnglish, 
                                                  finalTranslated, finalLangCode, finalTtsPath);
            
            if (dbId > 0) {
                logger.info("💾 Translation saved to database with ID: {}", dbId);
                SwingUtilities.invokeLater(() -> 
                    appendOutput("💾 Saved to database (ID: " + dbId + ")")
                );
            }

            // Reset UI
            SwingUtilities.invokeLater(() -> {
                updateUI("✅ Done! Press SPACE to talk again...", null);
                
                // Change cat animation back to IDLE
                if (catAnimator != null) {
                    catAnimator.setState(CatAnimator.AnimationState.IDLE);
                }
                
                appendOutput("---");
                isProcessing = false;
            });

        } catch (Exception ex) {
            logger.error("Error processing recording", ex);
            SwingUtilities.invokeLater(() -> {
                updateUI("⚠️ Error: " + ex.getMessage(), 
                    "Failed to process. Please try again.");
                
                // Set cat to ERROR state on error
                if (catAnimator != null) {
                    catAnimator.setState(CatAnimator.AnimationState.ERROR);
                }
                
                isProcessing = false;
            });
        }
    }

    private void updateUI(String status, String output) {
        // Update status label
        statusLabel.setText(status);
        if (output != null) {
            appendOutput(output);
        }
    }

    private void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    
    /**
     * Replay the last played TTS audio
     */
    private void replayLastAudio() {
        if (lastTTSFilePath == null) {
            JOptionPane.showMessageDialog(frame, 
                "No audio to replay yet!", 
                "Replay", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    appendOutput("🔊 Replaying last audio...");
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.SPEAKING);
                    }
                });
                
                AudioPlayer audioPlayer = new AudioPlayer();
                audioPlayer.play(lastTTSFilePath);
                
                SwingUtilities.invokeLater(() -> {
                    appendOutput("✅ Replay complete!");
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.IDLE);
                    }
                });
            } catch (Exception e) {
                logger.error("Failed to replay audio", e);
                SwingUtilities.invokeLater(() -> {
                    appendOutput("❌ Replay failed: " + e.getMessage());
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.ERROR);
                    }
                });
            }
        });
    }
    
    /**
     * Show translation history dialog
     */
    private void showHistoryDialog() {
        JDialog historyDialog = new JDialog(frame, "📜 Translation History", true);
        historyDialog.setSize(900, 500);
        historyDialog.setLocationRelativeTo(frame);
        
        // Get all translations from database
        java.util.List<DatabaseManager.TranslationRecord> records = dbManager.getAllTranslations();
        
        if (records.isEmpty()) {
            JLabel emptyLabel = new JLabel("No translations yet!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            historyDialog.add(emptyLabel);
            historyDialog.setVisible(true);
            return;
        }
        
        // Create table model
        String[] columnNames = {"ID", "Date/Time", "English", "Translation", "Language", "Actions"};
        Object[][] data = new Object[records.size()][6];
        
        for (int i = 0; i < records.size(); i++) {
            DatabaseManager.TranslationRecord rec = records.get(i);
            data[i][0] = rec.id;
            data[i][1] = rec.createdAt.toString().substring(0, 19); // Format timestamp
            data[i][2] = truncate(rec.englishText, 30);
            data[i][3] = truncate(rec.translatedText, 30);
            data[i][4] = rec.targetLanguage;
            data[i][5] = "▶️ Play";
        }
        
        JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Actions column clickable
            }
        };
        
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // Add mouse listener for play button
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                
                if (col == 5 && row >= 0) { // Actions column clicked
                    DatabaseManager.TranslationRecord rec = records.get(row);
                    playHistoryAudio(rec);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel infoLabel = new JLabel("Click ▶️ to play audio • Total: " + records.size() + " translations");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(infoLabel, BorderLayout.SOUTH);
        
        historyDialog.add(panel);
        historyDialog.setVisible(true);
    }
    
    /**
     * Play audio from history record
     */
    private void playHistoryAudio(DatabaseManager.TranslationRecord record) {
        if (record.ttsFilePath == null || record.ttsFilePath.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "No TTS audio available for this record", 
                "Play Audio", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    appendOutput("🔊 Playing history audio (ID: " + record.id + ")...");
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.SPEAKING);
                    }
                });
                
                AudioPlayer audioPlayer = new AudioPlayer();
                audioPlayer.play(record.ttsFilePath);
                
                SwingUtilities.invokeLater(() -> {
                    appendOutput("✅ Playback complete!");
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.IDLE);
                    }
                });
            } catch (Exception e) {
                logger.error("Failed to play history audio", e);
                SwingUtilities.invokeLater(() -> {
                    appendOutput("❌ Playback failed: " + e.getMessage());
                    if (catAnimator != null) {
                        catAnimator.setState(CatAnimator.AnimationState.ERROR);
                    }
                });
            }
        });
    }
    
    /**
     * Reset database - clear all data
     */
    private void resetDatabase() {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "⚠️ This will DELETE ALL translation records!\n\nAre you sure?",
            "Reset Database",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dbManager.resetDatabase();
            appendOutput("🗑️ Database reset - all records deleted");
            JOptionPane.showMessageDialog(frame, 
                "Database has been reset successfully!", 
                "Reset Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Truncate text for display
     */
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    private void clearOutput() {
        outputArea.setText("");
    }
}
