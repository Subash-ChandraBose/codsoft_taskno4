import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", rollNumber=" + rollNumber +
                ", grade='" + grade + '\'' +
                '}';
    }
}

class StudentManagementSystem {
    private List<Student> students;
    private String dataFilePath;

    public StudentManagementSystem(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.students = readStudentsFromFile();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudentsToFile();
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        saveStudentsToFile();
    }

    public Student searchStudent(int rollNumber) {
        return students.stream()
                .filter(student -> student.getRollNumber() == rollNumber)
                .findFirst()
                .orElse(null);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    private List<Student> readStudentsFromFile() {
        List<Student> students = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFilePath))) {
            students = (List<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore if the file is not found, as it may be the first run
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return students;
    }

    private void saveStudentsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFilePath))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class StudentManagementSystemApp {
    private static final String DATA_FILE_PATH = "students.dat";
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentManagementSystem studentManagementSystem = new StudentManagementSystem(DATA_FILE_PATH);

    public static void main(String[] args) {
        int choice;

        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    removeStudent();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    displayAllStudents();
                    break;
                case 5:
                    System.out.println("Exiting Student Management System. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        } while (choice != 5);
    }

    private static void displayMenu() {
        System.out.println("Student Management Systems:");
        System.out.println("1. Add a Student");
        System.out.println("2. Remove a Student");
        System.out.println("3. Search for a Student");
        System.out.println("4. Display All Students");
        System.out.println("5. Exit");
    }

    private static void addStudent() {
        System.out.println("Adding a new student:");

        // Input validation
        String name = getNonEmptyInput("Enter student name: ");
        int rollNumber = getIntInput("Enter student roll number: ");
        String grade = getNonEmptyInput("Enter student grade: ");

        Student newStudent = new Student(name, rollNumber, grade);
        studentManagementSystem.addStudent(newStudent);

        System.out.println("Student added successfully.");
    }

    private static void removeStudent() {
        System.out.println("Removing a student:");

        int rollNumber = getIntInput("Enter student roll number to remove: ");
        Student removedStudent = studentManagementSystem.searchStudent(rollNumber);

        if (removedStudent != null) {
            studentManagementSystem.removeStudent(rollNumber);
            System.out.println("Student removed successfully: " + removedStudent);
        } else {
            System.out.println("Student with roll number " + rollNumber + " not found.");
        }
    }

    private static void searchStudent() {
        System.out.println("Searching for a student:");

        int rollNumber = getIntInput("Enter student roll number to search: ");
        Student foundStudent = studentManagementSystem.searchStudent(rollNumber);

        if (foundStudent != null) {
            System.out.println("Student found: " + foundStudent);
        } else {
            System.out.println("Student with roll number " + rollNumber + " not found.");
        }
    }

    private static void displayAllStudents() {
        List<Student> allStudents = studentManagementSystem.getAllStudents();

        if (!allStudents.isEmpty()) {
            System.out.println("All Students:");

            for (Student student : allStudents) {
                System.out.println(student);
            }
        } else {
            System.out.println("No students found.");
        }
    }

    private static String getNonEmptyInput(String prompt) {
        String input;

        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        } while (input.isEmpty());

        return input;
    }

    private static int getIntInput(String prompt) {
        int input = 0;
        boolean validInput = false;

        do {
            System.out.print(prompt);

            try {
                input = scanner.nextInt();
                validInput = true;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); 
            }
        } while (!validInput);

        scanner.nextLine(); 

        return input;
    }
}
