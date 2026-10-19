import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for the Message class using the Part 2 test data. */
public class MessageTest {

    private Message msg1;
    private Message msg2;

    @BeforeEach
    void setUp() {
        Message.clearSentMessages();
        msg1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        msg2 = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    @AfterEach
    void cleanUp() {
        new File("messages.json").delete();   // remove file created by the store test
    }

    @Test
    void messageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(msg1.getMessageText()));
    }

    @Test
    void messageLengthFailure() {
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.",
                Message.checkMessageLength("a".repeat(251)));
    }

    @Test
    void recipientSuccess() {
        assertEquals("Cell phone number successfully captured.", msg1.checkRecipientCell());
    }

    @Test
    void recipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                msg2.checkRecipientCell());
    }

    @Test
    void messageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", msg1.createMessageHash());
    }

    @Test
    void allMessageHashesInLoop() {
        Message[] messages = {msg1, msg2};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    @Test
    void messageIdCreated() {
        Message generated = new Message(0, "+27718693002", "Hello there");
        assertTrue(generated.checkMessageID());
        assertEquals(10, generated.getMessageID().length());
        assertEquals("Message ID generated: " + generated.getMessageID(),
                generated.idGeneratedMessage());
    }

    @Test
    void sendMessage() {
        assertEquals("Message successfully sent.", msg1.SentMessage(1));
    }

    @Test
    void disregardMessage() {
        assertEquals("Press 0 to delete the message.", msg2.SentMessage(2));
    }

    @Test
    void storeMessage() {
        assertEquals("Message successfully stored.", msg1.SentMessage(3));
    }

    @Test
    void totalMessagesSent() {
        msg1.SentMessage(1);   // sent
        msg2.SentMessage(2);   // disregarded, not counted
        assertEquals(1, Message.returnTotalMessagess());
    }

    @Test
    void printMessagesContainsSentMessage() {
        msg1.SentMessage(1);
        assertTrue(Message.printMessages().contains("00:0:HITONIGHT"));
    }
}