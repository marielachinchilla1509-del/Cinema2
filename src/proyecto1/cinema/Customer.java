package proyecto1.cinema;

import java.util.Date;

public class Customer extends Person {

     private boolean vip;
     
    private String customerId;

    private Invoice[] invoices;

    public Customer(boolean vip, String customerId, Invoice[] invoices,
            boolean disability, Date birthDay, String email, String id, 
            String name, String phoneNumber) {
        super(disability, birthDay, email, id, name, phoneNumber);
        this.vip = false;
        this.customerId = customerId;
        this.invoices = invoices;
    }
    

    public Customer() {
        boolean vip= false;
        String customerId= "Not registered";
       Invoice[] invoices= new Invoice[0];
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Invoice[] getInvoices() {
        return invoices;
    }

    public void setInvoices(Invoice[] invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return "Customer{" + "vip=" + vip + ", customerId=" + customerId + 
                ", invoices=" + invoices + '}';
    }
    
    

    
}
