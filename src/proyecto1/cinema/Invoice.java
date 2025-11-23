package proyecto1.cinema;

import java.util.Arrays;
import java.util.Date;

/*
 * The Invoice class represents the invoice generated after a sale.
 * It contains purchase information such as payment method, date,
 * invoice number, membership status, and the list of tickets purchased.
 * 
 * @author Kendall
 */
public class Invoice {

    /* Indicates whether the customer used a membership card for discount. */
    private boolean membership;

    /* Date when the invoice was generated. */
    private Date date;

    /* Method used to pay (Cash, Card, SINPE, etc.). */
    private String paymentMethod;

    /* Unique invoice identifier. */
    private String invoiceNumber;

    /* List of tickets included in the invoice. */
    private Ticket[] tickets;

    /*
     * Full constructor to initialize an invoice with provided values.
     * 
     
     */
    public Invoice(boolean membership, Date date, String paymentMethod,
                   String invoiceNumber, Ticket[] tickets) {
        this.membership = membership;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.invoiceNumber = invoiceNumber;
        this.tickets = tickets;
    }

    /*
     * Default constructor that initializes attributes with default values.
     */
    public Invoice() {
        this.membership = false;
        this.date = null;
        this.paymentMethod = "Not registered";
        this.invoiceNumber = "Not registered";
        this.tickets = new Ticket[10];
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

    /*
     * Returns a readable representation of the invoice.
     * Also prints the list of tickets using Arrays.toString().
     * 
     * @return string representation of the invoice
     */
    @Override
    public String toString() {
        return "Invoice{" +
                "membership=" + membership +
                ", date=" + date +
                ", paymentMethod=" + paymentMethod +
                ", invoiceNumber=" + invoiceNumber +
                ", tickets=" + Arrays.toString(tickets) +
                '}';
    }
}

