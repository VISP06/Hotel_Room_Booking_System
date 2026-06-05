package main;

import util.ThemeManager;
import view.WelcomeFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Apply neon theme first to maintain global aesthetic
        ThemeManager.applyNeonTheme();

        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        });
    }
}
