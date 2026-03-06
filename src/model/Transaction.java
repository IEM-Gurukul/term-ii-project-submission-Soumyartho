package model;

import java.time.LocalDate;

public class Transaction {

    private String transactionId;
    private String bookId;
    private String userId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Transaction(String transactionId, String bookId, String userId, LocalDate issueDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    // --- Getters ---

    public String getTransactionId() {
        return transactionId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    // --- Setters ---

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // --- Utility ---

    public boolean isActive() {
        return returnDate == null;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", userId='" + userId + '\'' +
                ", issued=" + issueDate +
                ", due=" + dueDate +
                ", returned=" + (returnDate != null ? returnDate : "ACTIVE") +
                '}';
    }

}
