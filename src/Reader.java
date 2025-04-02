public class Reader implements Runnable{
    private final TicketPoolInterface ticketPool;

    public Reader(TicketPoolInterface ticketPool) {
        this.ticketPool = ticketPool;
    }


    @Override
    public void run() {
        while (true) {
            ticketPool.printStatus();

            if (ticketPool.isSalesComplete()) {
                break;
            }

            try {
                Thread.sleep(2000); // Checking status every 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted.");
            }
        }
    }
}
