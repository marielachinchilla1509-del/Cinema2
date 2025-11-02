/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1.cinema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;
/**
 *
 * @author Carolina
 * @author Kendall
 */


class CustomerModule {
    
    private final Customer[]customers = new Customer[100];
    private int contador =0;
    Scanner sc = new Scanner(System.in);
     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    public static void main(String[] args) throws IOException {
        CustomerModule cm = new CustomerModule();
        cm.showMenu(); /* this is used for show the menu of customers module
        */
    }
    public void showMenu(){
        int option;
        
        do {
            System.out.println("\n=== CUSTOMER MODULE ===");
            System.out.println("1) Register Customer");
            System.out.println("2) Customer List");
            System.out.println("3) Search Customer");
            System.out.println("4) Save List");
            System.out.println("0) Back to the main menu");
            option = sc.nextInt();
            sc.nextLine();
        
        switch (option){
            case 1:
                registerCustomer();
                break;
            case 2:
                customerList();
                break;
            case 3:
                searchCustomer();
                break;
            case 4:
                saveList();
                break;
            case 0:
                    System.out.println("Returning to the main module menu...");
                    break;
            default:
                  System.out.println("An error ocurred...");
        }
            
        } while (option != 0);
    }

    /*
    * this method is used for registered a customer
    */
    private void registerCustomer() {
       if(contador >= customers.length){
           System.out.println("There is not more space for customers");
           return;
       }
       Customer c = new Customer();
       System.out.println("Enter the customers full name ");
       String nombre = sc.nextLine();
       
       System.out.println("Enter the customers id ");
       String id = sc.nextLine();
       
       System.out.println("Does the customer have any disability? yes/ not");
       String answer = sc.nextLine();
       
      // this conditional is used for validity for the user says only yes or no
        while (!answer.equals("yes") && !answer.equals("not")) {
            System.out.println("Invalid answer. Please type 'yes' or 'not':");
            answer = sc.nextLine();
        }
        boolean disability;

        if (answer.equals("yes")) {
            disability = true;
            System.out.println("The customer has a special seat.");
        } else {
            disability = false;
            System.out.println("The customer does not require a special seat.");
        }
        System.out.println("Enter the customer birthday(yyyy/MM/dd)");
        String birthday = sc.nextLine();
        
        System.out.println("Enter the customers phone");
        String phoneNumber = sc.nextLine();
        
        System.out.println("The customers have membership? (yes / not)");
         // this conditional is used for validity for the user says only yes or no
         String answer1 = sc.nextLine();
        while (!answer1.equals("yes") && !answer1.equals("not")) {
            System.out.println("Invalid answer. Please type 'yes' or 'not':");
            answer1 = sc.nextLine();
        }
        boolean vip;

        if (answer1.equals("yes")) {
            vip = true;
            System.out.println("The customer has a benefit.");
        } else {
            vip = false;
            System.out.println("The customer has no benefits..");
        }
         /*
         *Save the dates with the object customer
        */
         c.setName(nombre);
         c.setId(id);
         c.setDisability(disability);
         c.setPhoneNumber(phoneNumber);
         c.setVip(vip);
         
         customers[contador] = c;
         contador++;
         System.out.println("Customer registered succesfully"); 
    }

    private void customerList() {
        if(contador==0){
            System.out.println("There are not registered customers");
            return;// this return to the menu
        }
        System.out.println("\n=== Customer List ===");
        for(int i=0; i<contador; i++){
            System.out.println((i+1)+" Name:"+ customers[i].getName()+ 
                    " Disability:"+ customers[i].isDisability()+" ID:" +
                    customers[i].getId()+ " VIP:"+ customers[i].isVip());
        
        }        
    }
       
    
    private void searchCustomer() {
        
    }

    private void saveList() {
        
    }
}
