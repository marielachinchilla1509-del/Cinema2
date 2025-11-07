/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1.cinema;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
/**
 *
 * @author Carolina
 * @author kendall
 */
public class EmployeeModule {
         private final Employee[]employee = new Employee[100];// Static array for storing employees
     private int contador =0;
     Scanner sc=new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
        EmployeeModule em = new EmployeeModule();
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
/*
 *this method is used for register employees and saving in array.
 */
    private void registerEmployee() {
        if(contador>= employee.length){
           System.out.println("There is not more space for Employee");
           return;
        }
        Employee e = new Employee();
      
        System.out.println("Enter the employee full name ");
        String name = sc.nextLine();

        System.out.println("Enter the employee id ");
        String id = sc.nextLine();

        System.out.println("Enter the phoneNumber");
        String phone = sc.nextLine();

        System.out.println("Enter employee position: ");
        String position = sc.nextLine();

        System.out.println("Enter employee salary: ");
        double salary = sc.nextDouble();
        sc.nextLine();
        
        System.out.println("Enter the startday (YYYY/MM/DD):");
        String startDay= sc.nextLine();
        
        System.out.println("Enter the employee address");
        String address= sc.nextLine();
        
        /*
        *We use the atributes of the employee class.
        */
        e.setName(name);
        e.setId(id);
        e.setPhoneNumber(phone);
        e.setPosition(position);
        e.setSalary(salary);
        e.setAddress(address);
        
        // Save employee in array
        employee[contador] = e;
        contador++;

        System.out.println("Employee registered successfully!");  
    }
    /**
     * this method show the list for all the employees
     */
    private void employeeList() {
        if (contador == 0) {
            System.out.println("There are not registered employee");
            return;
        }
        System.out.println("\n=== Employee List ===");
        for (int i = 0; i < contador; i++) {
            System.out.println((i + 1) + " Name: " + employee[i].getName()
                    + " EmployeeID : " + employee[i].getId()+ " Position: "
                    + employee[i].getPosition() + " Salary: " 
                    + employee[i].getSalary());
        }
    }

    private void searchEmployee() {
        if (contador == 0) {
            System.out.println("There are not registered employee");
            System.out.println("---Please register---");
            return;
        }
        System.out.println("Enter the employee ID");
        String id = sc.nextLine();

        /*
        *the arrangement is covered to verify that the employee is correctly registered.
        */
        boolean found = false;

        for (int i = 0; i < contador; i++) {
            if(employee[i].getId().equals(id)){
                    System.out.println("=== Employee found ===");
                    System.out.println("Name: "+ employee[i].getName());
                    System.out.println("EmployeeID : " + employee[i].getId());
                    System.out.println("Position: " + employee[i].getPosition());
                    System.out.println(" Salary: " + employee[i].getSalary());
                    found=true;
                }
            if (!found) {
                System.out.println("Employee not found");

            }
        }
    }

    private void saveList() {
        if (contador == 0) {
            System.out.println("There are not employee to save");
            return;
        }
        
        /*
        *we add fileWriter so taht the written information is saved as document .txt
        */
        try {
            FileWriter fileWriter = new FileWriter("employee.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Employee List");
            for (int i = 0; i < contador; i++) {
                Employee em = employee[i];
                
                /*
                *(i+1) is to list the employees
                */
                printWriter.println((i + 1) + " Name: " + em.getName() + " ID: "
                        + " Employee ID: " + em.getId() + " Position: " 
                        + em.getPosition() + " Salary: " + em.getSalary());
            }
            printWriter.close();
            System.out.println("The employee list was saved correctly in employee.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the list "
                    + e.getMessage());
        }
    }

    private void deleteEmployee() {
        /*
        *Go through the employee list and delete.
        */
       if(contador ==0){
           System.out.println("There are not registered employees to delete");
           return;
       }
        System.out.println("Enter the employee ID to delete");
        String id= sc.nextLine();
        
        boolean found= false;
        
        /*
        *Tour to verify if the employee exists.
         */
        for (int i = 0; i < contador; i++) {
            if (employee[i].getId().equals(id)) {
                found = true;

                System.out.println("Employee found: " + employee[i].getName()
                        + " - " + employee[i].getPosition());
                System.out.println("Are you sure you want to delete this employee? (yes/not)");
                String confirm = sc.nextLine();

                for (int j = i; j < contador - 1; j++) {
                    employee[j] = employee[j + 1];
                }
                employee[contador-1] = null;
                contador--;

                System.out.println("Employee deleted successfully!");
                /*
                * it is used to update the list
                */
                saveList();
                return;
            }
        }
    }

    private void loadEmploye() {
        try {
            // Create a File object representing the customers.txt file
            java.io.File file = new java.io.File("employee.txt");

            // this is used for validate the file exists
            if (!file.exists()) {
                System.out.println("No saved employee file found.");
                return;
            }
            /*
          *Create a BufferedReader to read the text file line by line
             */
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line;

            System.out.println("\\n=== LOADED EMPLOYEE LIST ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
            System.out.println("\nFile loaded successfully!");

        } catch (IOException e) {
            // Show error message in case the file cannot be read
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
