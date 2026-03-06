package model;

public class StudentMember extends User {

    public StudentMember(String userId, String name) {
        super(userId, name);
    }

    @Override
    public int getMaxBooksAllowed() {
        return 3;
    }

    @Override
    public double calculateFine(int daysLate) {
        // Students pay ₹5 per day late
        return daysLate * 5.0;
    }

}
