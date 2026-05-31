package view;

import dao.BookingDAO;
import dao.RoomDAO;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;

    public MainDashboard() {
        this.roomDAO = new RoomDAO();
        this.bookingDAO = new BookingDAO();

        setTitle("Hotel Room Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Placeholder Panels
        tabbedPane.addTab("Add Room", createPlaceholderPanel("Add Room Panel"));
        tabbedPane.addTab("Book Room", createPlaceholderPanel("Book Room Panel"));
        tabbedPane.addTab("View Bookings", createPlaceholderPanel("View Bookings Panel"));
        tabbedPane.addTab("Check-Out", createPlaceholderPanel("Check-Out Room Panel"));
        tabbedPane.addTab("Search Availability", createPlaceholderPanel("Search Room Availability Panel"));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel(text + " (Placeholder)"));
        return panel;
    }
}
