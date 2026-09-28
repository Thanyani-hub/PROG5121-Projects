import java.util.Scanner;

/** Console entry point: register, then log in. */
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== Registration ===");
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();
        System.out.print("Enter cell number (e.g. +27838968976): ");
        String cell = input.nextLine();

        Login login = new Login(firstName, lastName, username, password, cell);
        System.out.println(login.registerUser());

        if (!login.isRegistrationValid()) {
            System.out.println("Registration failed. Please restart and try again.");
            return;
        }

        System.out.println("\n=== Login ===");
        System.out.print("Enter username: ");
        String loginUser = input.nextLine();
        System.out.print("Enter password: ");
        String loginPass = input.nextLine();

        boolean success = login.loginUser(loginUser, loginPass);
        System.out.println(login.returnLoginStatus(success));
    }
}