package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.Room;

import javax.swing.*;
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
        gbc.gridx = 1;
        formPanel.add(txtGuestName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Contact Info:"), gbc);
        txtContact = new JTextField(20);
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
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnBook, gbc);

        add(formPanel, BorderLayout.NORTH);

        btnBook.addActionListener(e -> {
            try {
                String guestName = txtGuestName.getText();
                String contact = txtContact.getText();
                
                // Contact Validation
                if (!contact.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Contact Info must only contain numeric digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Room selectedRoom = (Room) comboRooms.getSelectedItem();
                Date checkIn = Date.valueOf(txtCheckIn.getText());
                Date checkOut = Date.valueOf(txtCheckOut.getText());

                // Date Validation
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
}
