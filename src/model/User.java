package model;

import java.io.Serializable;

public abstract class User implements Serializable {

    protected String userId;
    protected String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    // Maximum books allowed for this user type
    public abstract int getMaxBooksAllowed();

    // Fine calculation rule — differs per user type (polymorphism)
    public abstract double calculateFine(int daysLate);

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + userId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
