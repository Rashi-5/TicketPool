import java.util.LinkedList;
import java.util.Queue;

public class TicketPoolV1 implements TicketPoolInterface{
    String green = "\u001B[32m";
    String yellow = "\u001B[33m";
    String purple = "\u001B[35m";
    String defaultColor = "\u001B[30m";

    private final Queue<Ticket> queue = new LinkedList<Ticket>();

    private final int queueSize;
    private int noOfTicketSold = 0;
    private int noOfTicketOffered = 0;

    private int totalTicketsOnSale = 0;

    public TicketPoolV1(int queueSize) {
        super();
        this.queueSize = queueSize;
    }

    // Vendor (Producer) calls this method
    public synchronized void addTicket(Ticket ticket) {
        while (queue.size() == queueSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted.");
            }
        }

        queue.offer(ticket); // ticket is added to queue
        noOfTicketOffered++;
        System.out.println(purple + ticket.getVendorName() + "Vendor added: " + ticket.getTicketNumber() + " for " + ticket.getEventName());

        notifyAll();
    }

    //Customer (Consumer) calls purchaseTicket() method
    public synchronized Ticket purchaseTicket() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted.");
            }
        }

        Ticket ticket = queue.poll();
        noOfTicketSold++;
        notifyAll();
        return ticket;
    }

    //Reader method to check the current status of ticket sales
    public synchronized void printStatus() {
        System.out.println(yellow + "Current Sales Status of Tickets");
        System.out.println("No of ticket purchased "+noOfTicketSold);
        System.out.println("No of ticket added to the queue "+noOfTicketOffered);
        System.out.println("No of tickets currently available to sale "+queue.size() + defaultColor);
    }

    public synchronized boolean isSalesComplete() {

        if (noOfTicketOffered > 0 && noOfTicketOffered == noOfTicketSold) {
            System.out.println(green + "All tickets have been sold. Stopping status updates." + defaultColor);

            return noOfTicketSold == noOfTicketOffered;
        }
        return false;
    }

}
