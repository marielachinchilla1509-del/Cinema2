
package proyecto1.cinema;

import java.io.BufferedReader;
import java.io.*;
import java.io.File;
import java.io.IOException;
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
    public static final Scanner SC = new Scanner(System.in);
    private static Object br;
    
    /*
    *Main method that starts the execution of the program.
    */
    public static void main(String[] args) {
         try {
            menuPrincipal();
        } catch (IOException e) {
            System.out.println("General error: " + e.getMessage());
        }
    }
    
    /*
     *@throws IOException if an input/output error occurs during execution
    */
    public static void menuPrincipal() throws IOException {
        int opcion;
        do {
            System.out.println("\n=== CINEMA MENU ===");
            System.out.println("1) Customer module");
            System.out.println("2) Employee module");
            System.out.println("3) Product module");
            System.out.println("0) Salir");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    employeeMenu();
                    break;
                case 3:
                    productMenu();
                case 0:
                    System.out.println("Program finished.");
                    break;
                default:
                     System.out.println("Invalid option.");
            }
        } while (opcion != 0);
    }
        

     public static void customerMenu() {
       System.out.println("\n=== CINEMA MENU ===");
       System.out.println("(1) Register customer.");
       System.out.println("(2) Add membership.");
       System.out.println("0) Salir");
       System.out.print("Opcion: ");
       opcion = parseIntSafe(Scanner.readLine(), -1);
    }
     
      public static void employeeMenu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
       public static void productMenu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
       public static int leerEntero() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   
}
