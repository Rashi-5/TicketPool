import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable{

    private TicketPoolInterface ticketPool;
    private int totalTicketsToProduce;
    private String vendorName;
    private String eventName;

    public Producer(TicketPoolInterface ticketPool, int totalTicketsToProduce, String vendorName, String eventName) {
        this.ticketPool = ticketPool;
        this.totalTicketsToProduce = totalTicketsToProduce;
        this.vendorName = vendorName;
        this.eventName = eventName;
    }

    @Override
    public void run() {
        for (int i = 1; i <= totalTicketsToProduce; i++) {
            Ticket ticket = new Ticket("Ticket-" + i, vendorName, eventName);
            ticketPool.addTicket(ticket);

            try {
                Thread.sleep(new Random().nextInt(500)); // Simulating delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
