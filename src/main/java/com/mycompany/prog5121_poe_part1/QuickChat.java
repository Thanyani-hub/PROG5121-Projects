import java.util.List;
import java.util.Scanner;

/** Console menu for QuickChat. Runs after a successful login. */
public class QuickChat {

    /**
     * Starts QuickChat for a logged-in user.
     *
     * @param in     the console scanner
     * @param sender the logged-in user's name, shown as the sender of messages
     */
    public static void start(Scanner in, String sender) {
        System.out.println("Welcome to QuickChat.");

        // Part 3: previously stored messages are read from messages.json into the stored array
        MessageManager manager = new MessageManager();
        int loaded = manager.loadStoredMessages();
        if (loaded > 0) {
            System.out.println(loaded + " stored message(s) loaded from messages.json.");
        }

        int maxMessages = readInt(in, "How many messages do you wish to enter? ");
        boolean messagesEntered = false;
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int option = readInt(in, "Choose an option: ");

            switch (option) {
                case 1:
                    if (messagesEntered) {
                        System.out.println("You have already entered your " + maxMessages + " messages.");
                    } else {
                        sendMessages(in, maxMessages, manager);
                        messagesEntered = true;
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                case 4:
                    storedMessagesMenu(in, manager, sender);
                    break;
                default:
                    System.out.println("Please choose 1, 2, 3 or 4.");
            }
        }
    }

    private static void sendMessages(Scanner in, int maxMessages, MessageManager manager) {
        for (int i = 0; i < maxMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + maxMessages + " ---");

            String recipient;
            do {
                System.out.print("Recipient cell number (e.g. +27718693002): ");
                recipient = in.nextLine().trim();
                if (!Message.isValidRecipient(recipient)) {
                    System.out.println("Cell phone number is incorrectly formatted or does not "
                            + "contain an international code. Please correct the number and try again.");
                }
            } while (!Message.isValidRecipient(recipient));

            String text;
            while (true) {
                System.out.print("Enter your message: ");
                text = in.nextLine().trim();
                if (text.isEmpty()) {
                    System.out.println("Message cannot be empty.");
                } else if (text.length() > 250) {
                    System.out.println("Please enter a message of less than 250 characters.");
                    System.out.println(Message.checkMessageLength(text));
                } else {
                    System.out.println("Message sent");
                    break;
                }
            }

            Message message = new Message(i, recipient, text);
            System.out.println(message.idGeneratedMessage());

            System.out.println("1) Send Message  2) Disregard Message  3) Store Message to send later");
            int choice;
            do {
                choice = readInt(in, "Choose: ");
                if (choice < 1 || choice > 3) {
                    System.out.println("Please choose 1, 2 or 3.");
                }
            } while (choice < 1 || choice > 3);

            System.out.println(message.SentMessage(choice));

            // Part 3: add the message to the matching array (sent, disregarded or stored)
            manager.addMessage(message, flagFor(choice));

            if (choice == 2) {
                System.out.print("> ");
                if (in.nextLine().trim().equals("0")) {
                    System.out.println("Message deleted.");
                }
            } else if (choice == 1) {
                System.out.println(message.getDetails());
            }
        }
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessagess());
    }

    /** Converts the send/disregard/store menu choice into the flag used by MessageManager. */
    private static String flagFor(int choice) {
        return switch (choice) {
            case 1 -> MessageManager.FLAG_SENT;
            case 2 -> MessageManager.FLAG_DISREGARD;
            default -> MessageManager.FLAG_STORED;
        };
    }

    /** Part 3: the "Stored Messages" menu. */
    private static void storedMessagesMenu(Scanner in, MessageManager manager, String sender) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("a) Display the sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search for all messages for a particular recipient");
            System.out.println("e) Delete a message using its message hash");
            System.out.println("f) Display a report of all stored messages");
            System.out.println("g) Display a report of all sent messages");
            System.out.println("x) Back to the main menu");
            System.out.print("Choose an option: ");

            switch (in.nextLine().trim().toLowerCase()) {
                case "a":
                    System.out.println(manager.getStoredSendersAndRecipients(sender));
                    break;
                case "b":
                    System.out.println(manager.getLongestStoredMessage());
                    break;
                case "c":
                    searchByMessageID(in, manager);
                    break;
                case "d":
                    searchByRecipient(in, manager);
                    break;
                case "e":
                    System.out.print("Enter the message hash to delete: ");
                    System.out.println(manager.deleteByHash(in.nextLine().trim()));
                    break;
                case "f":
                    System.out.println(manager.getStoredReport());
                    break;
                case "g":
                    System.out.println(manager.getSentReport());
                    break;
                case "x":
                    back = true;
                    break;
                default:
                    System.out.println("Please choose a, b, c, d, e, f, g or x.");
            }
        }
    }

    private static void searchByMessageID(Scanner in, MessageManager manager) {
        System.out.print("Enter the message ID: ");
        Message found = manager.searchByMessageID(in.nextLine().trim());
        if (found == null) {
            System.out.println("No message found with that message ID.");
        } else {
            System.out.println("Recipient: " + found.getRecipient());
            System.out.println("Message: " + found.getMessageText());
        }
    }

    private static void searchByRecipient(Scanner in, MessageManager manager) {
        System.out.print("Enter the recipient's cell number: ");
        String recipient = in.nextLine().trim();
        List<String> found = manager.searchByRecipient(recipient);
        if (found.isEmpty()) {
            System.out.println("No messages found for " + recipient + ".");
        } else {
            for (String text : found) {
                System.out.println("- " + text);
            }
        }
    }

    private static int readInt(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}