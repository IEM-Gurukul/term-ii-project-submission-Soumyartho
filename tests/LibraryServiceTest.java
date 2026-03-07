import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import service.LibraryService;
import model.Book;
import model.StudentMember;
import model.FacultyMember;
import model.User;

/**
 * Unit tests for the Smart Library Management System.
 * Tests the core business logic in LibraryService.
 */
public class LibraryServiceTest {

    private LibraryService library;

    @Before
    public void setUp() {
        library = new LibraryService();

        library.addBook(new Book("B001", "Clean Code", "Robert Martin", "Programming", 2));
        library.addBook(new Book("B002", "Design Patterns", "Gamma", "Programming", 1));

        library.registerUser(new StudentMember("S001", "Rahul"));
        library.registerUser(new FacultyMember("F001", "Dr. Sen"));
    }

    // --- Book Management Tests ---

    @Test
    public void testAddBook() {
        library.addBook(new Book("B003", "New Book", "Author", "Fiction", 3));
        assertNotNull("Book should exist after adding", library.getBook("B003"));
        assertEquals(3, library.getAllBooks().size());
    }

    @Test
    public void testBookAvailability() {
        Book book = library.getBook("B001");
        assertTrue("New book should be available", book.isAvailable());
        assertEquals(2, book.getAvailableCopies());
    }

    @Test
    public void testBorrowReducesAvailability() {
        Book book = library.getBook("B001");
        book.borrowCopy();
        assertEquals(1, book.getAvailableCopies());
        assertTrue(book.isAvailable());

        book.borrowCopy();
        assertEquals(0, book.getAvailableCopies());
        assertFalse("Book should be unavailable after all copies borrowed", book.isAvailable());
    }

    @Test
    public void testReturnIncreasesAvailability() {
        Book book = library.getBook("B001");
        book.borrowCopy();
        assertEquals(1, book.getAvailableCopies());

        book.returnCopy();
        assertEquals(2, book.getAvailableCopies());
    }

    // --- User & Polymorphism Tests ---

    @Test
    public void testStudentMaxBooks() {
        User student = library.getUser("S001");
        assertEquals("Student max books should be 3", 3, student.getMaxBooksAllowed());
    }

    @Test
    public void testFacultyMaxBooks() {
        User faculty = library.getUser("F001");
        assertEquals("Faculty max books should be 5", 5, faculty.getMaxBooksAllowed());
    }

    @Test
    public void testStudentFineCalculation() {
        User student = library.getUser("S001");
        assertEquals("Student fine: 5 days * Rs.5 = Rs.25", 25.0, student.calculateFine(5), 0.01);
    }

    @Test
    public void testFacultyFineCalculation() {
        User faculty = library.getUser("F001");
        assertEquals("Faculty fine: 5 days * Rs.2 = Rs.10", 10.0, faculty.calculateFine(5), 0.01);
    }

    // --- Issue & Return via Service ---

    @Test
    public void testIssueBookSuccess() {
        String result = library.issueBookGUI("B001", "S001");
        assertTrue("Issue should succeed", result.startsWith("SUCCESS"));

        Book book = library.getBook("B001");
        assertEquals(1, book.getAvailableCopies());
    }

    @Test
    public void testIssueBookInvalidBookId() {
        String result = library.issueBookGUI("INVALID", "S001");
        assertTrue("Should return error for invalid book", result.startsWith("ERROR"));
    }

    @Test
    public void testIssueBookInvalidUserId() {
        String result = library.issueBookGUI("B001", "INVALID");
        assertTrue("Should return error for invalid user", result.startsWith("ERROR"));
    }

    @Test
    public void testIssueBookUnavailable() {
        // Book B002 has only 1 copy
        library.issueBookGUI("B002", "S001");
        String result = library.issueBookGUI("B002", "F001");
        assertTrue("Should fail when no copies available", result.startsWith("ERROR"));
    }

    @Test
    public void testReturnBookSuccess() {
        library.issueBookGUI("B001", "S001");
        String result = library.returnBookGUI("B001", "S001");
        assertTrue("Return should succeed", result.startsWith("SUCCESS"));

        Book book = library.getBook("B001");
        assertEquals(2, book.getAvailableCopies());
    }

    @Test
    public void testReturnBookNoTransaction() {
        String result = library.returnBookGUI("B001", "S001");
        assertTrue("Should fail with no active transaction", result.startsWith("ERROR"));
    }

}
