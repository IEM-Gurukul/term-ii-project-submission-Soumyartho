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

        // Add books
        Book b1 = new Book("B101", "Clean Code", "Robert Martin", "Programming", 5);
        Book b2 = new Book("B102", "Design Patterns", "Gamma", "Programming", 3);
        Book b3 = new Book("B103", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 2);

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);

        System.out.println("-------------------------------------------");

        // Register users
        User student = new StudentMember("S101", "Rahul");
        User faculty = new FacultyMember("F201", "Dr. Sen");

        library.registerUser(student);
        library.registerUser(faculty);

        System.out.println("-------------------------------------------");

        // Issue books
        library.issueBook("B101", "S101");   // Rahul borrows Clean Code
        library.issueBook("B102", "F201");   // Dr. Sen borrows Design Patterns

        System.out.println("-------------------------------------------");

        // Show active transactions
        library.printActiveTransactions();

        System.out.println("-------------------------------------------");

        // Return a book (on time since it was just issued)
        library.returnBook("B101", "S101");

        System.out.println("-------------------------------------------");

        // Verify availability changed
        System.out.println("Clean Code available copies: " + b1.getAvailableCopies() + "/" + b1.getTotalCopies());

        // Show active transactions after return
        library.printActiveTransactions();

        System.out.println("===========================================");
        System.out.println("All systems operational.");

    }

}
