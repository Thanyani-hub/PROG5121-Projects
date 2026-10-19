import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Represents one QuickChat message and keeps track of the messages sent
 * while the program is running (Part 2).
 *
 * JSON storage (Gson) based on:
 * Google (n.d.) Gson User Guide. Available at:
 * https://github.com/google/gson/blob/main/UserGuide.md
 * (Accessed: <add the date you accessed it>).
 *
 * Regex syntax based on:
 * Oracle (n.d.) Class Pattern. Java Platform, Standard Edition 8 API
 * Specification. Available at:
 * https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
 */
public class Message {

    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+27\\d{9}$");
    private static final int MAX_LENGTH = 250;
    private static final String JSON_FILE = "messages.json";
    private static final Random RANDOM = new Random();

    // Messages sent while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    /** Creates a message with an auto-generated 10-digit ID. */
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    /** Creates a message with a supplied ID (used for testing). */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    /** Generates a random ten-digit number (leading zeros allowed) as a String. */
    private static String generateMessageID() {
        long number = (long) (RANDOM.nextDouble() * 10_000_000_000L);
        String padded = String.format("%010d", number);
        return padded.substring(0, 10);
    }

    /** Message ID must be no more than ten characters. */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String idGeneratedMessage() {
        return "Message ID generated: " + messageID;
    }

    /** Static helper so a cell number can be checked before a Message exists. */
    public static boolean isValidRecipient(String cell) {
        return cell != null && CELL_PATTERN.matcher(cell).matches();
    }

    public String checkRecipientCell() {
        if (isValidRecipient(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    /** Checks the 250 character limit and returns the status message. */
    public static String checkMessageLength(String text) {
        if (text.length() <= MAX_LENGTH) {
            return "Message ready to send.";
        }
        int excess = text.length() - MAX_LENGTH;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /** Hash = first two digits of ID : message number : FIRSTWORDLASTWORD (all caps). */
    public final String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    /** 1 = send, 2 = disregard, 3 = store. */
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    /** Returns every message sent while the program is running. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() {
        return totalMessagesSent;
    }

    public static void clearSentMessages() {
        sentMessages.clear();
        totalMessagesSent = 0;
    }

    /** Appends this message to messages.json. */
    public void storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
        List<Message> stored = new ArrayList<>();

        try (FileReader reader = new FileReader(JSON_FILE)) {
            List<Message> existing = gson.fromJson(reader, listType);
            if (existing != null) {
                stored = existing;
            }
        } catch (IOException e) {
            // File does not exist yet - start a new list
        }

        stored.add(this);

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(stored, writer);
        } catch (IOException e) {
            System.out.println("Could not store message: " + e.getMessage());
        }
    }

    /** Details in the required order: ID, Hash, Recipient, Message. */
    public String getDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}