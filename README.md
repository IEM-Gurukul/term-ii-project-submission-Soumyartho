[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/pG3gvzt-)
# PCCCS495 – Term II Project

## Project Title
**Smart Library Management System**
---

## Problem Statement (max 150 words)
Managing a library's books, users, and transactions manually is error-prone, time-consuming, and difficult to scale. Librarians struggle to track available books, borrowed items, due dates, overdue fines, and reservations without software support. This project provides a robust Java desktop application using Swing to automate these core library operations. It enables efficient cataloging, role-based user registration (students and faculty), transaction processing with fine calculation, and a FIFO reservation queue for unavailable books. The system ensures data persistence through serialization and features a background auto-save mechanism, offering a complete, object-oriented solution to modern library management challenges.
---

## Target User
Librarians, university administrators, students, and faculty members who need a reliable system to track book availability, manage borrowing limits, and handle reservations seamlessly.
---

## Core Features

- **Book Catalog & User Management:** Add books with total copy tracking and register members as Students or Faculty with distinct borrowing limits.
- **Issue & Return Processing:** Automate book lending, enforce maximum borrow limits, calculate late fines based on user roles, and track active transactions.
- **Reservation System (FIFO):** Allow users to reserve unavailable books, queueing requests and automatically notifying them when the book is returned.
- **Data Persistence & Reporting:** Automatically save all data to disk via a background thread, and export comprehensive system reports containing catalogs, users, transactions, and reservations.

---

## OOP Concepts Used

- **Abstraction:** The `User` abstract class defines the shared template, forcing implementation of `getMaxBooksAllowed()` and `calculateFine()` in subclasses.
- **Inheritance:** `StudentMember` and `FacultyMember` extend the abstract `User` class to inherit common attributes while providing specific borrowing rules.
- **Polymorphism:** `LibraryService` calculates fines dynamically based on the actual subclass type (e.g., student vs. faculty rates) using a common `User` reference.
- **Exception Handling:** Custom checked exceptions (`BookUnavailableException`, `UserLimitExceededException`) handle specific business logic violations gracefully.
- **Collections / Threads:** Uses `HashMap` (O(1) lookups), `ArrayList` (ordered logging), and `LinkedList` (FIFO queue for reservations). A daemon `Thread` runs continuously in the background to auto-save data every 60 seconds without freezing the GUI.

---

## Proposed Architecture Description
The application follows a clean **Model-Service-UI** architecture:
1. **Model Layer (`model` package):** Defines the core entities—`Book`, abstract `User` (and concrete `StudentMember`, `FacultyMember`), `Transaction`, and `Reservation`. All data structures are `Serializable` for easy persistence.
2. **Service Layer (`service` package):** The `LibraryService` acts as the central engine containing the business logic (issuing, returning, reserving, finding books). It delegates persistence tasks to the `FileManager`.
3. **UI Layer (`ui` package):** Built with Java Swing, featuring a main `DashboardFrame` that observes the service layer, displaying data in a `JTable` and accepting user input via dedicated modal `JDialog`s (e.g., `AddBookDialog`, `IssueBookDialog`).
4. **Utility Layer (`util` package):** Contains helpers for Object stream I/O (`FileManager`) and initial sample data generation (`DataSeeder`).

---

## How to Run
1. Ensure you have the Java Development Kit (JDK 8 or higher) installed.
2. Compile the source code from the `src` directory:
   `javac -d out src/**/*.java`
3. Run the application from the output directory:
   `java -cp out Main`
4. On the first launch, the system will automatically seed demo data. Click "Enter System" on the login screen to access the Dashboard.

---

## Git Discipline Notes
Minimum 10 meaningful commits required. *(Note: This project contains 13 logical commits demonstrating incremental, phased development from setup to advanced features).*
