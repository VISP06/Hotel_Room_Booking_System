package view;

import dao.BookingDAO;
import model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        JButton btnDelete = new JButton("Delete");
        topPanel.add(btnDelete);

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
            String text = txtSearch.getText().trim();
            refreshTable(text);
        });

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            refreshTable(null);
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a booking to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                    bookingDAO.deleteBooking(bookingId);
                    JOptionPane.showMessageDialog(this, "Booking deleted successfully!");
                    refreshTable(null);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void refreshTable() {
        refreshTable(null);
    }

    public void refreshTable(String keyword) {
        try {
            tableModel.setRowCount(0);
            List<Booking> bookings;
            if (keyword != null && !keyword.isEmpty()) {
                bookings = bookingDAO.searchBookings(keyword);
            } else {
                bookings = bookingDAO.getAllBookings();
            }

            for (Booking booking : bookings) {
                double displayCost = booking.getTotalCost();
                if (displayCost <= 0) {
                    long diffInMillies = Math.abs(booking.getCheckoutDate().getTime() - booking.getCheckinDate().getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diff == 0) diff = 1;
                    displayCost = diff * booking.getRoomRate();
                }

                tableModel.addRow(new Object[]{
                        booking.getId(),
                        booking.getGuestName(),
                        booking.getContact(),
                        booking.getRoomId(),
                        booking.getCheckinDate(),
                        booking.getCheckoutDate(),
                        String.format("%.2f (Est.)", displayCost)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }
}
