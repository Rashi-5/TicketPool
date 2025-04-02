import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Hybrid approach using BlockingQueue and ReentrantReadWriteLock
public class TicketPoolV2 implements TicketPoolInterface{
    private final BlockingQueue<Ticket> queue ;
    private final ReadWriteLock statsLock = new ReentrantReadWriteLock();
    private int noOfTicketSold = 0;
    private int noOfTicketOffered = 0;
    private int queueSize;
    private int totalTicketsOnSale;
    String green = "\u001B[32m";
    String yellow = "\u001B[33m";
    String purple = "\u001B[35m";
    String defaultColor = "\u001B[31m";

    public TicketPoolV2(int queueSize, int totalTicketsOnSale) {
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.queueSize = queueSize;
        this.totalTicketsOnSale = totalTicketsOnSale;
    }

    public void addTicket(Ticket ticket) {
        try {
            // First put the ticket in the queue (this may block)
            queue.put(ticket);

            // Then update the statistics with the write lock
            statsLock.writeLock().lock();
            try {
                noOfTicketOffered++;
                System.out.println(purple + ticket.getVendorName() + " Vendor added: " + ticket.getTicketNumber() + " for " + ticket.getEventName());
            } finally {
                statsLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Ticket purchaseTicket() {
        try {
            Ticket ticket = queue.take();
            statsLock.writeLock().lock();
            try {
                noOfTicketSold++;
            } finally {
                statsLock.writeLock().unlock();
            }
            return ticket;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void printStatus() {
        statsLock.readLock().lock();
        try {
            System.out.println(yellow + "Current Sales Status of Tickets");
            System.out.println( "No of ticket purchased " + noOfTicketSold);
            System.out.println( "No of ticket added to the queue " + noOfTicketOffered);
            System.out.println( "No of tickets currently available to sale " + queue.size() + defaultColor);
        } finally {
            statsLock.readLock().unlock();
        }
    }

    public boolean isSalesComplete() {

        try{
            statsLock.readLock().lock();
            if (noOfTicketOffered > 0 && noOfTicketOffered == totalTicketsOnSale ) {
                System.out.println(green + "All tickets have been sold. Stopping status updates. " + defaultColor);
                return true;
            }
        return false;
        } finally {
            statsLock.readLock().unlock();
        }
    }

}
