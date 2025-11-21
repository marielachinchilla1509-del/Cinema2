package ui;

import proyecto1.cinema.Product;
import java.util.*;
import java.io.*;

public class ProductStorage {

    private static final List<Product> products = new ArrayList<>();

    // ───────────────────────────────
    // AGREGAR PRODUCTO
    // ───────────────────────────────
    public static void addProduct(Product p) {
        products.add(p);
    }

    // ───────────────────────────────
    // OBTENER LISTA
    // ───────────────────────────────
    public static List<Product> getProducts() {
        return products;
    }

    // ───────────────────────────────
    // GUARDAR EN TXT
    // ───────────────────────────────
    public static void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                pw.println(
                        p.getProductName() + ";"
                        + p.getCategory() + ";"
                        + p.getDescription() + ";"
                        + p.getCode() + ";"
                        + p.getStock() + ";"
                        + p.getPrice() + ";"
                        + p.getStatus()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ───────────────────────────────
    // CARGAR DESDE TXT
    // ───────────────────────────────
    public static void loadFromFile(String filename) {
        products.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(";");

                if (d.length == 7) {
                    Product p = new Product(
                            Double.parseDouble(d[5]),
                            Integer.parseInt(d[4]),
                            d[1], // category
                            d[3], // code
                            d[2], // description
                            d[0], // productName
                            d[6] // status
                    );
                    products.add(p);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
