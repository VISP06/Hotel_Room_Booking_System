package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
                String roomNumber = txtRoomNumber.getText();
                String type = (String) comboType.getSelectedItem();
                double rate = Double.parseDouble(txtRate.getText());
                String status = (String) comboStatus.getSelectedItem();

                Room room = new Room(0, roomNumber, type, rate, status);
                roomDAO.addRoom(room);

                JOptionPane.showMessageDialog(this, "Room added successfully!");
                clearFields();
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid rate.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
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
