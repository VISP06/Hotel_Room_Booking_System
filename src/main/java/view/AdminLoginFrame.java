package view;

import dao.AccountDAO;
import util.UserSession;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminLoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private AccountDAO accountDAO;

    public AdminLoginFrame() {
        this.accountDAO = new AccountDAO();
        setTitle("Staff Portal - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Staff Authentication", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 255, 127));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(btnLogin, gbc);

        JButton btnBack = new JButton("Back to Welcome");
        gbc.gridy = 4;
        mainPanel.add(btnBack, gbc);

        add(mainPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            try {
                String role = accountDAO.login(user, pass);
                if ("ADMIN".equals(role)) {
                    UserSession.getInstance().setUser(user, role);
                    new MainDashboard().setVisible(true);
                    this.dispose();
                } else if ("USER".equals(role)) {
                    JOptionPane.showMessageDialog(this, "Access Denied: Use Guest Portal for User accounts.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        btnBack.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }
}
