package model;

public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double rate;
    private String status;

    public Room() {
    }

    public Room(int id, String roomNumber, String type, double rate, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.rate = rate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", rate=" + rate +
                ", status='" + status + '\'' +
                '}';
    }
}
