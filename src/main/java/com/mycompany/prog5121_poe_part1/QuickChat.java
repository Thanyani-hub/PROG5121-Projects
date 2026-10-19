import java.util.Scanner;

/** Console menu for QuickChat. Runs after a successful login. */
public class QuickChat {

    public static void start(Scanner in) {
        System.out.println("Welcome to QuickChat.");

        int maxMessages = readInt(in, "How many messages do you wish to enter? ");
        boolean messagesEntered = false;
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int option = readInt(in, "Choose an option: ");

            switch (option) {
                case 1:
                    if (messagesEntered) {
                        System.out.println("You have already entered your " + maxMessages + " messages.");
                    } else {
                        sendMessages(in, maxMessages);
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
                default:
                    System.out.println("Please choose 1, 2 or 3.");
            }
        }
    }

    private static void sendMessages(Scanner in, int maxMessages) {
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
            int choice = readInt(in, "Choose: ");
            System.out.println(message.SentMessage(choice));

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