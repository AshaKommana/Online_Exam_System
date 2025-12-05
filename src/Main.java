import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // For input
    private static final Scanner sc = new Scanner(System.in);

    // Simple User model
    static class User {
        String username;
        String password;
        String name;
        String email;

        User(String username, String password, String name, String email) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
        }
    }

    // Simple Question model
    static class Question {
        String questionText;
        String[] options;   // options[0..3]
        int correctOption;  // 1-4

        Question(String questionText, String[] options, int correctOption) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
        }
    }

    // Hard-coded user (you can change this)
    private static User currentUser = new User(
            "user1",           // username
            "1234",            // password
            "Default Name",    // name
            "user@example.com" // email
    );

    // Questions list
    private static final List<Question> questions = new ArrayList<>();

    public static void main(String[] args) {
        loadQuestions();
        showWelcomeScreen();
        loginScreen();
        sc.close();
    }

    // Just a header
    private static void showWelcomeScreen() {
        System.out.println("===========================================");
        System.out.println("         ONLINE EXAMINATION SYSTEM         ");
        System.out.println("===========================================\n");
    }

    // Login functionality
    private static void loginScreen() {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            if (username.equals(currentUser.username) && password.equals(currentUser.password)) {
                System.out.println("\nLogin successful! Welcome, " + currentUser.name + " 👋\n");
                loggedIn = true;
                showMainMenu();
            } else {
                System.out.println("Invalid username or password. Try again.\n");
            }
        }
    }

    // Main menu after login
    private static void showMainMenu() {
        int choice;

        do {
            System.out.println("============ MAIN MENU ============");
            System.out.println("1. Update Profile");
            System.out.println("2. Change Password");
            System.out.println("3. Start Exam");
            System.out.println("4. Logout");
            System.out.println("===================================");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine();
            if (!isNumber(input)) {
                System.out.println("Please enter a valid number.\n");
                continue;
            }
            choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    updateProfile();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    startExam();
                    break;
                case 4:
                    logout();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.\n");
            }
        } while (true);
    }

    // Helper to check if input is number
    private static boolean isNumber(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    // Update profile: name and email
    private static void updateProfile() {
        System.out.println("\n------ UPDATE PROFILE ------");
        System.out.println("Current Name : " + currentUser.name);
        System.out.println("Current Email: " + currentUser.email);

        System.out.print("Enter new name (or press Enter to keep same): ");
        String newName = sc.nextLine();
        if (!newName.trim().isEmpty()) {
            currentUser.name = newName.trim();
        }

        System.out.print("Enter new email (or press Enter to keep same): ");
        String newEmail = sc.nextLine();
        if (!newEmail.trim().isEmpty()) {
            currentUser.email = newEmail.trim();
        }

        System.out.println("Profile updated successfully!\n");
    }

    // Change password
    private static void changePassword() {
        System.out.println("\n------ CHANGE PASSWORD ------");
        System.out.print("Enter current password: ");
        String oldPass = sc.nextLine();

        if (!oldPass.equals(currentUser.password)) {
            System.out.println("Incorrect current password. Try again.\n");
            return;
        }

        System.out.print("Enter new password: ");
        String newPass1 = sc.nextLine();

        System.out.print("Confirm new password: ");
        String newPass2 = sc.nextLine();

        if (!newPass1.equals(newPass2)) {
            System.out.println("Passwords do not match. Try again.\n");
            return;
        }

        currentUser.password = newPass1;
        System.out.println("Password changed successfully!\n");
    }

    // Load questions into list
    private static void loadQuestions() {
        questions.add(new Question(
                "Which keyword is used to inherit a class in Java?",
                new String[]{"this", "super", "extends", "implements"},
                3
        ));

        questions.add(new Question(
                "Which of the following is not a Java primitive type?",
                new String[]{"int", "String", "double", "boolean"},
                2
        ));

        questions.add(new Question(
                "Which package contains the Scanner class?",
                new String[]{"java.io", "java.util", "java.lang", "java.net"},
                2
        ));

        questions.add(new Question(
                "What is the size of int in Java?",
                new String[]{"8 bits", "16 bits", "32 bits", "64 bits"},
                3
        ));

        questions.add(new Question(
                "Which method is the entry point of a Java program?",
                new String[]{"start()", "run()", "main()", "init()"},
                3
        ));
    }

    // Start exam with timer
    private static void startExam() {
        System.out.println("\n------ START EXAM ------");
        System.out.println("You will be given " + questions.size() + " questions.");
        System.out.println("Exam duration: 60 seconds.");
        System.out.println("Type option number (1-4) for each question.");
        System.out.println("Exam will auto-submit when time is over.\n");

        final long examDurationMillis = 60 * 1000; // 60 seconds
        long startTime = System.currentTimeMillis();
        int score = 0;
        int attempted = 0;

        for (int i = 0; i < questions.size(); i++) {
            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = examDurationMillis - elapsed;

            if (remaining <= 0) {
                System.out.println("\n⏰ Time is up! Auto-submitting your exam...\n");
                break;
            }

            System.out.println("Time remaining: " + (remaining / 1000) + " seconds");
            Question q = questions.get(i);

            System.out.println("\nQ" + (i + 1) + ". " + q.questionText);
            for (int opt = 0; opt < q.options.length; opt++) {
                System.out.println((opt + 1) + ") " + q.options[opt]);
            }

            int answer = 0;
            boolean valid = false;
            while (!valid) {
                System.out.print("Your answer (1-4): ");
                String input = sc.nextLine();

                if (!isNumber(input)) {
                    System.out.println("Please enter a number between 1 and 4.");
                    continue;
                }

                answer = Integer.parseInt(input);
                if (answer < 1 || answer > 4) {
                    System.out.println("Please enter a number between 1 and 4.");
                } else {
                    valid = true;
                }
            }

            attempted++;

            if (answer == q.correctOption) {
                score++;
            }

            // Check again if time exceeded after answering
            elapsed = System.currentTimeMillis() - startTime;
            if (elapsed >= examDurationMillis) {
                System.out.println("\n⏰ Time is up! Auto-submitting your exam...\n");
                break;
            }
        }

        System.out.println("----------- EXAM RESULT -----------");
        System.out.println("Name           : " + currentUser.name);
        System.out.println("Questions given: " + questions.size());
        System.out.println("Attempted      : " + attempted);
        System.out.println("Correct        : " + score);
        System.out.println("Score          : " + score + " / " + questions.size());
        System.out.println("-----------------------------------\n");
    }

    // Logout
    private static void logout() {
        System.out.println("\nLogging out... Goodbye " + currentUser.name + " 👋");
        System.out.println("Session closed.\n");
    }
}
