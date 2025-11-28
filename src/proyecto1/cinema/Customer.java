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
    private Invoice[] invoices; // Asegúrate de que la clase Invoice exista

    
    // =================================================================
    // CONSTRUCTOR SIMPLIFICADO (Usado por CustomerUI y SellProductUI)
    // =================================================================
    /*
     * Constructor principal que la interfaz de usuario usará para crear un cliente.
     * Asigna valores por defecto a los campos complejos de Person (disability=false, birthDay=null).
     * El orden de los parámetros que SÍ pasamos es: (customerId, name, email, phoneNumber, vip)
     */
    public Customer(String customerId, String name, String email, String phoneNumber, boolean vip) {
        
        // La llamada a super() DEBE ser la primera línea.
        // Los argumentos deben coincidir con: (disability, birthDay, email, id, name, phoneNumber)
        // Usamos 'false', 'null' y 'customerId' como el ID de Persona.
        super(false, null, email, customerId, name, phoneNumber); 
        
        this.customerId = customerId;
        this.vip = vip;
        this.invoices = new Invoice[0]; // Inicialización por defecto
    }

    
    // =================================================================
    // CONSTRUCTOR COMPLETO (Línea 32 original)
    // =================================================================
    /*
     * Constructor completo que recibe todos los datos de Person y Customer.
     */
    public Customer(String customerId, boolean vip, Invoice[] invoices,
                    boolean disability, Date birthDay, String email, 
                    String personId, String name, String phoneNumber) {
        
        // 1. LLAMADA A SUPER: DEBE SER LA PRIMERA LÍNEA.
        // Argumentos requeridos por Person: (disability, birthDay, email, id, name, phoneNumber)
        super(disability, birthDay, email, personId, name, phoneNumber); 
        
        // 2. Inicialización de campos de Customer (después de super())
        this.customerId = customerId;
        this.vip = vip;
        this.invoices = invoices;
    }


    /*
     Default constructor initializing values with default or unregistered
     states.
     */
    public Customer() {
        // Llama al constructor por defecto de Person
        super(); 
        this.vip = false;
        this.customerId = "Not registered";
        this.invoices = new Invoice[0];
    }

    public Customer(String id, String name, String email, String phone, String membership) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*
     * Retrieves customer VIP status.
     */
    public boolean isVip() {
        return vip;
    }
    // Modifies customer VIP status
    public void setVip(boolean vip) {
        this.vip = vip;
    }
    // Retrieves the customer unique ID.
    public String getCustomerId() {
        return customerId;
    }
    // Updates the customer unique ID
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    // Retrieves customer invoice records.
    public Invoice[] getInvoices() {
        return invoices;
    }
    // Updates invoice records linked to the customer.
    public void setInvoices(Invoice[] invoices) {
        this.invoices = invoices;
    }
    // Returns a readable representation of customer information.

    @Override
    public String toString() {
        // Incluir la información de la superclase (Person)
        return "Customer{" + "vip=" + vip + ", customerId=" + customerId +
                 ", invoices=" + invoices + "} Person: " + super.toString();
    }

    public String getMembership() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}