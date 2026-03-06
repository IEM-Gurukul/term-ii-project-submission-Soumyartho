import service.LibraryService;
import model.Book;
import model.StudentMember;
import model.FacultyMember;
import model.User;

public class Main {

    public static void main(String[] args) {

        System.out.println("Smart Library Management System Starting...");
        System.out.println("===========================================");

        LibraryService library = new LibraryService();
        library.loadData();

        System.out.println("-------------------------------------------");

        // Only add sample data if library is empty (first run)
        if (library.getBook("B101") == null) {

            System.out.println("First run detected. Adding sample data...");
            System.out.println();

            // Add books
            library.addBook(new Book("B101", "Clean Code", "Robert Martin", "Programming", 5));
            library.addBook(new Book("B102", "Design Patterns", "Gamma", "Programming", 3));
            library.addBook(new Book("B103", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 2));

            // Register users
            library.registerUser(new StudentMember("S101", "Rahul"));
            library.registerUser(new FacultyMember("F201", "Dr. Sen"));

            // Issue a book
            library.issueBook("B101", "S101");

        } else {

            System.out.println("Data loaded from previous session!");

        }

        System.out.println("-------------------------------------------");

        // Show current state
        library.searchBookByTitle("Clean");
        library.printActiveTransactions();

        System.out.println("-------------------------------------------");

        // Save before exit
        library.saveData();

        System.out.println("===========================================");
        System.out.println("System shut down. Data persisted.");

    }

}
