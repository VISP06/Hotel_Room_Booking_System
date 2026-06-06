package view;

import dao.BookingDAO;
import dao.RoomDAO;
import util.UserSession;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;
    private JTabbedPane tabbedPane;

    public MainDashboard() {
        this.roomDAO = new RoomDAO();
        this.bookingDAO = new BookingDAO();

        setTitle("Hotel Room Booking System - " + UserSession.getInstance().getRole());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 45, 45));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Hotel Management System");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnProfile = new JButton("Profile");
        headerPanel.add(btnProfile, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        String role = UserSession.getInstance().getRole();

        SearchRoomPanel searchRoomPanel = new SearchRoomPanel(roomDAO);
        
        if ("ADMIN".equals(role)) {
            AddRoomPanel addRoomPanel = new AddRoomPanel(roomDAO);
            ViewBookingsPanel viewBookingsPanel = new ViewBookingsPanel(bookingDAO);
            
            tabbedPane.addTab("Add Room", addRoomPanel);
            tabbedPane.addTab("View Bookings", viewBookingsPanel);
            tabbedPane.addTab("Search Availability", searchRoomPanel);
            
            tabbedPane.addChangeListener(e -> {
                int index = tabbedPane.getSelectedIndex();
                if (index == 1) viewBookingsPanel.refreshTable();
            });
        } else {
            BookRoomPanel bookRoomPanel = new BookRoomPanel(roomDAO, bookingDAO);
            CheckOutPanel checkOutPanel = new CheckOutPanel(bookingDAO, roomDAO);
            
            tabbedPane.addTab("Book Room", bookRoomPanel);
            tabbedPane.addTab("Search Availability", searchRoomPanel);
            tabbedPane.addTab("Check-Out", checkOutPanel);

            tabbedPane.addChangeListener(e -> {
                int index = tabbedPane.getSelectedIndex();
                if (index == 0) bookRoomPanel.refreshRooms();
                else if (index == 2) checkOutPanel.refreshBookings();
            });
        }

        add(tabbedPane, BorderLayout.CENTER);

        btnProfile.addActionListener(e -> showProfileDialog());
    }

    private void showProfileDialog() {
        JDialog dialog = new JDialog(this, "User Profile", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        dialog.add(new JLabel("Name: " + UserSession.getInstance().getUsername()), gbc);
        gbc.gridy = 1;
        dialog.add(new JLabel("Role: " + UserSession.getInstance().getRole()), gbc);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        gbc.gridy = 2;
        dialog.add(btnLogout, gbc);

        btnLogout.addActionListener(e -> {
            dialog.dispose();
            // Inside MainDashboard.java where the Logout button is clicked:
            this.dispose(); // Closes the dashboard
            UserSession.getInstance().clear(); // Clears the session data
            //Open the Welcome Portal instead of the old LoginFrame
            SwingUtilities.invokeLater(() -> {
                new WelcomeFrame().setVisible(true);
            });
        });

        dialog.setVisible(true);
    }
}
