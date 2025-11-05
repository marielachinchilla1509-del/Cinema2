
package proyecto1.cinema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @author Mariela
 * @author Astrid
 * @author Buso
 * @author Kendall
 * 
 * <h2>.TXT DOCUMENTS READING AND WRITING FOR CINEMA PROJECT.<h2>
 * 
 * <p>
 * This project allows maineaining order in diferent aspects of cinema such as
 * stock control and movie management.
 * <p>
 * 
 */
public class Proyecto1Cinema {

    /**
     * Scanner for reading input from the console.
     * 
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CustomerModule customerModule = new CustomerModule();
        EmployeeModule em = new EmployeeModule(); 
        ProductModule pm = new ProductModule();
        TicketSalesModule tsm= new TicketSalesModule();
        Product p = new Product();

        /*
        *Create customer module
        
        */
        int option;
        
         do {
            System.out.println("\n=== CINEMA MENU ===");
            System.out.println("1) Customer module");
            System.out.println("2) Employee module");
            System.out.println("3) Product module");
            System.out.println("4) Ticket sales module");
            System.out.println("0) Exit");
            option = Integer.parseInt(br.readLine());
            
            switch (option){
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
                    tsm.showmenu();
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    break;
                default:
            }
    
         }while(option != 0);
         
    }
}

