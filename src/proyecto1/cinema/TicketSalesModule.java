package proyecto1.cinema;

import java.io.*;
import java.util.*;

/**
 * TicketSalesModule manages ticket and product sales within the cinema system.
 * <p>
 * It provides functionality for selling tickets and snacks, displaying registered
 * sales, and saving or loading sales records from a .txt file.
 * </p>
 *
 * <p><b>Features included:</b></p>
 * <ul>
 *   <li>Selling movie tickets</li>
 *   <li>Selling snack products</li>
 *   <li>Listing sales records</li>
 *   <li>Saving ticket data to a .txt file</li>
 *   <li>Loading ticket data from a .txt file</li>
 * </ul>
 *
 * @author Kendall
 * @author Carolina
 */
public class TicketSalesModule {

    /*
     * Array used for storing ticket and snack purchase data.
     * Maximum storage capacity: 100 records.
     */
    private final Ticket[] tickets = new Ticket[100];

    /*
     * Counter to track the current number of stored tickets.
     */
    private int counter = 0;

    /*
     * Scanner used for reading input from console.
     */
    Scanner sc = new Scanner(System.in);

    /*
     * Displays the Ticket Sales main menu and executes the selected option.
     */
    public void showMenu() {
        int option;

        do {
            System.out.println("\n=== TICKET SALES MODULE ===");
            System.out.println("1) Sell ticket");
            System.out.println("2) Sell product");
            System.out.println("3) Ticket list");
            System.out.println("4) Save ticket list");
            System.out.println("5) Load ticket list");
            System.out.println("0) Back to main menu");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> sellTicket();
                case 2 -> sellProduct();
                case 3 -> ticketList();
                case 4 -> saveTickets();
                case 5 -> loadTickets();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (option != 0);
    }

    /*
     * Registers and stores a new movie ticket sale.
     * <p>If the array is full, the system shows an alert message.</p>
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

        tickets[counter++] = t;
        System.out.println("Ticket sold successfully!");
    }

    /*
     * Allows selling snack products such as popcorn, soda, candy, and nachos.
     * <p>
     * The total purchase amount is automatically calculated.
     * The sale is stored as a Ticket object of type "Product".
     * </p>
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
                case 1 -> { total += 3.00; System.out.println("Added Popcorn."); }
                case 2 -> { total += 2.00; System.out.println("Added Soda."); }
                case 3 -> { total += 1.50; System.out.println("Added Candy."); }
                case 4 -> { total += 3.50; System.out.println("Added Nachos."); }
                case 0 -> {}
                default -> System.out.println("Invalid option.");
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

            tickets[counter++] = t;

            System.out.println("Total purchase: $" + total);
            System.out.println("Products sold successfully!");
        } else {
            System.out.println("No products selected.");
        }
    }

    /*
     * Displays the list of stored tickets and product purchases.
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

    /*
     * Saves all registered ticket information to a .txt file called <b>tickets.txt</b>.
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

    /*
     * Loads ticket records from the saved file <b>tickets.txt</b>.
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

    /*
     * Adds a new ticket to the array externally.
     *
     */
    public void addTicket(Ticket t) {
        if (counter < tickets.length) {
            tickets[counter++] = t;
            System.out.println("Ticket added successfully: " + t.getMovieTitle());
        } else {
            System.out.println("No more space for tickets!");
        }
    }

    /*
     * Saves invoice information into <b>factura.txt.file.
     *
     * @param factura Invoice formatted string to store
     */
    public void saveInvoiceToTxt(String factura) {
        try {
            FileWriter fw = new FileWriter("factura.txt", true);
            fw.write(factura);
            fw.write("\n-------------------------------------\n\n");
            fw.close();
            System.out.println("Factura guardada como factura.txt");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar la factura.");
        }
    }
}
