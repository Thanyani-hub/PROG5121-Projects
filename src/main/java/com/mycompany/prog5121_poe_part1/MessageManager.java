import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Keeps all of the QuickChat message arrays for Part 3 and provides the
 * searching, deleting and reporting features of the "Stored Messages" menu.
 *
 * The arrays are filled while the program runs (nothing is hard-coded):
 *   sentMessages, disregardedMessages, storedMessages, messageIDs, messageHashes.
 * messageIDs, messageHashes and allMessages are parallel arrays - the message at
 * position n in allMessages has its ID at position n in messageIDs and its hash
 * at position n in messageHashes.
 */
public class MessageManager {

    // Flags used to decide which array a message belongs in
    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARD = "Disregard";

    private static final String NO_STORED_MESSAGES = "No stored messages.";

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();

    // Parallel arrays holding every message, its ID and its hash
    private final List<Message> allMessages = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();

    /**
     * Adds a message to the array that matches its flag (Sent, Stored or Disregard)
     * and records its ID and hash in the parallel arrays.
     *
     * @return false if the flag is not recognised (nothing is added)
     */
    public boolean addMessage(Message message, String flag) {
        switch (flag) {
            case FLAG_SENT -> sentMessages.add(message);
            case FLAG_STORED -> storedMessages.add(message);
            case FLAG_DISREGARD -> disregardedMessages.add(message);
            default -> {
                return false;
            }
        }
        allMessages.add(message);
        messageIDs.add(message.getMessageID());
        messageHashes.add(message.getMessageHash());
        return true;
    }

    /**
     * Reads the stored messages from the JSON file into the stored messages array.
     * Messages that are already in the arrays (same ID) are skipped.
     *
     * @return how many messages were loaded
     */
    public int loadStoredMessages() {
        int loaded = 0;
        for (Message message : Message.readStoredMessages()) {
            if (!messageIDs.contains(message.getMessageID())) {
                addMessage(message, FLAG_STORED);
                loaded++;
            }
        }
        return loaded;
    }

    /** Returns the text of every message in the given array. */
    private List<String> getTexts(List<Message> messages) {
        List<String> texts = new ArrayList<>();
        for (Message message : messages) {
            texts.add(message.getMessageText());
        }
        return texts;
    }

    // ---------- Read-only views of the arrays ----------

    public List<String> getSentMessageTexts() {
        return getTexts(sentMessages);
    }

    public List<String> getDisregardedMessageTexts() {
        return getTexts(disregardedMessages);
    }

    public List<String> getStoredMessageTexts() {
        return getTexts(storedMessages);
    }

    public List<String> getMessageIDs() {
        return Collections.unmodifiableList(messageIDs);
    }

    public List<String> getMessageHashes() {
        return Collections.unmodifiableList(messageHashes);
    }

    // ---------- Stored Messages menu features ----------

    /** (a) Lists the sender and recipient of every stored message. */
    public String getStoredSendersAndRecipients(String sender) {
        if (storedMessages.isEmpty()) {
            return NO_STORED_MESSAGES;
        }
        StringBuilder result = new StringBuilder();
        for (Message message : storedMessages) {
            result.append("Sender: ").append(sender)
                  .append(" | Recipient: ").append(message.getRecipient()).append("\n");
        }
        return result.toString().trim();
    }

    /** (b) Returns the text of the longest stored message. */
    public String getLongestStoredMessage() {
        Message longest = null;
        for (Message message : storedMessages) {
            if (longest == null || message.getMessageText().length() > longest.getMessageText().length()) {
                longest = message;
            }
        }
        return longest == null ? NO_STORED_MESSAGES : longest.getMessageText();
    }

    /** (c) Searches the message ID array; returns the matching message or null. */
    public Message searchByMessageID(String messageID) {
        int position = messageIDs.indexOf(messageID);
        return position == -1 ? null : allMessages.get(position);
    }

    /** (d) Returns the text of every sent or stored message for the given recipient. */
    public List<String> searchByRecipient(String recipient) {
        List<String> found = new ArrayList<>();
        for (Message message : sentMessages) {
            if (message.getRecipient().equals(recipient)) {
                found.add(message.getMessageText());
            }
        }
        for (Message message : storedMessages) {
            if (message.getRecipient().equals(recipient)) {
                found.add(message.getMessageText());
            }
        }
        return found;
    }

    /**
     * (e) Deletes a stored message using its message hash. The message is removed
     * from the stored array and the parallel arrays, and the JSON file is updated.
     */
    public String deleteByHash(String messageHash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message message = storedMessages.get(i);
            if (message.getMessageHash().equalsIgnoreCase(messageHash)) {
                storedMessages.remove(i);

                int position = allMessages.indexOf(message);
                allMessages.remove(position);
                messageIDs.remove(position);
                messageHashes.remove(position);

                Message.saveMessages(storedMessages);
                return "Message: \"" + message.getMessageText() + "\" successfully deleted.";
            }
        }
        return "No stored message found with that message hash.";
    }

    /** (f) Report with the full details of all stored messages. */
    public String getStoredReport() {
        return buildReport("STORED MESSAGES REPORT", storedMessages);
    }

    /** Report with the full details of all sent messages. */
    public String getSentReport() {
        return buildReport("SENT MESSAGES REPORT", sentMessages);
    }

    private String buildReport(String title, List<Message> messages) {
        StringBuilder report = new StringBuilder("===== " + title + " =====\n");
        if (messages.isEmpty()) {
            report.append("No messages to display.\n");
        }
        for (int i = 0; i < messages.size(); i++) {
            report.append("\nMessage ").append(i + 1).append("\n")
                  .append(messages.get(i).getDetails()).append("\n");
        }
        return report.toString();
    }
}