package proyecto1.cinema;

import java.io.*;
import java.util.*;

/**
 * @author Kendall
 * @author Carolina
 * 
 * TicketSalesModule manages ticket and snack sales for the cinema.
 * It allows registering new sales, viewing and saving them in a .txt file.
 */
public class TicketSalesModule {

    private final Ticket[] tickets = new Ticket[100]; // static array for storing tickets
    private int counter = 0;
    private final Invoice[] invoices = new Invoice[50];
    private int invoiceCounter = 0;
    
    Scanner sc = new Scanner(System.in);

    public void showMenu() {
        int option;

        do {
            System.out.println("\n=== TICKET SALES MODULE ===");
            System.out.println("1) Sell ticket");
            System.out.println("2) Sell product");
            System.out.println("3) Ticket list");
            System.out.println("4) Save ticket list");
            System.out.println("5) Load ticket list");
            System.out.println("6) Create invoice");
            System.out.println("7) Invoice list");
            System.out.println("0) Back to main menu");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    sellTicket();
                    break;
                case 2:
                    sellProduct();
                    break;
                case 3:
                    ticketList();
                    break;
                case 4:
                    saveTickets();
                    break;
                case 5:
                    loadTickets();
                    break;
                case 6:
                    createInvoice();
                    break;
                case 7:
                    invoiceList();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while (option != 0);
    }

    /**
     * Method to sell a movie ticket.
     */
    private void sellTicket() {
        if (counter >= tickets.length) {
            System.out.println("No more space for tickets.");
            return;
        }

        Ticket t = new Ticket();
        System.out.println("Enter ticket ID:");
        t.setTicketId(sc.nextLine());

        System.out.println("Enter movie title:");
        t.setMovieTitle(sc.nextLine());

        System.out.println("Enter room number:");
        t.setRoomNumber(sc.nextLine());

        System.out.println("Enter seat number:");
        t.setSitNumber(sc.nextLine());

        System.out.println("Enter show time (e.g. 7:30 PM):");
        t.setShowTime(sc.nextLine());

        System.out.println("Enter ticket type (e.g. Adult, Child, Senior):");
        t.setType(sc.nextLine());

        System.out.println("Enter price:");
        t.setPrice(sc.nextDouble());
        sc.nextLine();

        System.out.println("Enter payment method (Card/Cash):");
        t.setPaymentMethod(sc.nextLine());

        t.setDate(new Date());
        t.setStatus("Sold");

        tickets[counter] = t;
        counter++;

        System.out.println("Ticket sold successfully!");
    }

    /**
     * Method to sell snacks or products (e.g. popcorn, soda, etc.).
     */
    private void sellProduct() {
        if (counter >= tickets.length) {
            System.out.println("No more space for tickets.");
            return;
        }

        double total = 0;
        int option;
        do {
            System.out.println("\n=== PRODUCT MENU ===");
            System.out.println("1) Popcorn - $3.00");
            System.out.println("2) Soda - $2.00");
            System.out.println("3) Candy - $1.50");
            System.out.println("4) Nachos - $3.50");
            System.out.println("0) Finish purchase");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    total += 3.00;
                    System.out.println("Added Popcorn.");
                    break;
                case 2:
                    total += 2.00;
                    System.out.println("Added Soda.");
                    break;
                case 3:
                    total += 1.50;
                    System.out.println("Added Candy.");
                    break;
                case 4:
                    total += 3.50;
                    System.out.println("Added Nachos.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (option != 0);

        if (total > 0) {
            Ticket t = new Ticket();
            System.out.println("Enter purchase ID:");
            t.setTicketId(sc.nextLine());
            t.setMovieTitle("Snack Purchase");
            t.setPrice(total);
            t.setDate(new Date());
            t.setStatus("Paid");
            t.setPaymentMethod("Cash");
            t.setType("Product");

            tickets[counter] = t;
            counter++;

            System.out.println("Total purchase: $" + total);
            System.out.println("Products sold successfully!");
        } else {
            System.out.println("No products selected.");
        }
    }

    /**
     * Displays all tickets and product sales.
     */
    private void ticketList() {
        if (counter == 0) {
            System.out.println("No tickets registered.");
            return;
        }
        System.out.println("\n=== TICKET LIST ===");
        for (int i = 0; i < counter; i++) {
            Ticket t = tickets[i];
            System.out.println((i + 1) + ") ID: " + t.getTicketId()
                    + " | Title: " + t.getMovieTitle()
                    + " | Type: " + t.getType()
                    + " | Price: $" + t.getPrice()
                    + " | Date: " + t.getDate());
        }
    }
    
   public Ticket findById(String id) {
    for (int i = 0; i < counter; i++) {
        if (tickets[i] != null && tickets[i].getTicketId().equals(id)) {
            return tickets[i];
        }
    }
    return null; // we dont found de customer
}



    /**
     * Saves all ticket data to a .txt file.
     */
    private void saveTickets() {
        if (counter == 0) {
            System.out.println("No tickets to save.");
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter("tickets.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("=== CINEMA TICKET SALES ===");
            for (int i = 0; i < counter; i++) {
                Ticket t = tickets[i];
                printWriter.println((i + 1) + ") ID: " + t.getTicketId()
                        + " | Movie: " + t.getMovieTitle()
                        + " | Type: " + t.getType()
                        + " | Price: $" + t.getPrice()
                        + " | Date: " + t.getDate()
                        + " | Status: " + t.getStatus());
            }
            printWriter.close();
            System.out.println("Tickets saved successfully in tickets.txt!");
        } catch (IOException e) {
            System.out.println("Error saving tickets: " + e.getMessage());
        }
    }

    /**
     * Loads and displays tickets from the saved .txt file.
     */
    private void loadTickets() {
        try {
            File file = new File("tickets.txt");
            if (!file.exists()) {
                System.out.println("No saved ticket file found.");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            System.out.println("\n=== LOADED TICKET LIST ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            System.out.println("\nFile loaded successfully!");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void addTicket(Ticket t) {
        if (counter < tickets.length) {
            tickets[counter++] = t;
            System.out.println("Ticket added successfully: " + t.getMovieTitle());
        } else {
            System.out.println("No more space for tickets!");
        }
    }

    private void createInvoice() {
        if (invoiceCounter >= invoices.length) {
            System.out.println("No more space for invoices.");
            return;
        }

        if (counter == 0) {
            System.out.println("No tickets available to generate an invoice.");
            return;
        }

        System.out.println("Enter customer ID:");
        String id = sc.nextLine();

        CustomerModule cm = new CustomerModule();
        Customer customer = cm.getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Enter invoice number:");
        String invoiceNumber = sc.nextLine();

        System.out.println("Enter payment method (Card/Cash):");
        String paymentMethod = sc.nextLine();

        System.out.println("Membership discount available? (true/false):");
        boolean membership = sc.nextBoolean();
        sc.nextLine();

        Invoice inv = new Invoice(
                membership,
                new Date(),
                paymentMethod,
                invoiceNumber,
                tickets
        );

        invoices[invoiceCounter] = inv;
        invoiceCounter++;

        // ADD INVOICE TO CUSTOMER ARRAY
        customer.addInvoice(inv);

        // PRINT INVOICE
        System.out.println("\n=== INVOICE CREATED ===");
        inv.printInvoice();

        // ===== SAVE INVOICES TO FILE =====
        try {
            FileWriter fw = new FileWriter("invoices.txt");
            PrintWriter pw = new PrintWriter(fw);

            pw.println("=== INVOICE RECORDS ===");
            for (int i = 0; i < invoiceCounter; i++) {
                Invoice invoice = invoices[i];
                pw.println((i + 1) + ") Invoice#: " + invoice.getInvoiceNumber()
                        + " | Date: " + invoice.getDate()
                        + " | Payment: " + invoice.getPaymentMethod()
                        + " | Membership: " + invoice.isMembership());
            }

            pw.close();
            System.out.println("Invoices saved successfully in invoices.txt!");
        } catch (IOException e) {
            System.out.println("Error saving invoices: " + e.getMessage());
        }
    }

    private void invoiceList() {
        if (invoiceCounter == 0) {
            System.out.println("No invoices registered.");
            return;
        }

        System.out.println("\n=== INVOICE LIST ===");
        for (int i = 0; i < invoiceCounter; i++) {
            Invoice inv = invoices[i];
            System.out.println((i + 1) + ") Invoice#: " + inv.getInvoiceNumber()
                    + " | Date: " + inv.getDate()
                    + " | Payment: " + inv.getPaymentMethod()
                    + " | Membership: " + inv.isMembership());
        }
    }
}