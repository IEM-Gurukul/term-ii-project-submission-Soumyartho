# Smart Library Management System with Reservation and Role-Based Access

## Problem Statement
Many small libraries manage their book records and borrowing activities manually or with simple systems. This makes it difficult to track issued books, borrowers, and due dates, leading to errors like misplaced records, incorrect fine calculations, and catalog disorganization. Furthermore, manual systems lack the ability to manage book reservations when all copies are already issued. This project proposes a Smart Library Management System that organizes these activities through a desktop application. The system allows librarians to manage books, register members, track issued books, handle reservations automatically, and dynamically calculate fines for late returns based on user roles. By digitizing these tasks, the system makes library management faster, more accurate, and easier to maintain.

## Target User
Librarians and library administrators responsible for managing book inventory, handling member registrations, and overseeing the borrowing, returning, and reservation processes of the library.

## Core Features
- Book Inventory Management: Add new books to the catalog and track available copies.
- Member Registration: Register users with different roles (Student and Faculty).
- Borrowing and Returns: Issue books to users and process returns with deadline enforcement.
- Role-Based Fine Calculation: Automatically calculate fines based on user type if a book is returned late.
- Reservation System (Queue): Allow users to reserve a book when all copies are currently issued, utilizing a FIFO queue system.
- Robust Search: Filter the library catalog by title, author, or category.
- Data Persistence: Automatically save and load data across sessions.
- Reporting: Export comprehensive library reports containing catalogs, users, active transactions, and reservations.

## OOP Concepts Used
- **Abstraction**: Implemented an abstract `User` class to define the common structure and contract for all library members without allowing direct instantiation of a generic user.
- **Inheritance**: Created `StudentMember` and `FacultyMember` classes that inherit from the abstract `User` base class, reusing common attributes such as user ID and name.
- **Polymorphism**: Overridden methods `getMaxBooksAllowed()` and `calculateFine()` in the subclass implementations. A student has a limit of 3 books and a higher daily fine, while a faculty member has a limit of 5 books and a lower fine. The system determines the behavior dynamically at runtime.
- **Encapsulation**: Used private access modifiers for the internal state of classes (like `Book` and `Transaction`) and exposed controlled access via public getter and setter methods to prevent unauthorized data mutation.

## Architecture Description
The project is designed using a layered, separation-of-concerns architecture consisting of four main layers:
- **Model Layer (`src/model`)**: Contains the core data structures and entities of the system, including `Book`, `User`, `Transaction`, and `Reservation`. 
- **Service Layer (`src/service`)**: Encapsulates the business logic of the application. The `LibraryService` class handles all computations, queue management, and rule enforcement independent of the user interface.
- **Utility Layer (`src/util`)**: Provides helper functionalities. `FileManager` facilitates object serialization for data persistence, and `DataSeeder` handles initial state population.
- **UI Layer (`src/ui`)**: Implements the graphical user interface using Java Swing. It consists of windows and dialogs (`LoginFrame`, `DashboardFrame`, `IssueBookDialog`) that interact only with the Service Layer and do not contain business logic.

## How to Run Instructions
1. Open a terminal or command prompt.
2. Navigate to the root directory of the project.
3. Compile the Java source files using the following command:
   ```bash
   javac src/model/*.java src/util/*.java src/service/*.java src/ui/*.java src/Main.java
   ```
4. Run the application using the following command:
   ```bash
   java -cp src Main
   ```

Note: The system requires Java Development Kit (JDK) installed on your machine to compile and run. Ensure the terminal is running in the project root directory where the `src` folder is located.
