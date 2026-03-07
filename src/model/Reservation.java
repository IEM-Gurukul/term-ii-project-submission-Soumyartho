package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a reservation request when a book is unavailable.
 * Stored in a queue (FIFO) — first to reserve, first to be notified.
 */
public class Reservation implements Serializable {

    private String reservationId;
    private String bookId;
    private String userId;
    private LocalDate reservedDate;
    private boolean fulfilled;

    public Reservation(String reservationId, String bookId, String userId, LocalDate reservedDate) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.userId = userId;
        this.reservedDate = reservedDate;
        this.fulfilled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getReservedDate() {
        return reservedDate;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + reservationId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", userId='" + userId + '\'' +
                ", date=" + reservedDate +
                ", status=" + (fulfilled ? "FULFILLED" : "WAITING") +
                '}';
    }

}
