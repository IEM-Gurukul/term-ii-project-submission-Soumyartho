package service;

import model.Book;
import model.User;
import model.Transaction;

import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryService {

    private HashMap<String, Book> books = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();

    // --- Book Management ---

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
        System.out.println("Book added: " + book.getTitle());
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
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

}
