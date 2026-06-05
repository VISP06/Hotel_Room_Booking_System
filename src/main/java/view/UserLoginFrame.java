package view;

import dao.AccountDAO;
import util.UserSession;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class UserLoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private AccountDAO accountDAO;

    public UserLoginFrame() {
        this.accountDAO = new AccountDAO();
        setTitle("Guest Portal - Login");
        setSize(400, 350);
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

        JLabel lblTitle = new JLabel("Guest Authentication", SwingConstants.CENTER);
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

        JLabel lblSignup = new JLabel("Don't have an account? Sign Up", SwingConstants.CENTER);
        lblSignup.setForeground(new Color(0, 255, 127));
        lblSignup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        mainPanel.add(lblSignup, gbc);

        JButton btnBack = new JButton("Back to Welcome");
        gbc.gridy = 5;
        mainPanel.add(btnBack, gbc);

        add(mainPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            try {
                String role = accountDAO.login(user, pass);
                if ("USER".equals(role)) {
                    UserSession.getInstance().setUser(user, role);
                    new MainDashboard().setVisible(true);
                    this.dispose();
                } else if ("ADMIN".equals(role)) {
                    JOptionPane.showMessageDialog(this, "Access Denied: Use Staff Portal for Admin accounts.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        lblSignup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SignupFrame().setVisible(true);
                dispose();
            }
        });

        btnBack.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }
}
