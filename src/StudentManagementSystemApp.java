import java.util.*;
import java.io.*;

public class StudentManagementSystemApp {

    // --------- Student Class ---------
    static class Student {
        private String name;
        private int rollNumber;
        private String grade;
        private String email;

        public Student(String name, int rollNumber, String grade, String email) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.grade = grade;
            this.email = email;
        }

        public String getName() { return name; }
        public int getRollNumber() { return rollNumber; }
        public String getGrade() { return grade; }
        public String getEmail() { return email; }

        public void setName(String name) { this.name = name; }
        public void setGrade(String grade) { this.grade = grade; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return "Roll No: " + rollNumber + ", Name: " + name + ", Grade: " + grade + ", Email: " + email;
        }
    }

    // --------- Management System Class ---------
    static class StudentManagementSystem {
        private List<Student> students;
        private final String fileName = "students.txt";

        public StudentManagementSystem() {
            students = new ArrayList<>();
            loadFromFile();
        }

        public void addStudent(Student student) {
            students.add(student);
            saveToFile();
        }

        public void removeStudent(int rollNumber) {
            students.removeIf(s -> s.getRollNumber() == rollNumber);
            saveToFile();
        }

        public Student searchStudent(int rollNumber) {
            for (Student s : students) {
                if (s.getRollNumber() == rollNumber) return s;
            }
            return null;
        }

        public void displayAllStudents() {
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student s : students) {
                    System.out.println(s);
                }
            }
        }

        public void editStudent(int rollNumber, String name, String grade, String email) {
            Student s = searchStudent(rollNumber);
            if (s != null) {
                s.setName(name);
                s.setGrade(grade);
                s.setEmail(email);
                saveToFile();
            }
        }

        private void loadFromFile() {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    students.add(new Student(data[0], Integer.parseInt(data[1]), data[2], data[3]));
                }
            } catch (IOException e) {
                System.out.println("File not found. Starting with empty list.");
            }
        }

        private void saveToFile() {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                for (Student s : students) {
                    bw.write(s.getName() + "," + s.getRollNumber() + "," + s.getGrade() + "," + s.getEmail());
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving to file.");
            }
        }
    }

    // --------- Main Method / Console UI ---------
    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==== Student Management System ====");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Edit Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter Roll Number: ");
                    int roll = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Grade: ");
                    String grade = scanner.nextLine().trim();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine().trim();

                    if (name.isEmpty() || grade.isEmpty() || email.isEmpty()) {
                        System.out.println("Error: All fields must be filled.");
                    } else {
                        sms.addStudent(new Student(name, roll, grade, email));
                        System.out.println("Student added successfully.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Roll Number to remove: ");
                    int rno = scanner.nextInt();
                    sms.removeStudent(rno);
                    System.out.println("Student removed if existed.");
                    break;

                case 3:
                    System.out.print("Enter Roll Number to search: ");
                    int sroll = scanner.nextInt();
                    Student s = sms.searchStudent(sroll);
                    if (s != null) System.out.println(s);
                    else System.out.println("Student not found.");
                    break;

                case 4:
                    sms.displayAllStudents();
                    break;

                case 5:
                    System.out.print("Enter Roll Number to edit: ");
                    int eroll = scanner.nextInt();
                    scanner.nextLine();
                    Student editS = sms.searchStudent(eroll);
                    if (editS != null) {
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine().trim();
                        System.out.print("Enter new grade: ");
                        String newGrade = scanner.nextLine().trim();
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine().trim();
                        if (newName.isEmpty() || newGrade.isEmpty() || newEmail.isEmpty()) {
                            System.out.println("Error: All fields must be filled.");
                        } else {
                            sms.editStudent(eroll, newName, newGrade, newEmail);
                            System.out.println("Student info updated.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 6);

        scanner.close();
    }
}