import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TicketPoolV3 implements TicketPoolInterface{
    private final CustomBlockingQueue<Ticket> queue;
    private final ReadWriteLock statsLock = new ReentrantReadWriteLock();
    private int noOfTicketSold = 0;
    private final int totalTicketsOnSale;
    private int noOfTicketOffered = 0;
    String green = "\u001B[32m";
    String yellow = "\u001B[33m";
    String purple = "\u001B[35m";
    String blue = "\u001B[34m";
    String defaultColor = "\u001B[31m";
    public TicketPoolV3(int queueSize, int totalTicketsOnSale) {
        this.queue = new CustomBlockingQueue<>(queueSize);
        this.totalTicketsOnSale = totalTicketsOnSale;
    }

    public void addTicket(Ticket ticket) {

        try {
            queue.put(ticket);
            statsLock.writeLock().lock();
            noOfTicketOffered++;
            System.out.println(purple + ticket.getVendorName() + " Vendor added: " + ticket.getTicketNumber() + " for " + ticket.getEventName());
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted.");
        }finally {
            statsLock.writeLock().unlock();
        }
    }

    public Ticket purchaseTicket() {

        try {
            Ticket ticket = queue.take();
            statsLock.writeLock().lock();

            noOfTicketSold++;
            return ticket;
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted.");
        }finally {
            statsLock.writeLock().unlock();
        }
        return null;
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
        statsLock.readLock().lock();
        try {
            if (noOfTicketOffered > 0 && noOfTicketOffered == totalTicketsOnSale ) {
                System.out.println(green + "All tickets have been sold. Stopping status updates. " + totalTicketsOnSale+ defaultColor);

                return true;
            }
        } finally {
            statsLock.readLock().unlock();
        }
        return false;
    }
}
