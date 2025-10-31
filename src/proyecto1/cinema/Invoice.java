package proyecto1.cinema;


import java.util.Date;

public class Invoice {

    private boolean membership;

    private Date date;

    private String paymentMethod;

    private String invoiceNumber;

    private Ticket[] tickets;

    public Invoice(boolean membership, Date date, String paymentMethod, 
            String invoiceNumber, Ticket[] tickets) {
        this.membership = membership;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.invoiceNumber = invoiceNumber;
        this.tickets = tickets;
    }

    public Invoice() {
        boolean membership = false;
        Date date= null;
        String paymentMethod= "Not registered";
        String invoiceNumber="Not registered";
        Ticket[] tickets = new Ticket[10];
    }

    public boolean isMembership() {
        return membership;
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Ticket[] getTickets() {
        return tickets;
    }

    public void setTickets(Ticket[] tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Invoice{" + "membership=" + membership + ", date=" + date +
                ", paymentMethod=" + paymentMethod + ", invoiceNumber=" + 
                invoiceNumber + ", tickets=" + tickets + '}';
    }
    
    

    
   
}
