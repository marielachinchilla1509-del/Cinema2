package proyecto1.cinema;

import java.util.Date;


/*
 * Represents a customer in the cinema system.
 * Inherits basic personal information from the Person class
 * and includes attributes related to VIP status, customer identification,
 * and associated invoices.
 */
public class Customer extends Person {

    private boolean vip;

    private String customerId;

    private Invoice[] invoices;

    /*
     * Creates a Customer with specified values and inherited person data.
     * Initializes VIP status, internal ID, and invoice record.
     */
    public Customer(boolean vip, String customerId, Invoice[] invoices,
            boolean disability, Date birthDay, String email, String id,
            String name, String phoneNumber) {
        super(disability, birthDay, email, id, name, phoneNumber);
        this.vip = false;
        this.customerId = customerId;
        this.invoices = invoices;
    }

    /*
     Default constructor initializing values with default or unregistered
      states.
     */
    public Customer() {
        this.vip = false;
        this.customerId = "Not registered";
        this.invoices = new Invoice[0];
    }

    /*
     * Retrieves customer VIP status.
     */
    public boolean isVip() {
        return vip;
    }
//Modifies customer VIP status

    public void setVip(boolean vip) {
        this.vip = vip;
    }
//Retrieves the customer unique ID.

    public String getCustomerId() {
        return customerId;
    }
//Updates the customer unique ID

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
//Retrieves customer invoice records.

    public Invoice[] getInvoices() {
        return invoices;
    }
//Updates invoice records linked to the customer.

    public void setInvoices(Invoice[] invoices) {
        this.invoices = invoices;
    }
//Returns a readable representation of customer information.

    @Override
    public String toString() {
        return "Customer{" + "vip=" + vip + ", customerId=" + customerId +
                ", invoices=" + invoices + '}';
    }

   
   

    

    
}
