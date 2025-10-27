package com.bhojpurri;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application entry point for Bhojpurri application.
 * This application allows users to record English speech and get Bhojpuri translation
 * with text-to-speech output.
 */
public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        // Force UTF-8 encoding for the entire JVM
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("console.encoding", "UTF-8");
        
        logger.info("Starting Bhojpurri Application...");
        
        // Set UTF-8 encoding for console output (fixes Urdu, Arabic, etc.)
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
            System.out.println("✅ Console encoding set to UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            logger.warn("Could not set UTF-8 console encoding: {}", e.getMessage());
        }
        
        // Load environment variables from .env file
        EnvLoader.load();
        logger.info("Environment variables loaded");
        
        // Set look and feel to system default for better native appearance
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            logger.warn("Could not set system look and feel: {}", e.getMessage());
        }

        // Launch UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                BilluUI ui = new BilluUI();
                ui.createAndShowGUI();
                logger.info("Application UI initialized successfully");
            } catch (Exception e) {
                logger.error("Failed to initialize application UI", e);
                System.exit(1);
            }
        });
    }
}
