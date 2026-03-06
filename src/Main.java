import model.StudentMember;
import model.FacultyMember;
import model.User;

public class Main {

    public static void main(String[] args) {

        System.out.println("Smart Library Management System Starting...");

        User student = new StudentMember("S101", "Rahul");
        User faculty = new FacultyMember("F201", "Dr. Sen");

        System.out.println(student.getName() + " fine for 3 days late: ₹" + student.calculateFine(3));
        System.out.println(faculty.getName() + " fine for 3 days late: ₹" + faculty.calculateFine(3));

    }

}
