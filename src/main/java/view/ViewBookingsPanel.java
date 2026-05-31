package view;

import dao.BookingDAO;
import model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ViewBookingsPanel extends JPanel {
    private BookingDAO bookingDAO;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private TableRowSorter<DefaultTableModel> sorter;

    public ViewBookingsPanel(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Top Panel for Filtering
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search (Guest/Room ID):"));
        txtSearch = new JTextField(20);
        topPanel.add(txtSearch);
        
        JButton btnFilter = new JButton("Filter");
        topPanel.add(btnFilter);
        
        JButton btnRefresh = new JButton("Refresh");
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Booking ID", "Guest Name", "Contact", "Room ID", "Check-in", "Check-out", "Total Cost"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookingTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        bookingTable.setRowSorter(sorter);
        
        add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        // Action Listeners
        btnFilter.addActionListener(e -> {
            String text = txtSearch.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());
    }

    public void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Booking> bookings = bookingDAO.getAllBookings();
            for (Booking booking : bookings) {
                tableModel.addRow(new Object[]{
                        booking.getId(),
                        booking.getGuestName(),
                        booking.getContact(),
                        booking.getRoomId(),
                        booking.getCheckinDate(),
                        booking.getCheckoutDate(),
                        booking.getTotalCost()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }
}
