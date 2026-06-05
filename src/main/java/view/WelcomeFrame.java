package view;

import util.ThemeManager;
import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Hotel Management System - Welcome");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel lblWelcome = new JLabel("Welcome to Grand Stay Hotel", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(0, 255, 127)); // Neon Green
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblWelcome, gbc);

        JButton btnGuest = new JButton("Guest Portal");
        btnGuest.setFont(new Font("Arial", Font.BOLD, 16));
        btnGuest.setPreferredSize(new Dimension(180, 60));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(btnGuest, gbc);

        JButton btnStaff = new JButton("Staff Portal");
        btnStaff.setFont(new Font("Arial", Font.BOLD, 16));
        btnStaff.setPreferredSize(new Dimension(180, 60));
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(btnStaff, gbc);

        add(mainPanel, BorderLayout.CENTER);

        btnGuest.addActionListener(e -> {
            new UserLoginFrame().setVisible(true);
            dispose();
        });

        btnStaff.addActionListener(e -> {
            new AdminLoginFrame().setVisible(true);
            dispose();
        });
    }
}
