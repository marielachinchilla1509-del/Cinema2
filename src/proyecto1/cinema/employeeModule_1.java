/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1.cinema;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
/**
 *
 * @author hp
 */
class employeeModule {
         private final Employee[]employee = new Employee[100];// Static array for storing employees
     private int contador =0;
     Scanner sc=new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
        employeeModule em = new employeeModule();
        em.showMenu(); /* this is used for show the menu of employee module
        */
    }

    public void showMenu() {
        
        int option;
        
        do{

            System.out.println("\n=== EMPLOYEE MODULE ===");
            System.out.println("1) Register Employee");
            System.out.println("2) Employee List");
            System.out.println("3) Search Employee");
            System.out.println("4) Save Employee List");
            System.out.println("5) Delete Employee");
            System.out.println("6) Load Employee List");
            System.out.println("0) Back to the main menu");
            option = sc.nextInt();
            sc.nextLine();
            
            switch(option){
            
                case 1:
                    registerEmployee();
                    break;
                case 2:
                    employeeList();
                    break;
                case 3:
                    searchEmployee();
                    break;
                case 4:
                    saveList();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
                    loadEmploye();
                    break;
                case 0:
                    System.out.println("Returning to the main module menu...");
                    break;
                default :
                    System.out.println("An error ocurred....");
                    
            
            }
                        
        }while(option != 0);
        
        
        
    }

    private void registerEmployee() {
        if(contador>= employee.length){
           System.out.println("There is not more space for Employee");
           return;
        }
        Employee e = new Employee();
      
        System.out.println("Enter the employee full name ");
        String employeed = sc.nextLine();

        System.out.println("Enter the employee id ");
        String id = sc.nextLine();

        System.out.println("Enter the phoneNumber");
        String phone = sc.nextLine();

        System.out.println("Enter employee position: ");
        String position = sc.nextLine();

        System.out.println("Enter employee salary: ");
        double salary = sc.nextDouble();
        sc.nextLine();
        
        System.out.println("Enter the startday");
        String startDay= sc.nextLine();
        
        System.out.println("Enter the employee adress (YYYY/MM/DD):");
        String adress= sc.nextLine();
        
        e.setEmployeeId(employeed);
        e.setId(id);
        e.setPhoneNumber(phone);
        e.setPosition(position);
        e.setSalary(salary);
        e.setAddress(adress);
        // Save employee in array
        employee[contador] = e;
        contador++;

        System.out.println("Employee registered successfully!");  
    }
    /**
     * this method show the list for all the employees
     */
    private void employeeList() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void searchEmployee() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void saveList() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteEmployee() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void loadEmploye() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

    

