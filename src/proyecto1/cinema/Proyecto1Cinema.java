package proyecto1.cinema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Mariela
 * @author Astrid
 * @author Buso
 * @author Kendall
 *
 * <h2>TXT DOCUMENTS READING AND WRITING FOR CINEMA PROJECT</h2>
 * <p>
 * This project allows maintaining order in different aspects of a cinema,
 * including customer records, employee management, product inventory and
 * ticket sales.
 * </p>
 * <p>
 * The system operates using console menus and stores data through .txt files.
 * </p>
 */
public class Proyecto1Cinema {

    /*
     * Entry point of the system. Displays the main menu where the user can access
     * each module related to cinema management.
     *
     * @param args command-line arguments
     * @throws IOException if an input/output error occurs while reading user input
     */
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Module instances
        CustomerModule customerModule = new CustomerModule();
        EmployeeModule em = new EmployeeModule();
        ProductModule pm = new ProductModule();
        TicketSalesModule tsm = new TicketSalesModule();

        int option;

        // Main menu loop
        do {
            System.out.println("\n=== CINEMA MENU ===");
            System.out.println("1) Customer module");
            System.out.println("2) Employee module");
            System.out.println("3) Product module");
            System.out.println("4) Ticket sales module");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");

            option = Integer.parseInt(br.readLine());

            switch (option) {
                case 1:
                    customerModule.showMenu();
                    break;
                case 2:
                    em.showMenu();
                    break;
                case 3:
                    pm.showMenu();
                    break;
                case 4:
                    tsm.showMenu();
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != 0);
    }

}
