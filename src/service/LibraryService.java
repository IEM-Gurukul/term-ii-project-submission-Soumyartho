package service;

import model.Book;
import model.User;
import model.Transaction;
import model.Reservation;
import util.FileManager;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryService {

    private HashMap<String, Book> books = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private LinkedList<Reservation> reservations = new LinkedList<>();  // Queue (FIFO)

    // --- Book Management ---

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
        System.out.println("Book added: " + book.getTitle());
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public java.util.Collection<Book> getAllBooks() {
        return books.values();
    }

    // --- User Management ---

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
        System.out.println("User registered: " + user.getName());
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    // --- Search ---

    public void searchBookByTitle(String title) {
        boolean found = false;

        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println("Found: " + book);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found matching: " + title);
        }
    }

    public void searchBookByAuthor(String author) {
        boolean found = false;

        for (Book book : books.values()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                System.out.println("Found: " + book);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found by author: " + author);
        }
    }

    public void searchBookByCategory(String category) {
        boolean found = false;

        for (Book book : books.values()) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                System.out.println("Found: " + book);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found in category: " + category);
        }
    }

    // --- Issue & Return ---

    public void issueBook(String bookId, String userId) {

        Book book = books.get(bookId);
        User user = users.get(userId);

        if (book == null || user == null) {
            System.out.println("Invalid book or user ID.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book '" + book.getTitle() + "' is currently unavailable.");
            return;
        }

        // Check if user has reached their borrowing limit
        int activeCount = 0;
        for (Transaction t : transactions) {
            if (t.getUserId().equals(userId) && t.isActive()) {
                activeCount++;
            }
        }

        if (activeCount >= user.getMaxBooksAllowed()) {
            System.out.println(user.getName() + " has reached the max borrow limit (" + user.getMaxBooksAllowed() + ").");
            return;
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        String transactionId = "T" + (transactions.size() + 1);

        Transaction transaction = new Transaction(transactionId, bookId, userId, issueDate, dueDate);
        transactions.add(transaction);
        book.borrowCopy();

        System.out.println(user.getName() + " borrowed '" + book.getTitle() + "' due on " + dueDate);

    }

    public void returnBook(String bookId, String userId) {

        for (Transaction t : transactions) {

            if (t.getBookId().equals(bookId) &&
                t.getUserId().equals(userId) &&
                t.isActive()) {

                LocalDate returnDate = LocalDate.now();
                t.setReturnDate(returnDate);

                Book book = books.get(bookId);
                User user = users.get(userId);

                book.returnCopy();

                if (returnDate.isAfter(t.getDueDate())) {

                    long daysLate = ChronoUnit.DAYS.between(t.getDueDate(), returnDate);
                    double fine = user.calculateFine((int) daysLate);

                    System.out.println("Book returned late by " + daysLate + " day(s). Fine: Rs." + fine);

                } else {

                    System.out.println(user.getName() + " returned '" + book.getTitle() + "' on time.");

                }

                return;

            }

        }

        System.out.println("No matching active transaction found.");

    }

    // --- Transaction Info ---

    public void printActiveTransactions() {
        System.out.println("Active transactions:");
        boolean any = false;
        for (Transaction t : transactions) {
            if (t.isActive()) {
                System.out.println("  " + t);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  None.");
        }
    }

    public java.util.Collection<User> getAllUsers() {
        return users.values();
    }

    public ArrayList<Transaction> getActiveTransactions() {
        ArrayList<Transaction> active = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.isActive()) {
                active.add(t);
            }
        }
        return active;
    }

    // --- GUI-friendly methods (return result strings instead of printing) ---

    public String issueBookGUI(String bookId, String userId) {

        Book book = books.get(bookId);
        User user = users.get(userId);

        if (book == null) return "ERROR:Book ID '" + bookId + "' not found.";
        if (user == null) return "ERROR:User ID '" + userId + "' not found.";

        if (!book.isAvailable()) {
            return "RESERVE:Book '" + book.getTitle() + "' is currently unavailable.\nWould you like to reserve it?";
        }

        int activeCount = 0;
        for (Transaction t : transactions) {
            if (t.getUserId().equals(userId) && t.isActive()) {
                activeCount++;
            }
        }

        if (activeCount >= user.getMaxBooksAllowed()) {
            return "ERROR:" + user.getName() + " has reached the max borrow limit (" + user.getMaxBooksAllowed() + ").";
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        String transactionId = "T" + (transactions.size() + 1);
        Transaction transaction = new Transaction(transactionId, bookId, userId, issueDate, dueDate);
        transactions.add(transaction);
        book.borrowCopy();

        return "SUCCESS:" + user.getName() + " borrowed '" + book.getTitle() + "'\nDue on: " + dueDate;
    }

    public String returnBookGUI(String bookId, String userId) {

        for (Transaction t : transactions) {
            if (t.getBookId().equals(bookId) &&
                t.getUserId().equals(userId) &&
                t.isActive()) {

                LocalDate returnDate = LocalDate.now();
                t.setReturnDate(returnDate);

                Book book = books.get(bookId);
                User user = users.get(userId);
                book.returnCopy();

                if (returnDate.isAfter(t.getDueDate())) {
                    long daysLate = ChronoUnit.DAYS.between(t.getDueDate(), returnDate);
                    double fine = user.calculateFine((int) daysLate);
                    String msg = "Book returned late by " + daysLate + " day(s).\nFine: Rs." + fine;
                    msg += checkReservationQueue(bookId);
                    return "SUCCESS:" + msg;
                } else {
                    String msg = user.getName() + " returned '" + book.getTitle() + "' on time!";
                    msg += checkReservationQueue(bookId);
                    return "SUCCESS:" + msg;
                }
            }
        }

        return "ERROR:No active borrow found for Book '" + bookId + "' by User '" + userId + "'.";
    }

    // --- Reservation System (Queue) ---

    public String reserveBook(String bookId, String userId) {
        Book book = books.get(bookId);
        User user = users.get(userId);

        if (book == null) return "ERROR:Book ID '" + bookId + "' not found.";
        if (user == null) return "ERROR:User ID '" + userId + "' not found.";

        // Check if user already has an active reservation for this book
        for (Reservation r : reservations) {
            if (r.getBookId().equals(bookId) && r.getUserId().equals(userId) && !r.isFulfilled()) {
                return "ERROR:" + user.getName() + " already has a reservation for this book.";
            }
        }

        String reservationId = "R" + (reservations.size() + 1);
        Reservation reservation = new Reservation(reservationId, bookId, userId, LocalDate.now());
        reservations.add(reservation);

        int position = 0;
        for (Reservation r : reservations) {
            if (r.getBookId().equals(bookId) && !r.isFulfilled()) {
                position++;
            }
        }

        return "SUCCESS:" + user.getName() + " reserved '" + book.getTitle() + "'\nQueue position: " + position;
    }

    private String checkReservationQueue(String bookId) {
        for (Reservation r : reservations) {
            if (r.getBookId().equals(bookId) && !r.isFulfilled()) {
                User waiting = users.get(r.getUserId());
                Book book = books.get(bookId);
                r.setFulfilled(true);
                return "\n\nNote: " + waiting.getName() + " had reserved '" + book.getTitle() + "' and should be notified.";
            }
        }
        return "";
    }

    public LinkedList<Reservation> getActiveReservations() {
        LinkedList<Reservation> active = new LinkedList<>();
        for (Reservation r : reservations) {
            if (!r.isFulfilled()) {
                active.add(r);
            }
        }
        return active;
    }

    // --- Export Report ---

    public String exportReport(String filepath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {

            writer.println("============================================");
            writer.println("   SMART LIBRARY MANAGEMENT SYSTEM REPORT");
            writer.println("   Generated: " + LocalDate.now());
            writer.println("============================================");
            writer.println();

            // Books
            writer.println("--- BOOK CATALOG (" + books.size() + " books) ---");
            writer.println(String.format("%-8s %-25s %-20s %-15s %s", "ID", "Title", "Author", "Category", "Available"));
            writer.println("------------------------------------------------------------------------");
            for (Book book : books.values()) {
                writer.println(String.format("%-8s %-25s %-20s %-15s %d/%d",
                    book.getBookId(), book.getTitle(), book.getAuthor(),
                    book.getCategory(), book.getAvailableCopies(), book.getTotalCopies()));
            }
            writer.println();

            // Users
            writer.println("--- REGISTERED USERS (" + users.size() + " users) ---");
            writer.println(String.format("%-8s %-20s %-15s %s", "ID", "Name", "Type", "Max Books"));
            writer.println("--------------------------------------------------------");
            for (User user : users.values()) {
                writer.println(String.format("%-8s %-20s %-15s %d",
                    user.getUserId(), user.getName(),
                    user.getClass().getSimpleName(), user.getMaxBooksAllowed()));
            }
            writer.println();

            // Transactions
            writer.println("--- ALL TRANSACTIONS (" + transactions.size() + " total) ---");
            writer.println(String.format("%-6s %-8s %-8s %-12s %-12s %s", "ID", "Book", "User", "Issued", "Due", "Returned"));
            writer.println("------------------------------------------------------------------------");
            for (Transaction t : transactions) {
                writer.println(String.format("%-6s %-8s %-8s %-12s %-12s %s",
                    t.getTransactionId(), t.getBookId(), t.getUserId(),
                    t.getIssueDate(), t.getDueDate(),
                    t.getReturnDate() != null ? t.getReturnDate() : "ACTIVE"));
            }
            writer.println();

            // Reservations
            LinkedList<Reservation> activeRes = getActiveReservations();
            writer.println("--- ACTIVE RESERVATIONS (" + activeRes.size() + ") ---");
            for (Reservation r : activeRes) {
                User u = users.get(r.getUserId());
                Book b = books.get(r.getBookId());
                writer.println("  " + (u != null ? u.getName() : r.getUserId()) + " -> " + (b != null ? b.getTitle() : r.getBookId()) + " (since " + r.getReservedDate() + ")");
            }
            if (activeRes.isEmpty()) writer.println("  None.");

            writer.println();
            writer.println("============================================");
            writer.println("                END OF REPORT");
            writer.println("============================================");

            return "SUCCESS:Report exported to: " + filepath;

        } catch (IOException e) {
            return "ERROR:Failed to write report: " + e.getMessage();
        }
    }

    // --- Persistence ---

    public void saveData() {
        new File("data").mkdirs();

        FileManager.saveObject(books, "data/books.dat");
        FileManager.saveObject(users, "data/users.dat");
        FileManager.saveObject(transactions, "data/transactions.dat");
        FileManager.saveObject(reservations, "data/reservations.dat");

        System.out.println("Library data saved.");
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        Object b = FileManager.loadObject("data/books.dat");
        Object u = FileManager.loadObject("data/users.dat");
        Object t = FileManager.loadObject("data/transactions.dat");
        Object r = FileManager.loadObject("data/reservations.dat");

        if (b != null) books = (HashMap<String, Book>) b;
        if (u != null) users = (HashMap<String, User>) u;
        if (t != null) transactions = (ArrayList<Transaction>) t;
        if (r != null) reservations = (LinkedList<Reservation>) r;

        System.out.println("Library data loaded. Books: " + books.size() + ", Users: " + users.size() + ", Transactions: " + transactions.size() + ", Reservations: " + reservations.size());
    }

}
