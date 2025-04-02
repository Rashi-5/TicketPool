public class Ticket {

    private final String ticketNumber;
    private final String vendorName;
    private final String eventName;

    public Ticket(String ticketNumber, String vendorName, String eventName) {
        super();
        this.ticketNumber = ticketNumber;
        this.vendorName = vendorName;
        this.eventName = eventName;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public String toString() {
        return "Ticket [ticketNumber=" + ticketNumber + ", vendorName=" + vendorName + ", eventName=" + eventName + "]";
    }


}
