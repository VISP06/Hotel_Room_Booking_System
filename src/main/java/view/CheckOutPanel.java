package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CheckOutPanel extends JPanel {
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private JComboBox<BookingWrapper> comboBookings;

    public CheckOutPanel(BookingDAO bookingDAO, RoomDAO roomDAO) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshBookings();
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Select Active Booking:"), gbc);

        comboBookings = new JComboBox<>();
        gbc.gridx = 1;
        centerPanel.add(comboBookings, gbc);

        JButton btnCheckOut = new JButton("Check-Out & Generate Receipt");
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(btnCheckOut, gbc);

        add(centerPanel, BorderLayout.CENTER);

        btnCheckOut.addActionListener(e -> {
            BookingWrapper selected = (BookingWrapper) comboBookings.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a booking.");
                return;
            }

            try {
                double totalCost = bookingDAO.calculateCheckoutCost(selected.booking.getId());
                
                String receipt = String.format(
                    "--- CHECK-OUT RECEIPT ---\n" +
                    "Guest: %s\n" +
                    "Room ID: %d\n" +
                    "Check-in: %s\n" +
                    "Check-out: %s\n" +
                    "-------------------------\n" +
                    "Total Cost: $%.2f\n" +
                    "-------------------------",
                    selected.booking.getGuestName(),
                    selected.booking.getRoomId(),
                    selected.booking.getCheckinDate(),
                    selected.booking.getCheckoutDate(),
                    totalCost
                );

                JOptionPane.showMessageDialog(this, receipt, "Checkout Success", JOptionPane.INFORMATION_MESSAGE);
                refreshBookings();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error during checkout: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void refreshBookings() {
        try {
            comboBookings.removeAllItems();
            List<Booking> bookings = bookingDAO.getAllBookings();
            // Only show bookings that haven't been charged yet (totalCost == 0)
            List<Booking> activeBookings = bookings.stream()
                    .filter(b -> b.getTotalCost() == 0)
                    .collect(Collectors.toList());
            
            for (Booking b : activeBookings) {
                comboBookings.addItem(new BookingWrapper(b));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Wrapper to control how Booking is displayed in JComboBox
    private static class BookingWrapper {
        Booking booking;
        BookingWrapper(Booking booking) { this.booking = booking; }
        @Override
        public String toString() {
            return "ID: " + booking.getId() + " - " + booking.getGuestName() + " (Room: " + booking.getRoomId() + ")";
        }
    }
}
