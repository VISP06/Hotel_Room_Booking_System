package main;

import util.ThemeManager;
import view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Apply neon theme first to maintain global aesthetic
        ThemeManager.applyNeonTheme();

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
