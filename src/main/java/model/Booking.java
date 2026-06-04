package model;

import java.sql.Date;

public class Booking {
    private int id;
    private String guestName;
    private String contact;
    private int roomId;
    private double roomRate;
    private Date checkinDate;
    private Date checkoutDate;
    private double totalCost;

    public Booking() {
    }

    public Booking(int id, String guestName, String contact, int roomId, Date checkinDate, Date checkoutDate, double totalCost) {
        this.id = id;
        this.guestName = guestName;
        this.contact = contact;
        this.roomId = roomId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public double getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", guestName='" + guestName + '\'' +
                ", contact='" + contact + '\'' +
                ", roomId=" + roomId +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", totalCost=" + totalCost +
                '}';
    }
}
