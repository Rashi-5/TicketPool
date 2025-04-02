import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int queueSize = getValidInt(scanner, "Enter queue size: ");
        int totalTicketsOnSale = getValidInt(scanner, "Enter number of tickets on sale: ");
        scanner.nextLine();

        System.out.println("Enter vendor name: ");
        String vendorName = scanner.nextLine();

        System.out.println("Enter event name: ");
        String eventName = scanner.nextLine();

        int choice;
        while (true) {
            System.out.println("Select TicketPool implementation:");
            System.out.println("1 - Intrinsic Synchronized Locks ");
            System.out.println("2 - Reentrant Read-Write Lock");
            System.out.println("3 - BlockingQueue with ReadWrite Lock ");
            System.out.println("4 - Custom BlockingQueue with ReadWrite Lock");
            System.out.print("Enter your choice (1, 2, 3 or 4): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice >= 1 && choice <= 4) {
                    break;
                } else {
                    System.out.println("Invalid choice! Please enter 1, 2, 3 or 4.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number (1, 2, 3 or 4).");
                scanner.next(); // Clear the invalid input
            }
        }

        System.out.println("You selected option " + choice);
        scanner.close();

        TicketPoolInterface selectedPool = switch (choice) {
            case 1 -> new TicketPoolV1(queueSize);
            case 2 -> new TicketPool(queueSize);
            case 3 -> new TicketPoolV2(queueSize, totalTicketsOnSale);
            case 4 -> new TicketPoolV3(queueSize, totalTicketsOnSale);
            default -> {
                System.out.println("Invalid choice, defaulting to TicketPoolV1 (Synchronized).");
                yield new TicketPoolV1(queueSize);
            }
        };

        // start the simulation
        SimulationManager simulation = new SimulationManager(selectedPool, totalTicketsOnSale, vendorName, eventName);
        simulation.startSimulation();
    }

    private static int getValidInt(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value > 0) { // Ensures the input is positive
                    return value;
                } else {
                    System.out.println("Invalid input! Please enter a positive number.");
                }
            } else {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next(); // Clear the invalid input
            }
        }
    }
}