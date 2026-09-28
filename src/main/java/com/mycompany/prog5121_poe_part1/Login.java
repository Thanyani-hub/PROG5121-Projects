import java.util.regex.Pattern;

/**
 * Handles registration and login for QuickChat (Part 1).
 * Stores the registered user's details and validates them.
 */
public class Login {

    // Registration details
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String cellNumber;

    /*
     * Cell phone regex: international code +27 followed by 9 digits.
     * TODO: Replace this comment with the source you researched, e.g.
     * Author/Website (Year). Title. Available at: URL (Accessed: date).
     */
    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+27\\d{9}$");

    // Messages required by the brief
    public static final String USERNAME_OK = "Username successfully captured.";
    public static final String USERNAME_BAD = "Username is not correctly formatted; please ensure that your "
            + "username contains an underscore and is no more than five characters in length.";
    public static final String PASSWORD_OK = "Password successfully captured.";
    public static final String PASSWORD_BAD = "Password is not correctly formatted; please ensure that the "
            + "password contains at least eight characters, a capital letter, a number, and a special character.";
    public static final String CELL_OK = "Cell number successfully captured.";
    public static final String CELL_BAD = "Cell number is incorrectly formatted or does not contain an "
            + "international code; please correct the number and try again.";
    public static final String LOGIN_FAIL = "Username or password incorrect, please try again.";

    public Login(String firstName, String lastName, String username, String password, String cellNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
    }

    /** Username must contain an underscore and be no more than five characters long. */
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /** Password: 8+ characters, a capital letter, a number and a special character. */
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetter(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /** Cell number must match the international-code regex. */
    public boolean checkCellPhoneNumber() {
        return cellNumber != null && CELL_PATTERN.matcher(cellNumber).matches();
    }

    // Helpers that return the message for each individual check
    public String getUsernameMessage() {
        return checkUserName() ? USERNAME_OK : USERNAME_BAD;
    }

    public String getPasswordMessage() {
        return checkPasswordComplexity() ? PASSWORD_OK : PASSWORD_BAD;
    }

    public String getCellMessage() {
        return checkCellPhoneNumber() ? CELL_OK : CELL_BAD;
    }

    /** Returns the registration messaging for all three checks. */
    public String registerUser() {
        StringBuilder result = new StringBuilder();
        result.append(getUsernameMessage()).append("\n");
        result.append(getPasswordMessage()).append("\n");
        result.append(getCellMessage());

        if (isRegistrationValid()) {
            result.append("\nUser has been registered successfully.");
        }
        return result.toString();
    }

    /** True only when username, password and cell number are all valid. */
    public boolean isRegistrationValid() {
        return checkUserName() && checkPasswordComplexity() && checkCellPhoneNumber();
    }

    /** Checks the entered details against the details stored at registration. */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return isRegistrationValid()
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    /** Returns the login status message. */
    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return LOGIN_FAIL;
    }
}