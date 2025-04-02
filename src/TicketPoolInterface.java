public interface TicketPoolInterface {
    void addTicket(Ticket ticket);
    Ticket purchaseTicket();

    void printStatus();

    boolean isSalesComplete();
}
