# Hotel Room Booking System Specification

## Technology Stack & Architectural Constraints
- **Language & Framework:** Java 8+ Desktop Application using pure Java Swing (`javax.swing.*`, `java.awt.*`).
- **Database Layer:** Local MySQL instance via native JDBC driver manager (`java.sql.*`).
- **Security Rule:** Strictly use `PreparedStatement` for all transactional operations.
- **UI Design Rule:** Generate components purely through Java code using layout managers. Do not utilize external `.form` XML visual designers.

## Database Schema (CRITICAL: Database `hotel_db` already exists)
- **Rule:** Do NOT write Java code to create the database or tables. Assume they exist. Connect to `jdbc:mysql://localhost:3306/hotel_db`.
- **Table:** `rooms` (id INT PK AI, room_number VARCHAR(10), type VARCHAR(20), rate DOUBLE, status VARCHAR(20))
- **Table:** `bookings` (id INT PK AI, guest_name VARCHAR(100), contact VARCHAR(50), room_id INT FK, checkin_date DATE, checkout_date DATE, total_cost DOUBLE)

## Application Packages
- `model` -> OOP classes (Room, Booking).
- `dao` -> Database access objects (RoomDAO, BookingDAO).
- `view` -> Swing UI frames and panels.
- `util` -> Database connection helper.
- `main` -> Application entry point.

## Required Features & UI Components
The application must have a Main Window with navigation to access the following 5 core features:

1. **Add Room Panel:**
    - Input fields: Room Number, Room Type (dropdown: Single, Double, Suite), Rate Per Night, Room Status (dropdown: Available, Booked).
    - Action button to insert into the `rooms` table.

2. **Book Room Panel:**
    - Input fields: Guest Name, Contact Info, Room Selection (dropdown of only *Available* rooms), Check-in Date, Check-out Date.
    - Action button: On submission, updates room status to "Booked" and saves the booking record.

3. **View Bookings Panel:**
    - Component: A `JTable` displaying all active bookings.
    - Features: Filter by guest name, room number, or dates. Displays both guest and room information.

4. **Check-Out Room Panel:**
    - Input: Select a currently booked room.
    - Action button: Updates the room status back to "Available". Calculates `total_cost` based on stay duration (checkout_date - checkin_date) * room rate, and updates the booking record.

5. **Search Room Availability Panel:**
    - Inputs: Combo boxes to filter rooms by type, price range, and availability.
    - Component: A `JTable` displaying the search results.@