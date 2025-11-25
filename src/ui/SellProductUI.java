package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;
import ui.ProductUI.Product; 
import proyecto1.cinema.Customer; // Importamos la clase Customer

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors; // Necesario para agrupar productos en la factura

public class SellProductUI extends JFrame {

    private final TicketSalesModule module;
    private double total = 0.0;
    private JLabel totalLabel;
    
    private List<Product> productsToSell = new ArrayList<>(); 
    private final List<Product> availableProducts = ProductUI.getProducts(); 

    private final String INVOICE_FOLDER = "invoices";
    
    // ==========================================================
    // CAMPOS DE INSTANCIA PARA LA VALIDACIÓN DE CLIENTE
    // ==========================================================
    private JTextField txtClientId;
    private JTextField txtClientName;
    private Customer foundCustomer = null; 
    private String customerName = "N/A (No Validado)"; 

    public SellProductUI(TicketSalesModule module) {
        this.module = module;

        setTitle("🍿 Sell Product - Cinema POS");
        setSize(700, 600); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);
        Color white = Color.WHITE;
        Color darkGray = new Color(0x2E2E2E);
        
        // ==========================================================
        // PANEL DE VALIDACIÓN DE CLIENTE (BorderLayout.NORTH)
        // ==========================================================
        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        clientPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(navy.darker()), 
                "👤 Customer ID Validation (Mandatory)", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Inter", Font.BOLD, 12), navy));
        clientPanel.setBackground(new Color(245, 245, 255)); 

        txtClientId = new JTextField(10);
        txtClientId.setToolTipText("Enter Customer ID");
        
        txtClientName = new JTextField("N/A", 20);
        txtClientName.setEditable(false);
        txtClientName.setBackground(Color.LIGHT_GRAY);

        JButton btnValidate = new JButton("Validate ID");
        btnValidate.setBackground(navy);
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

        // Título debajo de la validación
        JLabel title = new JLabel("🍫 Product Sales Menu", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(navy);
        title.setForeground(white);
        title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Más pequeño
        clientPanel.add(title); // Añadido al panel de clientes para mantenerlo en NORTH

        // ---------- PANEL DINÁMICO DE PRODUCTOS (LISTA) ----------
        // ... (El resto del layout del centro se mantiene igual que tu versión)
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS)); 
        productPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        productPanel.setBackground(white);

        JLabel productHeader = new JLabel("Available Products:");
        productHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        productPanel.add(productHeader);
        productPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        if (availableProducts.isEmpty()) {
             productPanel.add(new JLabel("No products available. Load products from Product Module."));
        } else {
            JPanel buttonGrid = new JPanel(new GridLayout(0, 3, 15, 15)); 
            buttonGrid.setBackground(white);
            
            for (Product p : availableProducts) {
                if ("Active".equalsIgnoreCase(p.getStatus()) && p.getStock() > 0) { 
                    JButton btn = createProductButton(p);
                    buttonGrid.add(btn);
                }
            }
            JScrollPane scrollPane = new JScrollPane(buttonGrid);
            scrollPane.setBorder(null);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            productPanel.add(scrollPane);
        }
        
        add(new JScrollPane(productPanel), BorderLayout.CENTER); 


        // ---------- PANEL INFERIOR ----------
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10)); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        bottomPanel.setBackground(white);
        
        totalLabel = new JLabel("Total: $0.00", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        totalLabel.setForeground(red); 
        
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        controlPanel.setBackground(white);
        
        JButton btnFinish = new JButton("✅ Finish Purchase");
        btnFinish.setBackground(navy.darker());
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
    // LÓGICA DE BÚSQUEDA DE CLIENTE (Lectura de archivo robusta)
    // ==========================================================
    private Customer searchCustomerFromFile(String id) {
        File file = new File("customers.txt"); 
        
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String name = null;
            String email = null;
            String phone = null;
            boolean isVip = false;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                // 1. Si encontramos la línea de ID que coincide
                if (line.startsWith("ID: ") && line.substring(4).trim().equals(id)) {
                    
                    // ID encontrado. Leemos y asignamos las siguientes líneas esperadas:
                    
                    line = br.readLine();
                    if (line != null && line.trim().startsWith("Name: ")) {
                        name = line.trim().substring(6).trim();
                    } else { continue; } 

                    line = br.readLine();
                    if (line != null && line.trim().startsWith("Email: ")) {
                        email = line.trim().substring(7).trim();
                    } else { continue; }

                    line = br.readLine();
                    if (line != null && line.trim().startsWith("Phone: ")) {
                        phone = line.trim().substring(7).trim();
                    } else { continue; }

                    line = br.readLine();
                    if (line != null && line.trim().startsWith("VIP: ")) {
                        isVip = Boolean.parseBoolean(line.trim().substring(5).trim());
                    } 

                    br.readLine(); // Omitir Membership line
                    br.readLine(); // Omitir Separator line (------------------------------)

                    // 6. ¡Cliente encontrado y datos recolectados!
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
    
    /**
     * Lógica de UI para la validación del cliente.
     */
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

    private JButton createProductButton(Product p) {
        String label = String.format("%s - $%.2f", p.getName(), p.getPrice());
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        Color bg = switch (p.getCategory()) {
            case "Food" -> new Color(0x7C0A02); 
            case "Snack" -> new Color(0xCC7722); 
            case "Candy" -> new Color(0xAA4A44); 
            case "Soda" -> new Color(0x001F3F); 
            default -> Color.DARK_GRAY;
        };
        
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        button.addActionListener((ActionEvent e) -> {
            // Se debe usar una nueva instancia de Product con stock 1 para el tracking
            productsToSell.add(new Product(
                    p.getName(), p.getCategory(), 
                    p.getDesc(), p.getCode(), 1, 
                    p.getPrice(), p.getStatus()));
            
            // Aquí se debería actualizar el stock real en el ProductUI (no está en este archivo)
            // Ya que ProductUI.getProducts() devuelve la referencia.
            // Para simplicidad, solo actualizamos el total.
            total += p.getPrice(); 
            updateTotalLabel();
            JOptionPane.showMessageDialog(this,
                    p.getName() + " added! Total items: " + productsToSell.size());
        });

        return button;
    }

    private void updateTotalLabel() {
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void finishPurchase(ActionEvent e) {
        if (productsToSell.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No products selected.",
                    "Empty Purchase",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // --- VALIDACIÓN DE CLIENTE (MANDATORIA) ---
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "❌ Customer ID is mandatory. Please validate a registered customer before completing the purchase.",
                    "Customer Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // ------------------------------------------
        
        Ticket t = new Ticket();
        t.setTicketId("P" + System.currentTimeMillis());
        t.setMovieTitle("Snack Purchase for " + customerName); // Incluye nombre del cliente
        t.setPrice(total);
        t.setDate(new Date());
        t.setStatus("Paid");
        t.setPaymentMethod("Cash");
        t.setType("Product");

        module.addTicket(t);
        
        showInvoice(t); 
        saveInvoiceToFile(t, productsToSell); 

        dispose();
    }
    
    // El método showInvoice se modifica para incluir el agrupamiento de productos
    private void showInvoice(Ticket t) {
        JFrame invoiceFrame = new JFrame("🧾 Product Invoice");
        invoiceFrame.setSize(400, 520);
        invoiceFrame.setLocationRelativeTo(null);
        invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("🍿 CINEMA INVOICE", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(0x001F3F));
        panel.add(header, BorderLayout.NORTH);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        double subtotal = total / (1 + 0.13); // Recalcular subtotal sin redondeos previos
        double tax = total - subtotal;
        
        // Generar la lista de productos agrupados
        StringBuilder productsList = new StringBuilder();
        productsToSell.stream()
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()))
                .forEach((name, count) -> {
                    double price = availableProducts.stream()
                            .filter(p -> p.getName().equals(name))
                            .findFirst().map(Product::getPrice).orElse(0.0);
                    productsList.append(String.format("  - %d x %-20s @ $%.2f\n", count, name, price));
                });
        

        String invoiceText =
                "----------------------------------------\n" +
                "     🍫 CINEMA INVOICE\n" +
                "----------------------------------------\n" +
                "Client: " + customerName + "\n" + // Nombre del cliente
                "Invoice ID: " + t.getTicketId() + "\n" +
                String.format("Date: %s\n", sdf.format(t.getDate())) +
                "Payment: Cash\n" +
                "----------------------------------------\n" +
                "Products Sold:\n" +
                productsList.toString() + // Lista agrupada
                "----------------------------------------\n" +
                String.format("SUBTOTAL: $%.2f\n", subtotal) +
                String.format("TAX (13%%): $%.2f\n", tax) +
                "----------------------------------------\n" +
                String.format("TOTAL: $%.2f\n", total) +
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
        btnClose.setBackground(new Color(0xB22222));
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

    private void saveInvoiceToFile(Ticket t, List<Product> soldProducts) {
        
        double subtotal = total / (1 + 0.13); 
        double tax = total - subtotal;
        
        try {
            File folder = new File(INVOICE_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "invoice_product_" + sdf.format(t.getDate()) + ".txt";

            File file = new File(folder, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("----------------------------------------\n");
                writer.write("     🍫 CINEMA INVOICE\n");
                writer.write("----------------------------------------\n");
                writer.write("Client: " + customerName + "\n");
                writer.write("Invoice ID: " + t.getTicketId() + "\n");
                writer.write("Purchase: Snack Products\n");
                writer.write("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getDate()) + "\n");
                writer.write("Payment: Cash\n");
                writer.write("----------------------------------------\n");
                
                // Escribir la lista de productos vendidos agrupados
                writer.write("Products Sold:\n");
                soldProducts.stream()
                    .collect(Collectors.groupingBy(Product::getName, Collectors.counting()))
                    .forEach((name, count) -> {
                        double price = availableProducts.stream()
                            .filter(p -> p.getName().equals(name))
                            .findFirst().map(Product::getPrice).orElse(0.0);
                        try {
                            writer.write(String.format("  - %d x %-20s @ $%.2f\n", count, name, price));
                        } catch (IOException ex) {
                            // ignore
                        }
                    });
                writer.write("----------------------------------------\n");

                writer.write(String.format("SUBTOTAL: $%.2f\n", subtotal));
                writer.write(String.format("TAX (13%%): $%.2f\n", tax));
                writer.write("----------------------------------------\n");
                writer.write(String.format("TOTAL: $%.2f\n", total));
                writer.write("\n----------------------------------------\n");
                writer.write("Thank you for your purchase! 🍿");
            }

            JOptionPane.showMessageDialog(this,
                    "✅ Invoice saved successfully!\nFile: " + file.getAbsolutePath(),
                    "Invoice Saved",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error saving invoice file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        ProductUI.main(new String[0]); 
        
        SwingUtilities.invokeLater(() ->
                new SellProductUI(new TicketSalesModule()).setVisible(true));
    }
}