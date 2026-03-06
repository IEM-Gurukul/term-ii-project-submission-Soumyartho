package service;

import model.Book;
import model.User;

import java.util.HashMap;

public class LibraryService {

    private HashMap<String, Book> books = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();

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

}
