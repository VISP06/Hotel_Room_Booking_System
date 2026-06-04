# 🏨 Hotel Room Booking System

A modern desktop application built with **Java Swing** and **MySQL** that helps hotel staff efficiently manage room inventory, guest reservations, and billing operations. The system provides an intuitive interface for handling daily hotel management tasks with speed and accuracy.

---

## 📸 Screenshots

| Dashboard                      | Room Search                      |
| ------------------------------ | -------------------------------- |
| ![Dashboard](placeholder-link) | ![Room Search](placeholder-link) |

| Booking Management                      | Check-Out & Billing            |
| --------------------------------------- | ------------------------------ |
| ![Booking Management](placeholder-link) | ![Check-Out](placeholder-link) |

---

## ✨ Features

### 🏠 Room Management

* Add new rooms to the inventory
* Manage room details and availability
* Track occupied and vacant rooms

### 📅 Booking Management

* Register guest bookings quickly
* Assign rooms seamlessly
* Store and manage booking information

### 🔍 Room Availability Search

* Search available rooms in real time
* Filter rooms based on requirements
* Instantly view room status

### 📋 Booking Records

* View all active and completed bookings
* Easily track guest information
* Access booking history when needed

### 💳 Automated Check-Out & Billing

* Calculate total stay duration automatically
* Generate billing information instantly
* Simplify the guest check-out process

---

## 🛠️ Tech Stack

| Technology | Purpose                                  |
| ---------- | ---------------------------------------- |
| Java 8+    | Core programming language                |
| Java Swing | Desktop GUI development                  |
| JDBC       | Database connectivity                    |
| MySQL      | Relational database management           |
| Maven      | Dependency management & build automation |

---

## 📁 Project Structure

```text
src/main/java/
├── dao/       # Data Access Objects
├── main/      # Application Entry Point
├── model/     # Entity Classes
├── util/      # Database Utility Classes
└── view/      # Swing User Interface Components
```

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/HotelRoomBookingSystem.git
cd HotelRoomBookingSystem
```

### 2️⃣ Set Up the Database

1. Open MySQL Workbench (or your preferred MySQL client).
2. Locate the provided `.sql` file:

```text
database/hotel_booking_db.sql
```

3. Execute the script to create the required database and tables.

### 3️⃣ Import the Project

* Open **IntelliJ IDEA** or **Eclipse**
* Import the project as a **Maven Project**

### 4️⃣ Configure Database Credentials

Navigate to:

```text
src/main/java/util/DBConnection.java
```

Update the database configuration:

```java
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

### 5️⃣ Run the Application

Locate:

```text
src/main/java/main/Main.java
```

Run:

```java
Main.main()
```

---

## 🎯 Core Modules

* Room Inventory Management
* Guest Reservation System
* Room Availability Search
* Booking Records Management
* Automated Billing & Check-Out

## 📄 License

This project is intended for educational and learning purposes. Feel free to modify and extend it according to your requirements.
