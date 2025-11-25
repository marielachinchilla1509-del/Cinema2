package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;
import ui.ProductUI.Product;
import proyecto1.cinema.Customer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import java.util.Map;
import java.util.Hashtable;
import java.util.TreeMap; 

public class SellProductUI extends JFrame {

    // Colores para la interfaz
    private final Color red = new Color(139, 0, 0);
    private final Color navyBlue = new Color(10, 25, 60);
    private final Color white = Color.WHITE;
    private final Color darkGray = new Color(0x2E2E2E);

    private final TicketSalesModule module;
    private double total = 0.0; 
    private JLabel totalLabel;
    
    // Contenedores
    private Vector<Product> productsToSell = new Vector<>();
    private final Vector<Product> availableProducts = ProductUI.getProducts();
    private final String INVOICE_FOLDER = "invoices";
    
    // Componentes de UI
    private JTextField txtClientId;
    private JTextField txtClientName;
    private JTextField txtSearch;
    private JPanel productsContainerPanel; 
    private JScrollPane productsScrollPane; 
    
    private Customer foundCustomer = null;
    private String customerName = "N/A (No Validado)";

    public SellProductUI(TicketSalesModule module) {
        this.module = module;

        setTitle("🍿 Sell Product - CINEMA UCR");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        
        // ==========================================================
        // PANEL NORTE (VALIDACIÓN DE CLIENTE)
        // ==========================================================
        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        clientPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(navyBlue.darker()), 
                "👤 Customer ID Validation (Mandatory)", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Inter", Font.BOLD, 12), navyBlue));
        clientPanel.setBackground(new Color(245, 245, 255)); 

        txtClientId = new JTextField(10);
        txtClientId.setToolTipText("Enter Customer ID");
        
        txtClientName = new JTextField("N/A", 20);
        txtClientName.setEditable(false);
        txtClientName.setBackground(Color.LIGHT_GRAY);

        JButton btnValidate = new JButton("Validate ID");
        btnValidate.setBackground(navyBlue);
        btnValidate.setForeground(white);
        btnValidate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        clientPanel.add(new JLabel("ID:"));
        clientPanel.add(txtClientId);
        clientPanel.add(btnValidate);
        clientPanel.add(new JLabel("Client:"));
        clientPanel.add(txtClientName);
        
        btnValidate.addActionListener(e -> validateClient());
        txtClientId.addActionListener(e -> validateClient()); 
        
        add(clientPanel, BorderLayout.NORTH); 

        // ==========================================================
        // PANEL CENTRAL (BÚSQUEDA Y PRODUCTOS AGRUPADOS)
        // ==========================================================
        productsContainerPanel = new JPanel(new BorderLayout(10, 10));
        productsContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        productsContainerPanel.setBackground(white);
        
        // ---------- BARRA DE BÚSQUEDA ----------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBackground(white);
        JLabel searchLabel = new JLabel("🔍 Search Product:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtSearch = new JTextField(30);
        txtSearch.setToolTipText("Enter product name or code to filter");
        
        searchPanel.add(searchLabel);
        searchPanel.add(txtSearch);
        
        productsContainerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // ---------- CONFIGURAR LISTENER DE BÚSQUEDA ----------
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateProductDisplay(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateProductDisplay(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateProductDisplay(); }
        });
        
        // Inicializar ScrollPane
        productsScrollPane = new JScrollPane();
        productsScrollPane.setBorder(null);
        productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        productsContainerPanel.add(productsScrollPane, BorderLayout.CENTER);
        
        // Inicializar y mostrar la lista de productos
        updateProductDisplay();

        add(productsContainerPanel, BorderLayout.CENTER); 


        // ==========================================================
        // PANEL INFERIOR (TOTAL Y CONTROLES)
        // ==========================================================
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10)); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        bottomPanel.setBackground(white);
        
        totalLabel = new JLabel("Total (with Tax): $0.00", SwingConstants.CENTER); 
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        totalLabel.setForeground(red); 
        
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        controlPanel.setBackground(white);
        
        JButton btnFinish = new JButton("✅ Finish Purchase");
        btnFinish.setBackground(navyBlue.darker());
        btnFinish.setForeground(white);
        btnFinish.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFinish.setFocusPainted(false);
        btnFinish.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFinish.addActionListener(this::finishPurchase);
        
        JButton btnCancel = new JButton("❌ Cancel");
        btnCancel.setBackground(darkGray);
        btnCancel.setForeground(white);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());
        
        controlPanel.add(btnFinish);
        controlPanel.add(btnCancel);
        
        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    // ==========================================================
    // MÉTODO DINÁMICO DE ACTUALIZACIÓN DE PRODUCTOS POR CATEGORÍA
    // ==========================================================
    private void updateProductDisplay() {
        
        String searchText = txtSearch.getText().trim().toLowerCase();
        
        // Agrupar los productos activos y filtrados por su categoría
        Map<String, Vector<Product>> categorizedProducts = new TreeMap<>();
        
        for (Product p : availableProducts) {
            if ("Active".equalsIgnoreCase(p.getStatus()) && p.getStock() > 0) {
                
                // Lógica de filtrado
                boolean matchesSearch = searchText.isEmpty() || 
                                        p.getName().toLowerCase().contains(searchText) ||
                                        p.getCode().toLowerCase().contains(searchText);
                
                if (matchesSearch) {
                    String category = p.getCategory();
                    categorizedProducts.computeIfAbsent(category, k -> new Vector<>()).add(p);
                }
            }
        }
        
        // Crear el panel principal que contendrá los paneles de categorías
        JPanel mainDisplayPanel = new JPanel();
        mainDisplayPanel.setLayout(new BoxLayout(mainDisplayPanel, BoxLayout.Y_AXIS));
        mainDisplayPanel.setBackground(white);
        
        boolean foundAnyProduct = false;

        // Iterar sobre cada categoría y crear su subpanel
        for (Map.Entry<String, Vector<Product>> entry : categorizedProducts.entrySet()) {
            String category = entry.getKey();
            Vector<Product> products = entry.getValue();
            
            if (!products.isEmpty()) {
                
                // 1. Agregar Separador y Título de Categoría
                JLabel categoryHeader = new JLabel("--- " + category.toUpperCase() + " ---");
                categoryHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
                categoryHeader.setForeground(navyBlue);
                categoryHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
                
                JPanel headerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
                headerWrapper.setBackground(white);
                headerWrapper.add(categoryHeader);
                mainDisplayPanel.add(headerWrapper);
                
                // 2. Crear Grid de Botones para la Categoría (6 columnas)
                JPanel buttonGrid = new JPanel(new GridLayout(0, 6, 8, 8)); 
                buttonGrid.setBackground(white);
                buttonGrid.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

                for (Product p : products) {
                    JButton btn = createProductButton(p);
                    buttonGrid.add(btn);
                    foundAnyProduct = true;
                }
                mainDisplayPanel.add(buttonGrid);
            }
        }
        
        // Si no se encontró ningún producto (después de filtrar o por stock)
        if (!foundAnyProduct) {
             JLabel noProductsLabel = new JLabel("No active products match the search criteria or are in stock.", SwingConstants.CENTER);
             noProductsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
             mainDisplayPanel.add(noProductsLabel);
        }

        // Actualizar el contenido del ScrollPane
        productsScrollPane.setViewportView(mainDisplayPanel);
        productsContainerPanel.revalidate();
        productsContainerPanel.repaint();
    }
    
    // ==========================================================
    // CREACIÓN DEL BOTÓN (MODIFICADO PARA STOCK DISPONIBLE)
    // ==========================================================
    private JButton createProductButton(Product p) {
        
        // --- MODIFICACIÓN CLAVE: CALCULAR STOCK DISPONIBLE ---
        long currentItemsInCart = 0;
        for (Product item : productsToSell) {
            if (item.getCode().equals(p.getCode())) {
                currentItemsInCart++;
            }
        }
        int availableStock = p.getStock() - (int)currentItemsInCart;
        // ----------------------------------------------------
        
        // Determinar el emoji basado en la categoría para detalle visual
        String emoji = switch (p.getCategory()) {
            case "Food" -> "🍔"; 
            case "Snack" -> "🍿"; 
            case "Candy" -> "🍬"; 
            case "Soda" -> "🥤"; 
            default -> "📦";
        };
            
        // Formato ultra limpio: [Emoji] Nombre | $Precio (Stock: X)
        String label = String.format("%s %s | $%.2f (Stock: %d)", 
                                     emoji, p.getName(), p.getPrice(), availableStock); // Usa STOCK DISPONIBLE
        
        JButton button = new JButton(label);
        
        // --- ESTILO ULTRA COMPACTO Y FUENTE GRANDE ---
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(navyBlue);
        button.setForeground(white);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        // ---------------------------------------------
        
        // Deshabilitar si no hay stock disponible
        if (availableStock <= 0) {
            button.setEnabled(false);
            button.setBackground(Color.GRAY.darker());
        }
        
        button.addActionListener((ActionEvent e) -> {
            
            Product actualProduct = availableProducts.stream()
                .filter(ap -> ap.getCode().equals(p.getCode()))
                .findFirst().orElse(null);
            
            if (actualProduct == null) {
                 JOptionPane.showMessageDialog(this,
                    "Error: Product not found in inventory.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Recalcular el conteo en carrito antes de añadir
            long currentItemsInCartAfterClick = 0;
            for (Product item : productsToSell) {
                if (item.getCode().equals(p.getCode())) {
                    currentItemsInCartAfterClick++;
                }
            }
                
            // Límite de Stock
            if (currentItemsInCartAfterClick >= actualProduct.getStock()) {
                 JOptionPane.showMessageDialog(this,
                    "❌ Máximo de stock alcanzado para " + actualProduct.getName() + ".\nDisponible: 0", 
                    "Límite de Stock", JOptionPane.ERROR_MESSAGE);
                return; 
            }
            
            // Añadir el producto al Vector
            productsToSell.add(new Product(
                    p.getName(), p.getCategory(), 
                    p.getDesc(), p.getCode(), 1, 
                    p.getPrice(), p.getStatus()));
            
            total += p.getPrice(); // Acumula el subtotal
            updateTotalLabel();
            
            long newItemsInCart = currentItemsInCartAfterClick + 1;
            
            JOptionPane.showMessageDialog(this,
                    p.getName() + " añadido! Cantidad total en carrito: " + newItemsInCart);
            
            // ¡MODIFICACIÓN CLAVE! Recarga la vista para actualizar el stock en el botón
            updateProductDisplay(); 
        });

        return button;
    }


    // ==========================================================
    // MÉTODOS DE LÓGICA (VALIDACIÓN, TOTAL, FACTURACIÓN)
    // ==========================================================

    private Customer searchCustomerFromFile(String id) {
        File file = new File("customers.txt"); 
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String name = null;
            String email = null;
            String phone = null;
            boolean isVip = false;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("ID: ") && line.substring(4).trim().equals(id)) {
                    line = br.readLine();
                    if (line != null && line.trim().startsWith("Name: ")) name = line.trim().substring(6).trim(); else { continue; } 
                    line = br.readLine();
                    if (line != null && line.trim().startsWith("Email: ")) email = line.trim().substring(7).trim(); else { continue; }
                    line = br.readLine();
                    if (line != null && line.trim().startsWith("Phone: ")) phone = line.trim().substring(7).trim(); else { continue; }
                    line = br.readLine();
                    if (line != null && line.trim().startsWith("VIP: ")) isVip = Boolean.parseBoolean(line.trim().substring(5).trim()); 
                    br.readLine(); 
                    br.readLine();
                    return new Customer(id, name, email, phone, isVip);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customer data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during customer file parsing: " + e.getMessage());
        }
        return null;
    }
    
    private void validateClient() {
        String clientId = txtClientId.getText().trim();
        foundCustomer = null; 
        
        if (clientId.isEmpty()) {
            txtClientName.setText("N/A (Enter ID)");
            JOptionPane.showMessageDialog(this, "Please enter a Customer ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = searchCustomerFromFile(clientId);

        if (customer != null) {
            foundCustomer = customer;
            customerName = foundCustomer.getName(); 
            txtClientName.setText(customerName);
            JOptionPane.showMessageDialog(this, "✅ Customer Found: " + customerName, "Valid", JOptionPane.INFORMATION_MESSAGE);
        } else {
            foundCustomer = null;
            customerName = "N/A (No Validado)";
            txtClientName.setText(customerName);
            JOptionPane.showMessageDialog(this, 
                    "❌ Customer ID Not Found. Sales are only allowed for registered customers.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateTotalLabel() {
        double subtotal = total;
        double tax = subtotal * 0.13;
        double totalFinal = subtotal + tax;

        totalLabel.setText(String.format("Total (with Tax): $%.2f", totalFinal));
    }

    private void finishPurchase(ActionEvent e) {
        if (productsToSell.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No products selected.", "Empty Purchase", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "❌ Customer ID is mandatory. Please validate a registered customer.",
                    "Customer Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double subtotal = total;
        double tax = subtotal * 0.13;
        double totalFinal = subtotal + tax;
        
        updateProductStock(); 

        Ticket t = new Ticket();
        t.setTicketId("P" + System.currentTimeMillis());
        t.setMovieTitle("Snack Purchase for " + customerName);
        t.setPrice(totalFinal); 
        t.setDate(new Date());
        t.setStatus("Paid");
        t.setPaymentMethod("Cash");
        t.setType("Product");

        module.addTicket(t);
        
        saveInvoiceToFile(t, productsToSell, subtotal, tax, totalFinal);
        showInvoice(t, subtotal, tax, totalFinal);
        
        dispose();
    }
    
    // Función para contar productos vendidos SIN usar Collectors 
    private Map<String, Long> countSoldProducts() {
        Map<String, Long> soldCounts = new Hashtable<>();
        for (Product p : productsToSell) {
            String code = p.getCode();
            soldCounts.put(code, soldCounts.getOrDefault(code, 0L) + 1);
        }
        return soldCounts;
    }

    private void updateProductStock() {
        Map<String, Long> soldCounts = countSoldProducts(); 
        
        Vector<Product> currentProducts = ProductUI.readAllProductsFromFile();

        for (Product currentP : currentProducts) {
            String code = currentP.getCode();
            if (soldCounts.containsKey(code)) {
                int soldQty = soldCounts.get(code).intValue();
                int newStock = currentP.getStock() - soldQty;
                
                currentP.setStock(newStock > 0 ? newStock : 0);
            }
        }

        ProductUI.writeAllProductsToFile(currentProducts);
    }
    
    /**
     * Muestra la factura con el cálculo aditivo de impuestos.
     */
    private void showInvoice(Ticket t, double subtotal, double tax, double totalFinal) {
        JFrame invoiceFrame = new JFrame("🧾 Product Invoice");
        invoiceFrame.setSize(400, 520);
        invoiceFrame.setLocationRelativeTo(null);
        invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("🍿 CINEMA INVOICE", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(navyBlue);
        panel.add(header, BorderLayout.NORTH);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        Map<String, Long> groupedProducts = new Hashtable<>();
        for (Product p : productsToSell) {
            groupedProducts.put(p.getName(), groupedProducts.getOrDefault(p.getName(), 0L) + 1);
        }

        StringBuilder productsList = new StringBuilder();
        for (Map.Entry<String, Long> entry : groupedProducts.entrySet()) {
            String name = entry.getKey();
            Long count = entry.getValue();
            
            double price = 0.0;
            for (Product ap : availableProducts) {
                if (ap.getName().equals(name)) {
                    price = ap.getPrice();
                    break;
                }
            }
            productsList.append(String.format("  - %d x %-20s @ $%.2f\n", count, name, price));
        }

        String invoiceText =
                "----------------------------------------\n" +
                "      🍫 CINEMA INVOICE\n" +
                "----------------------------------------\n" +
                "Client: " + customerName + "\n" +
                "Invoice ID: " + t.getTicketId() + "\n" +
                String.format("Date: %s\n", sdf.format(t.getDate())) +
                "Payment: Cash\n" +
                "----------------------------------------\n" +
                "Products Sold:\n" +
                productsList.toString() +
                "----------------------------------------\n" +
                String.format("SUBTOTAL: $%.2f\n", subtotal) +
                String.format("TAX (13%%): $%.2f\n", tax) +
                "----------------------------------------\n" +
                String.format("TOTAL: $%.2f\n", totalFinal) +
                "----------------------------------------\n" +
                "Thank you for your purchase! 🍿";

        JTextArea textArea = new JTextArea(invoiceText);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(red);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> invoiceFrame.dispose());

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnClose);
        panel.add(bottom, BorderLayout.SOUTH);

        invoiceFrame.add(panel);
        invoiceFrame.setVisible(true);
    }

    /**
     * Guarda la factura con el cálculo aditivo de impuestos.
     */
    private void saveInvoiceToFile(Ticket t, Vector<Product> soldProducts, double subtotal, double tax, double totalFinal) {
        
        try {
            File folder = new File(INVOICE_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "invoice_product_" + sdf.format(t.getDate()) + ".txt";

            File file = new File(folder, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("----------------------------------------\n");
                writer.write("      🍫 CINEMA INVOICE\n");
                writer.write("----------------------------------------\n");
                writer.write("Client: " + customerName + "\n");
                writer.write("Invoice ID: " + t.getTicketId() + "\n");
                writer.write("Purchase: Snack Products\n");
                writer.write("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getDate()) + "\n");
                writer.write("Payment: Cash\n");
                writer.write("----------------------------------------\n");
                
                Map<String, Long> groupedProducts = new Hashtable<>();
                for (Product p : soldProducts) {
                    groupedProducts.put(p.getName(), groupedProducts.getOrDefault(p.getName(), 0L) + 1);
                }

                writer.write("Products Sold:\n");
                for (Map.Entry<String, Long> entry : groupedProducts.entrySet()) {
                    String name = entry.getKey();
                    Long count = entry.getValue();

                    double price = 0.0;
                    for (Product ap : availableProducts) {
                        if (ap.getName().equals(name)) {
                            price = ap.getPrice();
                            break;
                        }
                    }
                    writer.write(String.format("  - %d x %-20s @ $%.2f\n", count, name, price));
                }
                
                writer.write("----------------------------------------\n");

                writer.write(String.format("SUBTOTAL: $%.2f\n", subtotal));
                writer.write(String.format("TAX (13%%): $%.2f\n", tax));
                writer.write("----------------------------------------\n");
                writer.write(String.format("TOTAL: $%.2f\n", totalFinal));
                writer.write("\n----------------------------------------\n");
                writer.write("Thank you for your purchase! 🍿");
            }

      

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error saving invoice file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Asumiendo que las otras clases necesarias (Customer, Ticket, TicketSalesModule) existen
    }
}