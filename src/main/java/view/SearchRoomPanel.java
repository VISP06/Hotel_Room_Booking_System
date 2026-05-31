package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchRoomPanel extends JPanel {
    private RoomDAO roomDAO;
    private JComboBox<String> comboType;
    private JComboBox<String> comboStatus;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchRoomPanel(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        performSearch(); // Initial load
    }

    private void initComponents() {
        // Search Header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        searchPanel.add(new JLabel("Type:"));
        comboType = new JComboBox<>(new String[]{"All", "Single", "Double", "Suite"});
        searchPanel.add(comboType);

        searchPanel.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(new String[]{"All", "Available", "Booked"});
        searchPanel.add(comboStatus);

        JButton btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Room Number", "Type", "Rate", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // Action
        btnSearch.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        try {
            tableModel.setRowCount(0);
            String selectedType = (String) comboType.getSelectedItem();
            String selectedStatus = (String) comboStatus.getSelectedItem();

            List<Room> rooms = roomDAO.getAllRooms();
            
            List<Room> filteredRooms = rooms.stream()
                .filter(r -> selectedType.equals("All") || r.getType().equalsIgnoreCase(selectedType))
                .filter(r -> selectedStatus.equals("All") || r.getStatus().equalsIgnoreCase(selectedStatus))
                .collect(Collectors.toList());

            for (Room room : filteredRooms) {
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
            JOptionPane.showMessageDialog(this, "Error searching rooms: " + e.getMessage());
        }
    }
}
