# PCCCS495 – Object-Oriented Programming with Java

# Term-II Project Report

---

**Course Name:** PCCCS495 – Object-Oriented Programming with Java

**Project Title:** Smart Library Management System

**Student Name:** Soumyartho

**Roll Number:** [Your Roll Number]

**Instructor Name:** [Instructor Name]

**Semester:** Spring 2026

---

## 1. Problem Statement

Managing a library's books, users, and transactions manually is error-prone, time-consuming, and does not scale well beyond a small collection. Librarians must track which books are available, who has borrowed them, when they are due, and whether overdue fines apply — all of which are difficult to handle without software support.

This project, **Smart Library Management System**, is a desktop application built entirely in Java using Swing for the graphical user interface. It automates the core operations of a small-to-medium academic library: cataloging books, registering users (students and faculty), issuing and returning books with due-date tracking, calculating role-based fines, reserving unavailable books via a FIFO queue, and exporting comprehensive reports. Data is persisted to disk using Java Object Serialization, and a background daemon thread performs automatic saves every 60 seconds to prevent data loss.

The system is designed to demonstrate real-world usage of Object-Oriented Programming principles — including abstraction, inheritance, polymorphism, encapsulation, exception handling, collections, and multi-threading — within a functional, user-facing application.

---

## 2. System Design

### 2.1 UML Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         <<Main Entry Point>>                            │
│                              Main                                       │
│  ─ Creates LibraryService, starts AutoSave Thread, launches LoginFrame  │
└────────────────────────────────┬────────────────────────────────────────┘
                                 │ uses
                                 ▼
┌──────────────────────────────────────────────┐
│            LibraryService                     │
│───────────────────────────────────────────────│
│ - books : HashMap<String, Book>               │
│ - users : HashMap<String, User>               │
│ - transactions : ArrayList<Transaction>       │
│ - reservations : LinkedList<Reservation>      │
│───────────────────────────────────────────────│
│ + addBook(Book)                               │
│ + registerUser(User)                          │
│ + issueBookGUI(bookId, userId) : String       │
│ + returnBookGUI(bookId, userId) : String      │
│ + reserveBook(bookId, userId) : String        │
│ + exportReport(filepath) : String             │
│ + saveData() / loadData()                     │
│ + searchBookByTitle/Author/Category()         │
└──────────┬───────────────────────────────────┘
           │ manages
           ▼
┌──────────────────────┐   ┌─────────────────────────────┐
│   <<Serializable>>   │   │     <<abstract>>             │
│       Book           │   │        User                  │
│──────────────────────│   │─────────────────────────────│
│ - bookId : String    │   │ # userId : String            │
│ - title : String     │   │ # name : String              │
│ - author : String    │   │─────────────────────────────│
│ - category : String  │   │ + getMaxBooksAllowed() : int │
│ - totalCopies : int  │   │ + calculateFine(int) : double│
│ - availableCopies    │   └──────────┬──────────────────┘
│──────────────────────│              │ extends
│ + borrowCopy()       │      ┌───────┴────────┐
│ + returnCopy()       │      │                │
│ + isAvailable()      │      ▼                ▼
└──────────────────────┘ ┌──────────────┐ ┌──────────────┐
                         │StudentMember │ │FacultyMember │
                         │──────────────│ │──────────────│
                         │maxBooks = 3  │ │maxBooks = 5  │
                         │fine = ₹5/day │ │fine = ₹2/day │
                         └──────────────┘ └──────────────┘

┌──────────────────────────┐   ┌─────────────────────────┐
│   <<Serializable>>       │   │   <<Serializable>>      │
│     Transaction          │   │     Reservation         │
│──────────────────────────│   │─────────────────────────│
│ - transactionId : String │   │ - reservationId : String│
│ - bookId : String        │   │ - bookId : String       │
│ - userId : String        │   │ - userId : String       │
│ - issueDate : LocalDate  │   │ - reservedDate: LocalDate│
│ - dueDate : LocalDate    │   │ - fulfilled : boolean   │
│ - returnDate : LocalDate │   │─────────────────────────│
│──────────────────────────│   │ + isFulfilled() : bool  │
│ + isActive() : boolean   │   │ + setFulfilled(bool)    │
└──────────────────────────┘   └─────────────────────────┘

┌─────────────────────────────────┐
│   <<Exceptions>>                │
│  BookUnavailableException       │
│  UserLimitExceededException     │
│  (both extend Exception)       │
└─────────────────────────────────┘

┌───────────────────────────┐   ┌────────────────────────┐
│       FileManager         │   │      DataSeeder        │
│───────────────────────────│   │────────────────────────│
│ + saveObject(Object, file)│   │ + seed(LibraryService) │
│ + loadObject(file) : Object│  │  (adds sample data)   │
└───────────────────────────┘   └────────────────────────┘

                    << UI Layer (Swing) >>
┌────────────────────────────────────────────────────────┐
│  LoginFrame ──▶ DashboardFrame                          │
│                    ├── AddBookDialog                     │
│                    ├── RegisterUserDialog                │
│                    ├── IssueBookDialog                   │
│                    └── ReturnBookDialog                  │
└────────────────────────────────────────────────────────┘
```

### 2.2 Description of Major Classes

| Class | Package | Responsibility |
|-------|---------|---------------|
| **Book** | `model` | Represents a library book with ID, title, author, category, and copy tracking. Provides `borrowCopy()`, `returnCopy()`, and `isAvailable()` methods. Implements `Serializable`. |
| **User** | `model` | Abstract base class for library members. Declares abstract methods `getMaxBooksAllowed()` and `calculateFine()` to enforce role-specific behavior. |
| **StudentMember** | `model` | Extends `User`. Students can borrow up to 3 books and are fined ₹5 per day for late returns. |
| **FacultyMember** | `model` | Extends `User`. Faculty can borrow up to 5 books and are fined ₹2 per day for late returns. |
| **Transaction** | `model` | Records a borrow event — tracks book ID, user ID, issue/due/return dates. `isActive()` returns true if the book has not been returned. |
| **Reservation** | `model` | Represents a queue-based reservation for an unavailable book, with a fulfilled flag. |
| **LibraryService** | `service` | Central business-logic class. Manages all books, users, transactions, and reservations using `HashMap`, `ArrayList`, and `LinkedList`. Handles issuing, returning, reserving, searching, report export, and persistence. |
| **BookUnavailableException** | `service` | Custom checked exception thrown when a requested book has no available copies. |
| **UserLimitExceededException** | `service` | Custom checked exception thrown when a user exceeds their borrowing limit. |
| **FileManager** | `util` | Utility class for serializing/deserializing objects to/from `.dat` files using `ObjectOutputStream` and `ObjectInputStream`. |
| **DataSeeder** | `util` | Seeds the system with 8 sample books, 4 users, and 3 transactions on first launch. |
| **LoginFrame** | `ui` | Welcome screen with a styled "Enter System" button. Launches `DashboardFrame`. |
| **DashboardFrame** | `ui` | Main control panel with a book catalog table (JTable), sidebar action buttons, and a status bar. |
| **AddBookDialog** | `ui` | Modal dialog for adding a new book with input validation. |
| **RegisterUserDialog** | `ui` | Modal dialog for registering a new user (Student or Faculty) with a dropdown. |
| **IssueBookDialog** | `ui` | Modal dialog for issuing a book; offers reservation if unavailable. |
| **ReturnBookDialog** | `ui` | Modal dialog for returning a book; calculates and displays fines if overdue. |

### 2.3 Package Structure

```
src/
├── Main.java                          (Entry point)
├── model/                             (Data models)
│   ├── Book.java
│   ├── User.java                      (Abstract)
│   ├── StudentMember.java
│   ├── FacultyMember.java
│   ├── Transaction.java
│   └── Reservation.java
├── service/                           (Business logic)
│   ├── LibraryService.java
│   ├── BookUnavailableException.java
│   └── UserLimitExceededException.java
├── ui/                                (Swing GUI)
│   ├── LoginFrame.java
│   ├── DashboardFrame.java
│   ├── AddBookDialog.java
│   ├── IssueBookDialog.java
│   ├── ReturnBookDialog.java
│   └── RegisterUserDialog.java
└── util/                              (Utilities)
    ├── FileManager.java
    └── DataSeeder.java

tests/
└── LibraryServiceTest.java            (JUnit test suite)

data/                                  (Serialized .dat files — runtime)
reports/                               (Exported text reports — runtime)
```

### 2.4 Interaction Flow

1. **Startup:** `Main.java` creates a `LibraryService` instance and loads persisted data from disk. If the library is empty (first launch), `DataSeeder` populates sample data.
2. **Auto-Save Thread:** A daemon `Thread` is started that saves all data to disk every 60 seconds in the background.
3. **Login Screen:** `LoginFrame` is displayed on Swing's Event Dispatch Thread. The user clicks "Enter System" to proceed.
4. **Dashboard:** `DashboardFrame` renders the book catalog in a read-only `JTable` and provides sidebar buttons: Add Book, Register User, Issue Book, Return Book, Search Books, Export Report, Refresh, Save & Exit.
5. **Dialogs:** Each action opens a modal `JDialog` that collects input, validates it, and delegates to `LibraryService` methods that return result strings (e.g., `"SUCCESS:..."`, `"ERROR:..."`, `"RESERVE:..."`).
6. **Reservation Flow:** If a book is unavailable during issue, the system offers to queue a reservation. When the book is returned, the next user in the queue is notified.
7. **Exit:** On "Save & Exit," all data is serialized to the `data/` directory.

---

## 3. OOP Concepts Demonstrated

### 3.1 Abstraction

**Where it is used:** The `User` class in `model/User.java`.

**Why it is used:** Different user types (Student, Faculty) have different borrowing limits and fine rates. By making `User` abstract, we define a common interface without committing to a specific implementation, forcing subclasses to provide their own rules.

```java
public abstract class User implements Serializable {
    protected String userId;
    protected String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public abstract int getMaxBooksAllowed();
    public abstract double calculateFine(int daysLate);
}
```

**Explanation:** The `User` class cannot be instantiated directly. It declares two abstract methods — `getMaxBooksAllowed()` and `calculateFine()` — that each subclass *must* override, ensuring every user type enforces its own borrowing policy. This separates "what a user must do" from "how each type does it."

---

### 3.2 Inheritance

**Where it is used:** `StudentMember` and `FacultyMember` both extend `User`.

**Why it is used:** Students and faculty share common attributes (ID, name) and behaviors (toString), but differ in borrowing limit and fine calculation. Inheritance avoids code duplication by reusing the `User` base class.

```java
public class StudentMember extends User {
    public StudentMember(String userId, String name) {
        super(userId, name);
    }

    @Override
    public int getMaxBooksAllowed() { return 3; }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 5.0; // ₹5 per day
    }
}
```

**Explanation:** `StudentMember` inherits `userId`, `name`, getters, and `toString()` from `User` via `super(userId, name)`. It only overrides the abstract methods with student-specific rules (max 3 books, ₹5/day fine). `FacultyMember` follows the same pattern with its own values (max 5 books, ₹2/day).

---

### 3.3 Polymorphism

**Where it is used:** In `LibraryService.returnBookGUI()` and the `DashboardFrame` user-type display.

**Why it is used:** The service layer works with `User` references without knowing whether the user is a Student or Faculty. At runtime, Java calls the correct overridden method — this is *runtime polymorphism*.

```java
// In LibraryService.returnBookGUI():
User user = users.get(userId);
if (returnDate.isAfter(t.getDueDate())) {
    long daysLate = ChronoUnit.DAYS.between(
        t.getDueDate(), returnDate);
    double fine = user.calculateFine((int) daysLate);
    // Calls StudentMember or FacultyMember version
}
```

**Explanation:** The variable `user` is declared as type `User` (the abstract parent). When `calculateFine()` is invoked, Java dynamically dispatches to the correct subclass implementation. If `user` is a `StudentMember`, it computes `daysLate * 5.0`; if a `FacultyMember`, it computes `daysLate * 2.0` — all without any `instanceof` checks.

---

### 3.4 Encapsulation

**Where it is used:** The `Book` class in `model/Book.java`.

**Why it is used:** Fields like `availableCopies` should not be modified arbitrarily by external code. Encapsulation guards the internal state and exposes controlled mutation methods.

```java
public class Book implements Serializable {
    private String bookId;
    private int totalCopies;
    private int availableCopies;

    public void borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }
}
```

**Explanation:** All fields are `private`. The `borrowCopy()` method contains a guard (`availableCopies > 0`) to prevent negative counts, and `returnCopy()` ensures copies never exceed the total. External classes interact only through the public API, making it impossible to put the object into an inconsistent state.

---

### 3.5 Custom Exception Handling

**Where it is used:** `BookUnavailableException` and `UserLimitExceededException` in the `service` package.

**Why it is used:** Standard exceptions like `IllegalArgumentException` do not convey domain-specific semantics. Custom exceptions make error handling self-documenting and allow the caller to handle specific failure scenarios differently.

```java
public class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }
}

public class UserLimitExceededException extends Exception {
    public UserLimitExceededException(String message) {
        super(message);
    }
}
```

**Explanation:** Both extend `Exception` (checked exceptions), meaning any method throwing them must declare it in the signature or catch it. `BookUnavailableException` is semantically linked to the scenario where all copies are borrowed. `UserLimitExceededException` signals that a user has reached their maximum borrow count.

---

### 3.6 Collections Framework

**Where it is used:** `LibraryService.java` — the central data store.

**Why it is used:** Different data structures serve different access patterns: `HashMap` for O(1) lookup by ID, `ArrayList` for ordered transaction history, and `LinkedList` for FIFO reservation queue processing.

```java
public class LibraryService {
    private HashMap<String, Book> books = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private LinkedList<Reservation> reservations = new LinkedList<>();
    // ...
}
```

**Explanation:** `HashMap<String, Book>` enables direct book/user lookup by ID. `ArrayList<Transaction>` maintains insertion-order history and supports iteration. `LinkedList<Reservation>` functions as a FIFO queue — the first user to reserve a returned book gets priority, matching real-world library reservation behavior.

---

### 3.7 Multi-Threading

**Where it is used:** `Main.java` — the auto-save daemon thread.

**Why it is used:** The Swing GUI runs on the Event Dispatch Thread. To periodically save data without freezing the UI, a separate background thread is used.

```java
Thread autoSaveThread = new Thread(() -> {
    while (true) {
        try {
            Thread.sleep(60000); // 60 seconds
            library.saveData();
            System.out.println("[AutoSave] Data saved.");
        } catch (InterruptedException e) {
            System.out.println("[AutoSave] Stopped.");
            break;
        }
    }
});
autoSaveThread.setDaemon(true);
autoSaveThread.start();
```

**Explanation:** A lambda-based `Runnable` is passed to a `Thread`. `setDaemon(true)` ensures the thread terminates automatically when the JVM exits (i.e., when the user closes the application), preventing the program from hanging. This demonstrates practical concurrent programming in a desktop application.

---

### 3.8 Serialization (File I/O)

**Where it is used:** `FileManager.java` in the `util` package.

**Why it is used:** Library data must persist between application runs. Java's `ObjectOutputStream` / `ObjectInputStream` serialize entire object graphs to files without manual parsing.

```java
public class FileManager {
    public static void saveObject(Object data, String filename) {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(filename))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadObject(String filename) {
        File file = new File(filename);
        if (!file.exists()) return null;
        try (ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(file))) {
            return in.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
```

**Explanation:** `saveObject()` uses try-with-resources (auto-close) to write any `Serializable` object to a binary `.dat` file. `loadObject()` reads it back. Because `Book`, `User`, `Transaction`, and `Reservation` all implement `Serializable`, the entire data store is persisted with 4 file writes in `LibraryService.saveData()`.

---

## 4. Implementation Highlights

### 4.1 GUI-Friendly Service Layer with Status Codes

The `LibraryService` exposes `issueBookGUI()` and `returnBookGUI()` methods that return prefixed strings (`SUCCESS:`, `ERROR:`, `RESERVE:`) instead of printing to the console. This clean separation enables the UI to show appropriate dialogs.

```java
public String issueBookGUI(String bookId, String userId) {
    Book book = books.get(bookId);
    User user = users.get(userId);

    if (book == null) return "ERROR:Book ID not found.";
    if (!book.isAvailable()) {
        return "RESERVE:Book unavailable. Reserve?";
    }

    int activeCount = 0;
    for (Transaction t : transactions) {
        if (t.getUserId().equals(userId) && t.isActive())
            activeCount++;
    }
    if (activeCount >= user.getMaxBooksAllowed())
        return "ERROR:Max borrow limit reached.";

    // ... create transaction, borrow copy
    return "SUCCESS:" + user.getName() + " borrowed '"
           + book.getTitle() + "'";
}
```

**Justification:** This pattern decouples business logic from the UI layer. The dialog parses the prefix to decide whether to show a success message, an error, or a reservation prompt — without the service needing any Swing dependencies.

---

### 4.2 FIFO Reservation Queue with Auto-Notification

When a book is returned, `checkReservationQueue()` scans the `LinkedList<Reservation>` for the first unfulfilled reservation and marks it as fulfilled, appending a notification message to the return result.

```java
private String checkReservationQueue(String bookId) {
    for (Reservation r : reservations) {
        if (r.getBookId().equals(bookId) && !r.isFulfilled()) {
            User waiting = users.get(r.getUserId());
            Book book = books.get(bookId);
            r.setFulfilled(true);
            return "\n\nNote: " + waiting.getName()
                + " had reserved '" + book.getTitle()
                + "' and should be notified.";
        }
    }
    return "";
}
```

**Justification:** Using `LinkedList` in insertion order naturally implements FIFO semantics — the first user to reserve is the first to be notified. No external queue library is needed.

---

### 4.3 Comprehensive Report Export

The `exportReport()` method generates a formatted text file containing the full book catalog, registered users, all transactions, and active reservations — all with aligned column headers using `String.format()`.

```java
public String exportReport(String filepath) {
    try (PrintWriter writer =
            new PrintWriter(new FileWriter(filepath))) {
        writer.println("SMART LIBRARY MANAGEMENT SYSTEM REPORT");
        writer.println("Generated: " + LocalDate.now());

        writer.println("--- BOOK CATALOG ---");
        writer.println(String.format("%-8s %-25s %-20s", "ID", "Title", "Author"));
        for (Book book : books.values()) {
            writer.println(String.format("%-8s %-25s %-20s",
                book.getBookId(), book.getTitle(), book.getAuthor()));
        }
        // ... users, transactions, reservations
        return "SUCCESS:Report exported to: " + filepath;
    } catch (IOException e) {
        return "ERROR:Failed to write report.";
    }
}
```

**Justification:** This demonstrates file I/O with `PrintWriter`, formatting with `String.format()`, and proper resource management via try-with-resources — a practical "export" feature expected in real software.

---

### 4.4 First-Launch Data Seeding

`DataSeeder.seed()` populates the library with realistic sample data (8 books, 4 users, 3 transactions) only when the data store is empty, providing immediate usability.

```java
public static void seed(LibraryService library) {
    library.addBook(new Book("B001", "Clean Code",
        "Robert C. Martin", "Programming", 3));
    // ... 7 more books
    library.registerUser(new StudentMember("S001", "Rahul Sharma"));
    library.registerUser(new FacultyMember("F001", "Dr. Amit Sen"));
    // ... 2 more users
    library.issueBookGUI("B001", "S001"); // sample transaction
    library.saveData();
}
```

**Justification:** This avoids "empty screen" syndrome on first launch and doubles as a demonstration of the system working end-to-end with realistic data.

---

### 4.5 Dark-Themed Swing UI with Hover Effects

The entire GUI uses a consistent Catppuccin-inspired dark color palette defined as static constants in `DashboardFrame`, and every button has a `MouseListener` for hover feedback.

```java
private static final Color BG_DARK = new Color(30, 30, 46);
private static final Color ACCENT_BLUE = new Color(137, 180, 250);

JButton btn = new JButton(text);
btn.setBackground(BG_OVERLAY);
btn.setForeground(accentColor);
btn.addMouseListener(new MouseAdapter() {
    public void mouseEntered(MouseEvent e) {
        btn.setBackground(new Color(69, 71, 90));
    }
    public void mouseExited(MouseEvent e) {
        btn.setBackground(BG_OVERLAY);
    }
});
```

**Justification:** A polished, consistent UI shows attention to detail. The `MouseAdapter` (adapter pattern) allows overriding only the needed methods from `MouseListener`, and anonymous inner classes keep the code concise via lambda-like patterns.

---

## 5. Testing & Error Handling

### 5.1 Test Cases Considered

The project includes a JUnit test suite (`LibraryServiceTest.java`) with **15 test methods** covering all major business operations:

| # | Test Method | What It Validates |
|---|-------------|-------------------|
| 1 | `testAddBook()` | Book is added and collection size increases |
| 2 | `testBookAvailability()` | Newly added book shows as available |
| 3 | `testBorrowReducesAvailability()` | Available count decreases on borrow; becomes 0 → unavailable |
| 4 | `testReturnIncreasesAvailability()` | Available count increases on return |
| 5 | `testStudentMaxBooks()` | Student limit is 3 (polymorphic call) |
| 6 | `testFacultyMaxBooks()` | Faculty limit is 5 (polymorphic call) |
| 7 | `testStudentFineCalculation()` | 5 days late × ₹5 = ₹25 |
| 8 | `testFacultyFineCalculation()` | 5 days late × ₹2 = ₹10 |
| 9 | `testIssueBookSuccess()` | Valid issue returns SUCCESS prefix; available copies decrease |
| 10 | `testIssueBookInvalidBookId()` | Non-existent book ID returns ERROR |
| 11 | `testIssueBookInvalidUserId()` | Non-existent user ID returns ERROR |
| 12 | `testIssueBookUnavailable()` | Issuing when copies = 0 returns RESERVE |
| 13 | `testReserveBook()` | Reservation succeeds with queue position = 1 |
| 14 | `testDuplicateReservation()` | Same user reserving same book again returns ERROR |
| 15 | `testReturnBookSuccess()` | Valid return succeeds; available copies restored |
| 16 | `testReturnBookNoTransaction()` | Return without active borrow returns ERROR |

### 5.2 Edge Cases Handled

- **Empty fields:** All UI dialogs validate that required fields are non-empty before proceeding.
- **Invalid number format:** `AddBookDialog` catches `NumberFormatException` if the copies field is not a valid integer.
- **Non-positive copies:** Rejects copy counts ≤ 0 with a validation message.
- **Duplicate user ID:** `RegisterUserDialog` checks `library.getUser(id) != null` before registering.
- **Borrow limit exceeded:** `issueBookGUI()` counts active transactions per user and blocks if the limit is reached.
- **File not found on load:** `FileManager.loadObject()` checks `file.exists()` and returns `null` gracefully if no saved data exists.
- **Reservation deduplication:** `reserveBook()` iterates through existing reservations to reject duplicate requests.

### 5.3 Failure Scenarios

| Scenario | System Response |
|----------|----------------|
| User tries to borrow a book when all copies are out | Displays "Book unavailable" and offers reservation |
| Student tries to borrow a 4th book (limit = 3) | Returns `ERROR: Max borrow limit reached` |
| Return is attempted with no matching active transaction | Returns `ERROR: No active borrow found` |
| Data files are missing or corrupted on startup | `loadObject()` catches Exception and returns `null`; library starts fresh |
| Report export to a read-only path | `exportReport()` catches `IOException` and returns error message |
| Auto-save thread is interrupted | `InterruptedException` is caught; thread exits cleanly |

---

## 6. Git Workflow

### 6.1 Commit History

The project was developed incrementally across **13 meaningful commits**, each representing a logical development milestone:

```
24f17e8  Added reservation system, custom exceptions, and export report
18252ec  Added background autosave thread and JUnit test suite
69a1518  Implemented all action dialogs - AddBook, RegisterUser, IssueBook, ReturnBook
3f4da81  Added DashboardFrame with book table and control buttons
45f18b6  Added initial Swing GUI with LoginFrame
a6da9e1  file persistence using serialization
b6076cb  Implemented Transaction model with issueBook and returnBook logic
57d73b8  Implemented LibraryService with book and user management
e2de4a9  Implemented User hierarchy with inheritance and polymorphism
72247a5  Created the package folders
b0ef363  Create LibraryManagement
5cf5a1f  add deadline
59939a0  Initial commit
```

*(Screenshot of the actual commit history should be inserted here from `git log --oneline` or GitHub.)*

### 6.2 Development Progression

The development followed a structured, layered approach:

1. **Phase 1 — Project Setup** (Commits 59939a0 → 72247a5): Initialized the repository, created the `model/`, `service/`, `ui/`, and `util/` package directories.

2. **Phase 2 — Domain Models** (Commits e2de4a9 → b6076cb): Built the `User` abstract class hierarchy with `StudentMember` and `FacultyMember`; created the `Book` model; added `Transaction` with issue/return logic.

3. **Phase 3 — Service Layer** (Commit 57d73b8): Implemented `LibraryService` as the central engine managing books, users, and transactions.

4. **Phase 4 — Persistence** (Commit a6da9e1): Added `FileManager` for binary serialization to `.dat` files using `ObjectOutputStream`.

5. **Phase 5 — Swing GUI** (Commits 45f18b6 → 69a1518): Developed `LoginFrame` as the entry point, then `DashboardFrame` with the book table and sidebar, followed by all four dialog windows (`AddBookDialog`, `RegisterUserDialog`, `IssueBookDialog`, `ReturnBookDialog`).

6. **Phase 6 — Advanced Features** (Commits 18252ec → 24f17e8): Introduced the background auto-save daemon thread, JUnit test suite, reservation queue system, custom exception classes, and report export functionality.

---

## 7. Conclusion & Future Scope

### Conclusion

The **Smart Library Management System** successfully demonstrates the application of core Object-Oriented Programming principles in a practical, user-facing desktop application. The project features:

- A clean **Model–Service–UI** architecture with well-defined package boundaries.
- Robust use of **abstraction**, **inheritance**, and **polymorphism** through the `User` class hierarchy.
- **Encapsulation** of book state with controlled mutation methods.
- **Custom exceptions** for domain-specific error handling.
- The **Java Collections Framework** (`HashMap`, `ArrayList`, `LinkedList`) for efficient data management.
- **Multi-threading** via a daemon auto-save thread for background persistence.
- **Serialization** for data persistence across sessions.
- A polished **Swing GUI** with a consistent dark theme, hover effects, and input validation.
- A comprehensive **JUnit test suite** with 16 test cases covering standard and edge-case scenarios.

The project was developed incrementally with 13 meaningful Git commits, reflecting a disciplined, milestone-driven development workflow.

### Future Scope

1. **Database Integration:** Replace file-based serialization with an embedded database (e.g., SQLite via JDBC) for better query support and scalability.
2. **Role-Based Authentication:** Add login credentials and access control so that students can view their own borrowing history while librarians have full administrative access.
3. **Email/SMS Notifications:** Integrate notification APIs to alert users when a reserved book becomes available or when a due date is approaching.
4. **Fine Payment Tracking:** Add a payment module to record fine payments and maintain outstanding balance per user.
5. **Advanced Search & Filters:** Support multi-criteria search (author + category), sort by availability, and paginated results.
6. **Barcode/QR Scanning:** Integrate a barcode scanner library to auto-fill book IDs during issue/return, reducing manual input errors.
7. **Analytics Dashboard:** Display visual statistics (most borrowed books, active user count, overdue trends) using JFreeChart.
8. **Web Interface:** Port the system to a Spring Boot web application with a REST API and a React/Thymeleaf frontend for browser-based access.

---

*— End of Report —*
