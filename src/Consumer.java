import java.util.Random;

public class Consumer implements Runnable{
    private final TicketPoolInterface ticketPool;
    private final int totalTicketsToBuy;
    String blue = "\u001B[34m";
    String defaultColor = "\u001B[30m";
    public Consumer(TicketPoolInterface ticketPool, int totalTicketsToBuy) {
        this.ticketPool = ticketPool;
        this.totalTicketsToBuy = totalTicketsToBuy;
    }


    @Override
    public void run() {
        for (int i = 0; i < totalTicketsToBuy; i++) {
            Ticket ticket = ticketPool.purchaseTicket();

            if (ticket != null) {
                System.out.println(blue + "Customer purchased: " + ticket.getTicketNumber() + defaultColor);
            }
            try {
                Thread.sleep(new Random().nextInt(1000)); // Simulating delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
