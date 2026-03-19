package util;

import model.Book;
import model.StudentMember;
import model.FacultyMember;
import service.LibraryService;

/**
 * Seeds the library with sample data for demonstration purposes.
 * Only runs when the library is empty (first launch).
 */
public class DataSeeder {

    public static void seed(LibraryService library) {

        // --- 8 Books across different categories ---

        library.addBook(new Book("B001", "Clean Code", "Robert C. Martin", "Programming", 3));
        library.addBook(new Book("B002", "Design Patterns", "Erich Gamma", "Programming", 2));
        library.addBook(new Book("B003", "Data Structures with Java", "John Lewis", "Computer Science", 4));
        library.addBook(new Book("B004", "Introduction to Algorithms", "Thomas Cormen", "Computer Science", 2));
        library.addBook(new Book("B005", "The Pragmatic Programmer", "Andrew Hunt", "Programming", 1));
        library.addBook(new Book("B006", "Database System Concepts", "Abraham Silberschatz", "Database", 3));
        library.addBook(new Book("B007", "Operating System Concepts", "Abraham Silberschatz", "Operating Systems", 2));
        library.addBook(new Book("B008", "Computer Networks", "Andrew Tanenbaum", "Networking", 3));

        // --- 4 Users (2 Students + 2 Faculty) ---

        library.registerUser(new StudentMember("S001", "Rahul Sharma"));
        library.registerUser(new StudentMember("S002", "Priya Das"));
        library.registerUser(new FacultyMember("F001", "Dr. Amit Sen"));
        library.registerUser(new FacultyMember("F002", "Prof. Neeta Roy"));

        // --- 3 Sample Transactions (to show issue history) ---

        library.issueBookGUI("B001", "S001");  // Rahul borrows Clean Code
        library.issueBookGUI("B003", "S002");  // Priya borrows Data Structures
        library.issueBookGUI("B002", "F001");  // Dr. Sen borrows Design Patterns

        // Save immediately so data persists
        library.saveData();

        System.out.println("[Seeder] Sample data loaded: 8 books, 4 users, 3 transactions.");
    }

}
