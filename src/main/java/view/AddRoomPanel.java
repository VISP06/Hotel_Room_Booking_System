package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AddRoomPanel extends JPanel {
    private RoomDAO roomDAO;
    private JTextField txtRoomNumber;
    private JComboBox<String> comboType;
    private JTextField txtRate;
    private JComboBox<String> comboStatus;
    private JTable roomTable;
    private DefaultTableModel tableModel;

    public AddRoomPanel(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:"), gbc);
        txtRoomNumber = new JTextField(15);
        ((AbstractDocument) txtRoomNumber.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        gbc.gridx = 1;
        formPanel.add(txtRoomNumber, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Type:"), gbc);
        comboType = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        gbc.gridx = 1;
        formPanel.add(comboType, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Rate:"), gbc);
        txtRate = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtRate, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Status:"), gbc);
        comboStatus = new JComboBox<>(new String[]{"Available", "Booked"});
        gbc.gridx = 1;
        formPanel.add(comboStatus, gbc);

        JButton btnAdd = new JButton("Add Room");
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnAdd, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID", "Room Number", "Type", "Rate", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);
        add(new JScrollPane(roomTable), BorderLayout.CENTER);

        // Action Listener
        btnAdd.addActionListener(e -> {
            try {
                String roomNumber = txtRoomNumber.getText().trim();
                if (roomNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Room Number is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Duplicate Room Check
                if (roomDAO.isRoomNumberExists(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Room Number already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String type = (String) comboType.getSelectedItem();
                
                String rateText = txtRate.getText().trim();
                if (rateText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nightly Rate is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!rateText.matches("\\d+(\\.\\d+)?")) {
                    JOptionPane.showMessageDialog(this, "Nightly Rate must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double rate = Double.parseDouble(rateText);
                
                if (rate <= 0) {
                    JOptionPane.showMessageDialog(this, "Nightly Rate must be greater than zero.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String status = (String) comboStatus.getSelectedItem();

                Room room = new Room(0, roomNumber, type, rate, status);
                roomDAO.addRoom(room);

                JOptionPane.showMessageDialog(this, "Room added successfully!");
                clearFields();
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d+")) super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d+")) super.replace(fb, offset, length, text, attrs);
        }
    }

    private void clearFields() {
        txtRoomNumber.setText("");
        txtRate.setText("");
        comboType.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Room> rooms = roomDAO.getAllRooms();
            for (Room room : rooms) {
                tableModel.addRow(new Object[]{
                        room.getId(),
                        room.getRoomNumber(),
                        room.getType(),
                        room.getRate(),
                        room.getStatus()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
