import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Unit tests for the Part 3 arrays and Stored Messages features, using the Part 3 test data. */
public class MessageManagerTest {

    private static final String SENDER = "Kyle Smith";

    @TempDir
    Path tempDir;

    private MessageManager manager;
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    void setUp() {
        // Use a temporary JSON file so the real messages.json is never touched
        Message.setJsonFile(tempDir.resolve("messages.json").toString());

        // Test data messages 1-5 from the brief (IDs are developer-supplied so the results are predictable).
        // Message 4's value 0838884567 is used as its ID and recipient, as listed in the brief.
        message1 = new Message("0011223344", 0, "+27834557896", "Did you get the cake?");
        message2 = new Message("1122334455", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message("2233445566", 2, "+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message("0838884567", 3, "0838884567", "It is dinner time!");
        message5 = new Message("4455667788", 4, "+27838884567", "Ok, I am leaving without you.");

        manager = new MessageManager();
        manager.addMessage(message1, MessageManager.FLAG_SENT);
        manager.addMessage(message2, MessageManager.FLAG_STORED);
        manager.addMessage(message3, MessageManager.FLAG_DISREGARD);
        manager.addMessage(message4, MessageManager.FLAG_SENT);
        manager.addMessage(message5, MessageManager.FLAG_STORED);
    }

    // ---------- Arrays correctly populated ----------

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"), manager.getSentMessageTexts());
    }

    @Test
    void disregardedMessagesArrayCorrectlyPopulated() {
        assertEquals(List.of("Yohoooo, I am at your gate."), manager.getDisregardedMessageTexts());
    }

    @Test
    void storedMessagesArrayCorrectlyPopulated() {
        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."), manager.getStoredMessageTexts());
    }

    @Test
    void messageHashArrayCorrectlyPopulated() {
        assertEquals(List.of("00:0:DIDCAKE", "11:1:WHERETIME", "22:2:YOHOOOOGATE",
                "08:3:ITTIME", "44:4:OKYOU"), manager.getMessageHashes());
    }

    @Test
    void messageIdArrayCorrectlyPopulated() {
        assertEquals(List.of("0011223344", "1122334455", "2233445566", "0838884567", "4455667788"),
                manager.getMessageIDs());
    }

    @Test
    void unknownFlagIsNotAdded() {
        assertFalse(manager.addMessage(message1, "Unknown"));
        assertEquals(5, manager.getMessageIDs().size());
    }

    @Test
    void storedMessagesAreReadBackFromJsonFile() {
        message2.storeMessage();
        message5.storeMessage();

        MessageManager freshManager = new MessageManager();
        assertEquals(2, freshManager.loadStoredMessages());
        assertEquals(manager.getStoredMessageTexts(), freshManager.getStoredMessageTexts());
        // Loading again must not create duplicates
        assertEquals(0, freshManager.loadStoredMessages());
    }

    // ---------- Display, longest message and searches ----------

    @Test
    void displaySenderAndRecipientOfStoredMessages() {
        assertEquals("Sender: Kyle Smith | Recipient: +27838884567\n"
                + "Sender: Kyle Smith | Recipient: +27838884567",
                manager.getStoredSendersAndRecipients(SENDER));
    }

    @Test
    void displayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.getLongestStoredMessage());
    }

    @Test
    void longestMessageWhenNothingStored() {
        assertEquals("No stored messages.", new MessageManager().getLongestStoredMessage());
    }

    @Test
    void searchForMessageId() {
        Message found = manager.searchByMessageID("0838884567");
        assertNotNull(found);
        assertEquals("It is dinner time!", found.getMessageText());
        assertEquals("0838884567", found.getRecipient());
    }

    @Test
    void searchForMessageIdNotFound() {
        assertNull(manager.searchByMessageID("9999999999"));
    }

    @Test
    void searchMessagesForRecipient() {
        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."), manager.searchByRecipient("+27838884567"));
    }

    @Test
    void searchMessagesForRecipientIgnoresDisregarded() {
        assertTrue(manager.searchByRecipient("+27834484567").isEmpty());
    }

    // ---------- Delete and report ----------

    @Test
    void deleteMessageUsingHash() {
        message2.storeMessage();
        message5.storeMessage();

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                manager.deleteByHash("11:1:WHERETIME"));

        // Removed from the stored array and the parallel arrays
        assertEquals(List.of("Ok, I am leaving without you."), manager.getStoredMessageTexts());
        assertFalse(manager.getMessageHashes().contains("11:1:WHERETIME"));
        assertFalse(manager.getMessageIDs().contains("1122334455"));
        assertNull(manager.searchByMessageID("1122334455"));

        // ... and from the JSON file
        assertEquals(1, Message.readStoredMessages().size());
    }

    @Test
    void deleteMessageUsingUnknownHash() {
        assertEquals("No stored message found with that message hash.", manager.deleteByHash("00:0:NOTHING"));
        assertEquals(2, manager.getStoredMessageTexts().size());
    }

    @Test
    void displaySentMessagesReport() {
        String report = manager.getSentReport();
        assertTrue(report.contains("00:0:DIDCAKE"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("08:3:ITTIME"));
        assertTrue(report.contains("It is dinner time!"));
        assertFalse(report.contains("Where are you?"));
    }

    @Test
    void displayStoredMessagesReport() {
        String report = manager.getStoredReport();
        assertTrue(report.contains("11:1:WHERETIME"));
        assertTrue(report.contains("1122334455"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("44:4:OKYOU"));
        assertTrue(report.contains("Ok, I am leaving without you."));
        assertFalse(report.contains("Did you get the cake?"));
    }

    @Test
    void reportWhenNoMessages() {
        assertTrue(new MessageManager().getStoredReport().contains("No messages to display."));
    }
}