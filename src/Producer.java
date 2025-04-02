import java.util.Random;

public class Producer implements Runnable{

    private final TicketPoolInterface ticketPool;
    private final int totalTicketsToProduce;
    private final String vendorName;
    private final String eventName;

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
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted.");
            }
        }
    }
}
