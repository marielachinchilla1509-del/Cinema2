package proyecto1.cinema;

import java.util.Date;

/**
 * Invoice class represents a billing record associated with cinema sales.
 * It contains information such as invoice number, payment method, date,
 * membership discount and the list of ticket items included in the invoice.
 */
public class Invoice {

    private boolean membership;
    private Date date;
    private String paymentMethod;
    private String invoiceNumber;
    private Ticket[] tickets;

    public Invoice(boolean membership, Date date, String paymentMethod, String invoiceNumber, Ticket[] tickets) {
        this.membership = membership;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.invoiceNumber = invoiceNumber;
        this.tickets = tickets;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Ticket[] getTickets() {
        return tickets;
    }

    /**
     * Prints a formatted invoice with all ticket items and total calculations.
     */
    public void printInvoice() {
        System.out.println("\n===== CINEMA INVOICE =====");
        System.out.println("Invoice Number: " + invoiceNumber);
        System.out.println("Date: " + date);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Membership Discount Applied: " + membership);
        System.out.println("--------- ITEMS ----------");

        double total = 0;

        for (Ticket t : tickets) {
            if (t != null) {
                System.out.println("- " + t.getMovieTitle() + " | $" + t.getPrice());
                total += t.getPrice();
            }
        }

        if (membership) {
            double discount = total * 0.10;
            total -= discount;
            System.out.println("Membership Discount (10%): -$" + discount);
        }

        System.out.println("--------------------------");
        System.out.println("TOTAL: $" + total);
        System.out.println("==========================");
    }
}

