package model;

public class FacultyMember extends User {

    public FacultyMember(String userId, String name) {
        super(userId, name);
    }

    @Override
    public int getMaxBooksAllowed() {
        return 5;
    }

    @Override
    public double calculateFine(int daysLate) {
        // Faculty pay ₹2 per day late (lower rate)
        return daysLate * 2.0;
    }

}
