package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.Room;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class BookRoomPanel extends JPanel {
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;

    private JTextField txtGuestName;
    private JTextField txtContact;
    private JComboBox<Room> comboRooms;
    private JTextField txtCheckIn;
    private JTextField txtCheckOut;

    public BookRoomPanel(RoomDAO roomDAO, BookingDAO bookingDAO) {
        this.roomDAO = roomDAO;
        this.bookingDAO = bookingDAO;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshRooms();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Guest Name:"), gbc);
        txtGuestName = new JTextField(20);
        ((AbstractDocument) txtGuestName.getDocument()).setDocumentFilter(new AlphaSpaceFilter());
        gbc.gridx = 1;
        formPanel.add(txtGuestName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Contact Info:"), gbc);
        txtContact = new JTextField(20);
        ((AbstractDocument) txtContact.getDocument()).setDocumentFilter(new NumericLimitFilter(10));
        gbc.gridx = 1;
        formPanel.add(txtContact, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Select Room:"), gbc);
        comboRooms = new JComboBox<>();
        comboRooms.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Room) {
                    Room room = (Room) value;
                    setText(String.format("Room %s - %s (Rs. %.0f/night)", room.getRoomNumber(), room.getType(), room.getRate()));
                }
                return this;
            }
        });
        gbc.gridx = 1;
        formPanel.add(comboRooms, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Check-in (YYYY-MM-DD):"), gbc);
        txtCheckIn = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(txtCheckIn, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Check-out (YYYY-MM-DD):"), gbc);
        txtCheckOut = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(txtCheckOut, gbc);

        JButton btnBook = new JButton("Book Room");
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnBook, gbc);

        add(formPanel, BorderLayout.NORTH);

        btnBook.addActionListener(e -> {
            try {
                String guestName = txtGuestName.getText().trim();
                String contact = txtContact.getText().trim();
                String checkInStr = txtCheckIn.getText().trim();
                String checkOutStr = txtCheckOut.getText().trim();

                if (guestName.isEmpty() || contact.isEmpty() || checkInStr.isEmpty() || checkOutStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (contact.length() != 10) {
                    JOptionPane.showMessageDialog(this, "Contact Info must be exactly 10 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Room selectedRoom = (Room) comboRooms.getSelectedItem();
                Date checkIn = Date.valueOf(checkInStr);
                Date checkOut = Date.valueOf(checkOutStr);

                if (checkIn.after(checkOut)) {
                    JOptionPane.showMessageDialog(this, "Check-in date cannot be after the Check-out date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedRoom == null) {
                    JOptionPane.showMessageDialog(this, "Please select a room.");
                    return;
                }

                Booking booking = new Booking(0, guestName, contact, selectedRoom.getId(), checkIn, checkOut, 0.0);
                bookingDAO.addBooking(booking);

                JOptionPane.showMessageDialog(this, "Booking successful!");
                clearFields();
                refreshRooms();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error booking room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void clearFields() {
        txtGuestName.setText("");
        txtContact.setText("");
        txtCheckIn.setText("");
        txtCheckOut.setText("");
        if (comboRooms.getItemCount() > 0) comboRooms.setSelectedIndex(0);
    }

    public void refreshRooms() {
        try {
            comboRooms.removeAllItems();
            List<Room> rooms = roomDAO.getAvailableRooms();
            for (Room room : rooms) {
                comboRooms.addItem(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Properly nested helper classes
    private static class AlphaSpaceFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[a-zA-Z ]*")) super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[a-zA-Z ]*")) super.replace(fb, offset, length, text, attrs);
        }
    }

    private static class NumericLimitFilter extends DocumentFilter {
        private final int limit;
        public NumericLimitFilter(int limit) { this.limit = limit; }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*") && (fb.getDocument().getLength() + string.length()) <= limit) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d*") && (fb.getDocument().getLength() - length + text.length()) <= limit) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
