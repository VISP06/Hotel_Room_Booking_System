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

        // Feature Panels
        AddRoomPanel addRoomPanel = new AddRoomPanel(roomDAO);
        BookRoomPanel bookRoomPanel = new BookRoomPanel(roomDAO, bookingDAO);
        ViewBookingsPanel viewBookingsPanel = new ViewBookingsPanel(bookingDAO);
        CheckOutPanel checkOutPanel = new CheckOutPanel(bookingDAO, roomDAO);
        SearchRoomPanel searchRoomPanel = new SearchRoomPanel(roomDAO);

        tabbedPane.addTab("Add Room", addRoomPanel);
        tabbedPane.addTab("Book Room", bookRoomPanel);
        tabbedPane.addTab("View Bookings", viewBookingsPanel);
        tabbedPane.addTab("Check-Out", checkOutPanel);
        tabbedPane.addTab("Search Availability", searchRoomPanel);

        // Refresh data when switching tabs
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            switch (index) {
                case 1: bookRoomPanel.refreshRooms(); break;
                case 2: viewBookingsPanel.refreshTable(); break;
                case 3: checkOutPanel.refreshBookings(); break;
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel(text + " (Placeholder)"));
        return panel;
    }
}
