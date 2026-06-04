package dao;

import model.Booking;
import model.Room;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookingDAO {
    private RoomDAO roomDAO = new RoomDAO();

    public void addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (guest_name, contact, room_id, checkin_date, checkout_date, total_cost) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, booking.getGuestName());
            pstmt.setString(2, booking.getContact());
            pstmt.setInt(3, booking.getRoomId());
            pstmt.setDate(4, booking.getCheckinDate());
            pstmt.setDate(5, booking.getCheckoutDate());
            pstmt.setDouble(6, booking.getTotalCost());
            pstmt.executeUpdate();
            
            // Update room status to 'Booked'
            roomDAO.updateRoomStatus(booking.getRoomId(), "Booked");
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.rate FROM bookings b JOIN rooms r ON b.room_id = r.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                booking.setRoomRate(rs.getDouble("rate"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public List<Booking> searchBookings(String keyword) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.rate FROM bookings b JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.guest_name LIKE ? OR b.room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            
            int roomId = -1;
            try {
                roomId = Integer.parseInt(keyword);
            } catch (NumberFormatException e) {
                // Not a number, so room_id filter won't match anyway
            }
            pstmt.setInt(2, roomId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setRoomRate(rs.getDouble("rate"));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    public double calculateCheckoutCost(int bookingId) throws SQLException {
        String sql = "SELECT b.id, b.room_id, b.checkin_date, b.checkout_date, r.rate " +
                     "FROM bookings b JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.id = ?";
        
        double totalCost = 0;
        int roomId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                    Date checkin = rs.getDate("checkin_date");
                    Date checkout = rs.getDate("checkout_date");
                    double rate = rs.getDouble("rate");

                    long diffInMillies = Math.abs(checkout.getTime() - checkin.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    
                    if (diff == 0) diff = 1; // Minimum 1 day charge
                    
                    totalCost = diff * rate;

                    // Update booking with total cost
                    updateBookingCost(bookingId, totalCost);
                    
                    // Update room status back to 'Available'
                    roomDAO.updateRoomStatus(roomId, "Available");
                }
            }
        }
        return totalCost;
    }

    public void deleteBooking(int bookingId) throws SQLException {
        String getRoomSql = "SELECT room_id FROM bookings WHERE id = ?";
        String deleteSql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            int roomId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(getRoomSql)) {
                pstmt.setInt(1, bookingId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        roomId = rs.getInt("room_id");
                    }
                }
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, bookingId);
                pstmt.executeUpdate();
            }
            
            if (roomId != -1) {
                roomDAO.updateRoomStatus(roomId, "Available");
            }
        }
    }

    private void updateBookingCost(int bookingId, double totalCost) throws SQLException {
        String sql = "UPDATE bookings SET total_cost = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, totalCost);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        }
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setGuestName(rs.getString("guest_name"));
        booking.setContact(rs.getString("contact"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setCheckinDate(rs.getDate("checkin_date"));
        booking.setCheckoutDate(rs.getDate("checkout_date"));
        booking.setTotalCost(rs.getDouble("total_cost"));
        return booking;
    }
}
