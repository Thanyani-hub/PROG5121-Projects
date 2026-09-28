import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Unit tests for the Login class using the test data from the brief. */
public class LoginTest {

    private Login make(String user, String pass, String cell) {
        return new Login("Kyle", "Smith", user, pass, cell);
    }

    // ---------- assertEquals ----------
    @Test
    void usernameCorrectlyFormattedMessage() {
        assertEquals("Username successfully captured.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").getUsernameMessage());
    }

    @Test
    void usernameIncorrectlyFormattedMessage() {
        assertEquals(Login.USERNAME_BAD, make("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976").getUsernameMessage());
    }

    @Test
    void passwordMeetsComplexityMessage() {
        assertEquals("Password successfully captured.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").getPasswordMessage());
    }

    @Test
    void passwordFailsComplexityMessage() {
        assertEquals(Login.PASSWORD_BAD, make("kyl_1", "password", "+27838968976").getPasswordMessage());
    }

    @Test
    void cellCorrectlyFormattedMessage() {
        assertEquals("Cell number successfully captured.", make("kyl_1", "Ch&&sec@ke99!", "+27838968976").getCellMessage());
    }

    @Test
    void cellIncorrectlyFormattedMessage() {
        assertEquals(Login.CELL_BAD, make("kyl_1", "Ch&&sec@ke99!", "08966553").getCellMessage());
    }

    // ---------- assertTrue / assertFalse ----------
    @Test
    void loginSuccessful() {
        Login login = make("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus(true));
    }

    @Test
    void loginFailed() {
        Login login = make("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
        assertEquals(Login.LOGIN_FAIL, login.returnLoginStatus(false));
    }

    @Test
    void usernameTrueAndFalse() {
        assertTrue(make("kyl_1", "x", "x").checkUserName());
        assertFalse(make("kyle!!!!!!!", "x", "x").checkUserName());
    }

    @Test
    void passwordTrueAndFalse() {
        assertTrue(make("x", "Ch&&sec@ke99!", "x").checkPasswordComplexity());
        assertFalse(make("x", "password", "x").checkPasswordComplexity());
    }

    @Test
    void cellTrueAndFalse() {
        assertTrue(make("x", "x", "+27838968976").checkCellPhoneNumber());
        assertFalse(make("x", "x", "08966553").checkCellPhoneNumber());
    }
}