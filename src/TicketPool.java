import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//ReentrantReadWriteLock
public class TicketPool implements TicketPoolInterface{

    private final Queue<Ticket> queue = new LinkedList<Ticket>();
    String green = "\u001B[32m";
    String yellow = "\u001B[33m";
    String purple = "\u001B[35m";
    String defaultColor = "\u001B[30m";
    String blue = "\u001B[34m";
    private final int queueSize;
    private int noOfTicketSold = 0;
    private int noOfTicketOffered = 0;

    ReadWriteLock lock = new ReentrantReadWriteLock(true);
    Lock writeLock = lock.writeLock();
    Condition QueueEmpty = writeLock.newCondition();
    Condition QueueFull = writeLock.newCondition();

    public TicketPool(int queueSize) {
        super();
        this.queueSize = queueSize;
    }

    // Vendor (Producer) calls addTicket() method
    public void addTicket(Ticket ticket) {
        try {
            writeLock.lock();

            while(queue.size() == queueSize) {
                try {
                    QueueFull.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Properly handle the interruption
                    System.out.println("Thread was interrupted.");
                }
            }

            queue.offer(ticket); // ticket is added to queue
            noOfTicketOffered++;
            System.out.println(purple + ticket.getVendorName() + "Vendor added: " + ticket.getTicketNumber() + " for " + ticket.getEventName());

            QueueEmpty.signalAll();

        } finally {
            writeLock.unlock();
        }

    }

    //Customer (Consumer) calls purchaseTicket() method
    public Ticket purchaseTicket() {
        try {
            writeLock.lock();
            while (queue.isEmpty()) {
                try {
                    System.out.println("Queue is empty. Waiting for tickets...");
                    QueueEmpty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread was interrupted.");
                }
            }

            Ticket ticket = queue.poll();
            noOfTicketSold++;

            System.out.println("Ticket " + (ticket != null ? ticket.getTicketNumber() : "NULL") + " has been sold.");

            QueueFull.signalAll();
            return ticket;
        } finally {
            writeLock.unlock();
        }
    }


    //Reader method to check the current status of ticket sales
    public  void printStatus() {
        try {
            lock.readLock().lock();
            System.out.println(yellow + "Current Sales Status of Tickets");
            System.out.println( "No of ticket purchased " + noOfTicketSold);
            System.out.println( "No of ticket added to the queue " + noOfTicketOffered);
            System.out.println( "No of tickets currently available to sale " + queue.size() + defaultColor);
        } finally {
            lock.readLock().unlock();
        }

    }

    public boolean isSalesComplete() {
        try {
            lock.readLock().lock();
            if (noOfTicketOffered > 0 && noOfTicketOffered == noOfTicketSold) {
                System.out.println(green + "All tickets have been sold. Stopping status updates." + defaultColor);

                return noOfTicketSold == noOfTicketOffered;
            }
        } finally {
            lock.readLock().unlock();
        }
        return false;
    }

}
