/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1.cinema;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Astrid
 * @author Buso
 */
public class ProductModule {

    private final Product[] product = new Product[100];
    private int contador = 0;
    Scanner sc = new Scanner(System.in);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /* this is used for show the menu of product module
     */
    public static void main(String[] args) throws IOException {
        ProductModule pm = new ProductModule();
        pm.showMenu();
    }

    public void showMenu() {
        int option;

        do {

            System.out.println("\n=== PRODUCT MODULE ===");
            System.out.println("1) Register Product");
            System.out.println("2) Edit Product");
            System.out.println("3) Product Inventory");
            System.out.println("4) Check Produck Stock");
            System.out.println("5) Save Product List");
            System.out.println("6) Delete Product");
            System.out.println("0) Back to the main menu");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    registerProduct();
                    break;

                case 2:
                    editProduct();
                    break;

                case 3:
                    productInventory();
                    break;

                case 4:
                    checkProdutcStoc();
                    break;

                case 5:
                    SaveProductList();
                    break;

                case 6:
                    DeleteProduct();
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
    * this method is used for registered a Product
     */
    private void registerProduct() {

        if (contador >= product.length) {
            System.out.println("The stock is full, no more products allowed");
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter product name:");
            String productName = br.readLine();

            System.out.println("Enter product category:");
            String category = br.readLine();

            System.out.println("Enter product description:");
            String description = br.readLine();

            System.out.println("Enter product code:");
            String code = br.readLine();

            System.out.println("Enter product stock:");
            int stock = Integer.parseInt(br.readLine());

            System.out.println("Enter product price:");
            double price = Double.parseDouble(br.readLine());

            System.out.println("Enter product status (Available / Out of stock):");
            String status = br.readLine();

            Product pm = new Product();
            pm.setProductName(productName);
            pm.setCategory(category);
            pm.setDescription(description);
            pm.setCode(code);
            pm.setStock(stock);
            pm.setPrice(price);
            pm.setStatus(status);

            product[contador] = pm;
            contador++;

            System.out.println("Product registered successfully!");

        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Please try again.");
        }
    }

    private void productInventory() {
        System.out.println("\n=== PRODUCT INVENTORY ===");
        if (contador == 0) {
            System.out.println("No products registered yet");
        } else {
            for (int i = 0; i < contador; i++) {
                System.out.println("Product " + (i + 1) + ":");
                System.out.println("Name: " + product[i].getProductName());
                System.out.println("Category: " + product[i].getCategory());
                System.out.println("Description: " + product[i].getDescription());
                System.out.println("Code: " + product[i].getCode());
                System.out.println("Stock: " + product[i].getStock());
                System.out.println("Price: $" + product[i].getPrice());
                System.out.println("Status: " + product[i].getStatus());
            }
        }
    }

    private void checkProdutcStoc() {
        try {
            System.out.println("Enter product code to check stock:");
            String codeToCheck = br.readLine();
            boolean found = false;

            for (int i = 0; i < contador; i++) {
                if (product[i].getCode().equalsIgnoreCase(codeToCheck)) {
                    found = true;
                    System.out.println("Product: " + product[i].getProductName());
                    System.out.println("Stock available: " + product[i].getStock());
                    System.out.println("Status: " + product[i].getStatus());
                    break;
                }
            }

            if (!found) {
                System.out.println("Product not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }

    private void editProduct() {
        try {
            System.out.println("Enter product code to edit:");
            String codeToEdit = br.readLine();

            boolean found = false;
            for (int i = 0; i < contador; i++) {
                if (product[i].getCode().equalsIgnoreCase(codeToEdit)) {
                    found = true;
                    System.out.println("Editing product: " + product[i].getProductName());

                    System.out.println("Enter new name (leave empty to keep current):");
                    String newName = br.readLine();
                    if (!newName.isEmpty()) {
                        product[i].setProductName(newName);
                    }

                    System.out.println("Enter new category (leave empty to keep current):");
                    String newCategory = br.readLine();
                    if (!newCategory.isEmpty()) {
                        product[i].setCategory(newCategory);
                    }

                    System.out.println("Enter new description (leave empty to keep current):");
                    String newDesc = br.readLine();
                    if (!newDesc.isEmpty()) {
                        product[i].setDescription(newDesc);
                    }

                    System.out.println("Enter new price (leave empty to keep current):");
                    String newPrice = br.readLine();
                    if (!newPrice.isEmpty()) {
                        product[i].setPrice(Double.parseDouble(newPrice));
                    }

                    System.out.println("Enter new stock (leave empty to keep current):");
                    String newStock = br.readLine();
                    if (!newStock.isEmpty()) {
                        product[i].setStock(Integer.parseInt(newStock));
                    }

                    System.out.println("Enter new status (leave empty to keep current):");
                    String newStatus = br.readLine();
                    if (!newStatus.isEmpty()) {
                        product[i].setStatus(newStatus);
                    }

                    System.out.println("Product updated successfully!");
                    break;
                }
            }

            if (!found) {
                System.out.println("Product with code '" + codeToEdit + "' not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }

    private void SaveProductList() {
        if (contador == 0) {
            System.out.println("There are no products to save.");
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter("products.txt"); // crea o sobreescribe el archivo
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("=== PRODUCT LIST ===");

            for (int i = 0; i < contador; i++) {
                Product p = product[i];
                printWriter.println((i + 1) + ") Name: " + p.getProductName()
                        + " | Category: " + p.getCategory()
                        + " | Description: " + p.getDescription()
                        + " | Code: " + p.getCode()
                        + " | Stock: " + p.getStock()
                        + " | Price: $" + p.getPrice()
                        + " | Status: " + p.getStatus());
            }

            printWriter.close(); // close file
            System.out.println("The product list was saved correctly in 'products.txt'");

        } catch (IOException e) {
            System.out.println("The An error occurred while saving the product list: " + e.getMessage());
        }
    }

    private void DeleteProduct() {
        try {
            if (contador == 0) {
                System.out.println("There are no products to delete.");
                return;
            }

            System.out.println("Enter the product code to delete:");
            String codeToDelete = br.readLine();

            boolean found = false;

            // Recorremos la lista y movemos los productos hacia arriba para eliminar el encontrado
            for (int i = 0; i < contador; i++) {
                if (product[i].getCode().equalsIgnoreCase(codeToDelete)) {
                    found = true;
                    for (int j = i; j < contador - 1; j++) {
                        product[j] = product[j + 1];
                    }
                    product[contador - 1] = null;
                    contador--;
                    break;
                }
            }

            if (found) {
                // Reescribimos el archivo sin el producto eliminado+
                SaveProductList();
                System.out.println("Product with code '" + codeToDelete + "' was deleted successfully.");
            } else {
                System.out.println("Product not found.");
            }

        } catch (IOException e) {
            System.out.println("An error occurred while deleting the product: " + e.getMessage());
        }
    }

}
