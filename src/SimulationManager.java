public class SimulationManager {
    private final TicketPoolInterface ticketPool;
    private final int totalTicketsOnSale;
    private final String vendorName;
    private final String eventName;

    public SimulationManager(TicketPoolInterface ticketPool, int totalTicketsOnSale, String vendorName, String eventName) {
        this.ticketPool = ticketPool;
        this.totalTicketsOnSale = totalTicketsOnSale;
        this.vendorName = vendorName;
        this.eventName = eventName;
    }

    public void startSimulation() {
        // Create producer, consumer, and reader threads
        Thread vendorThread = new Thread(new Producer(ticketPool, totalTicketsOnSale, vendorName, eventName));
        Thread customerThread = new Thread(new Consumer(ticketPool, totalTicketsOnSale));
        Thread statusCheckerThread = new Thread(new Reader(ticketPool));

        // Start threads
        vendorThread.start();
        customerThread.start();
        statusCheckerThread.start();
    }
}
