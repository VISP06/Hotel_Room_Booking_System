Hotel Room Booking System

  A professional desktop application designed for hotel staff to streamline the management of room inventory, guest bookings, and automated
  check-out billing. The system provides an intuitive interface for handling daily hotel operations efficiently.

  Screenshots

  !Main Dashboard (placeholder-link)
  !Room Search & Availability (placeholder-link)
  !Booking Management (placeholder-link)
  !Check-Out & Billing (placeholder-link)

  Tech Stack

   * Java 8+: Core programming language.
   * Java Swing: Framework for the graphical user interface.
   * JDBC: API for database connectivity.
   * MySQL: Relational database for data storage.
   * Maven: Dependency management and build tool.

  Project Structure

   1 src/main/java/
   2 ├── dao     # Data Access Objects
   3 ├── main    # App Entry Point
   4 ├── model   # Data Entity Classes
   5 ├── util    # Database Connection Utility
   6 └── view    # Swing UI Components

  Key Features

   * Add Room: Easily manage and expand room inventory.
   * Book Room: Seamless guest registration and room assignment.
   * View Bookings: Comprehensive list of bookings with built-in filtering options.
   * Check-Out: Automated billing with dynamic stay duration and cost calculation.
   * Search Room Availability: Real-time lookup for vacant rooms based on criteria.

  Installation Steps

   1. Clone the Repository:

   1     git clone https://github.com/your-username/HotelRoomBookingSystem.git

   2. Setup Database:
       * Open MySQL Workbench or your preferred MySQL client.
       * Locate the included .sql dump file in the project root.
       * Execute the script to create the necessary database schema and tables.

   3. Import Project:
       * Open your IDE (IntelliJ IDEA or Eclipse).
       * Import the project as a Maven Project.
   4. Configure Database Connection:
       * Crucial: Navigate to src/main/java/util/DBConnection.java.
       * Update the username and password variables to match your local MySQL configuration.

   5. Run Application:
       * Locate src/main/java/main/Main.java.
       * Right-click and select Run 'Main.main()'.
