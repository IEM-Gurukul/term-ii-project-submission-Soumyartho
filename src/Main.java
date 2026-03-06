import service.LibraryService;
import model.Book;
import model.StudentMember;
import model.FacultyMember;
import model.User;

public class Main {

    public static void main(String[] args) {

        System.out.println("Smart Library Management System Starting...");
        System.out.println("-------------------------------------------");

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

        // Search tests
        System.out.println("Search by title 'Clean':");
        library.searchBookByTitle("Clean");

        System.out.println();
        System.out.println("Search by author 'Gamma':");
        library.searchBookByAuthor("Gamma");

        System.out.println();
        System.out.println("Search by category 'Fiction':");
        library.searchBookByCategory("Fiction");

        System.out.println("-------------------------------------------");

        // Polymorphism demo
        System.out.println(student.getName() + " max books: " + student.getMaxBooksAllowed());
        System.out.println(faculty.getName() + " max books: " + faculty.getMaxBooksAllowed());

    }

}
