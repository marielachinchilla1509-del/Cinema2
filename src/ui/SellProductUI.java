package ui;

import proyecto1.cinema.TicketSalesModule;
import ui.ProductUI.Product;
import proyecto1.cinema.Customer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import java.util.Map;
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

                // 2. Crear el Panel de Productos para la categoría (grid dinámico)
                int cols = 3;
                JPanel categoryPanel = new JPanel(new GridLayout(0, cols, 20, 20));
                categoryPanel.setBackground(white);
                categoryPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

                for (Product p : products) {
                    categoryPanel.add(createProductCard(p));
                    foundAnyProduct = true;
                }
                mainDisplayPanel.add(categoryPanel);
            }
        }
        
        if (!foundAnyProduct) {
             JLabel noProductsLabel = new JLabel("No active products found or matching search.", SwingConstants.CENTER);
             noProductsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
             noProductsLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
             mainDisplayPanel.add(noProductsLabel);
        }

        // Actualizar el JScrollPane con el nuevo contenido
        productsScrollPane.setViewportView(mainDisplayPanel);
        productsScrollPane.revalidate();
        productsScrollPane.repaint();
    }
    
    // Método simulado para crear una tarjeta de producto (card)
    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(new Color(250, 250, 250));

        JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(nameLabel, BorderLayout.NORTH);

        JLabel priceLabel = new JLabel(String.format("Price: $%.2f", p.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(priceLabel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add to Cart");
        addButton.setBackground(navyBlue);
        addButton.setForeground(white);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addProductToSell(p));
        card.add(addButton, BorderLayout.SOUTH);

        return card;
    }

    private void addProductToSell(Product p) {
        if (p.getStock() > 0) {
            productsToSell.add(p);
            p.setStock(p.getStock() - 1); // Reducir stock simuladamente
            this.total += p.getPrice();
            updateTotalLabel();
            updateProductDisplay(); // Refrescar para reflejar el stock
            JOptionPane.showMessageDialog(this, 
                    "Added: " + p.getName() + " to cart.", 
                    "Product Added", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Stock is 0 for: " + p.getName());
        }
    }


    // ==========================================================
    // MÉTODOS DE LÓGICA DE CLIENTE Y CÁLCULO
    // ==========================================================

    private Customer searchCustomerFromFile(String id) {
        File file = new File("customers.txt");
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String name = null, email = null, phone = null;
            boolean vip = false;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID: ") && line.substring(4).trim().equals(id)) {
                    // Lee las siguientes líneas para obtener los detalles
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("Name: ")) {
                        name = line.substring(6).trim();
                    }
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("Email: ")) {
                        email = line.substring(7).trim();
                    }
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("Phone: ")) {
                        phone
                                = line.substring(7).trim();
                    }
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("VIP: ")) {
                        vip
                                = Boolean.parseBoolean(line.substring(5).trim());
                    }

                    // Saltar líneas en blanco si las hay entre clientes
                    br.readLine();
                    br.readLine();

                    // Asumiendo que Customer tiene un constructor (String id, String name, String email, String phone, boolean vip)
                    return new Customer(id, name, email, phone, vip);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private void validateClient() {
        String id = txtClientId.getText().trim();

        if (id.isEmpty()) {
            txtClientName.setText("N/A");
            JOptionPane.showMessageDialog(this, "Enter a valid ID.");
            return;
        }

        Customer c = searchCustomerFromFile(id);

        if (c == null) {
            foundCustomer = null;
            customerName = "N/A (No Validado)";
            txtClientName.setText("Not Found");
            JOptionPane.showMessageDialog(this, "❌ Customer not found.");
        } else {
            foundCustomer = c;
            customerName = c.getName();
            txtClientName.setText(customerName);
            String level = getMembershipLevel();
            double discount = getDiscountPercentage() * 100;
            
            JOptionPane.showMessageDialog(this,
                    String.format("✅ Customer Found: %s\n🏷 Membership: %s (%.0f%% Discount)",
                        customerName, level, discount));
            updateTotalLabel();
        }
    }
    
    private double getDiscountPercentage() {
        if (foundCustomer == null) {
            return 0.0;
        }

        // Mapeo de descuentos: VIP (true) -> Premium 15%, No VIP (false) -> Basic 5%
        if (foundCustomer.isVip()) {
            return 0.15; // 15% Premium
        } else {
            return 0.05; // 5% Basic
        }
    }

    private double getDiscountAmount(double subtotal) {
        return subtotal * getDiscountPercentage();
    }

    private String getMembershipLevel() {
        if (foundCustomer == null) {
            return "N/A";
        }

        // Mapeo simple: VIP -> Premium, No VIP -> Basic
        return foundCustomer.isVip() ? "Premium" : "Basic";
    }

    private void updateTotalLabel() {
        // --- Cálculo de descuentos y Total Final ---
        double subtotalBase = this.total; 
        
        // Descuento
        double discountAmount = getDiscountAmount(subtotalBase);
        double subtotalAfterDiscount = subtotalBase - discountAmount;

        // Impuesto (13%)
        double tax = subtotalAfterDiscount * 0.13;
        double totalFinal = subtotalAfterDiscount + tax;

        totalLabel.setText(String.format("Total (with Tax): $%.2f", totalFinal));
    }


    private void finishPurchase(ActionEvent e) {
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this, 
                    "❌ You must validate a customer before selling products.", 
                    "Customer Required", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (productsToSell.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "❌ Add at least one product to the purchase.", 
                    "No Products", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- CÁLCULO FINAL DE LA FACTURA ---
        double subtotalBase = this.total; 
        // Lógica de descuento
        double discountRate = getDiscountPercentage();
        double discountAmount = subtotalBase * discountRate;
        double subtotalAfterDiscount = subtotalBase - discountAmount;
        // Lógica de Impuestos y Total
        double tax = subtotalAfterDiscount * 0.13;
        double totalFinal = subtotalAfterDiscount + tax;

        // Aquí iría la lógica para registrar la venta de productos... 
        // module.recordProductSale(foundCustomer, productsToSell, totalFinal, "Cash"); // (Ejemplo)

        showProductInvoice(subtotalBase, discountAmount, tax, totalFinal); // Llamar a la factura

        // Restablecer la venta y cerrar
        productsToSell.clear();
        this.total = 0.0;
        updateTotalLabel();
        dispose();
    }
    
    // =========================================================
    // FACTURA DE PRODUCTOS
    // =========================================================
    private void showProductInvoice(double subtotalBase, double discountAmount, double tax,
            double totalFinal) {
        JFrame invoiceFrame = new JFrame("🧾 Product Invoice");
        invoiceFrame.setSize(400, 550);
        invoiceFrame.setLocationRelativeTo(null);
        invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String membership = getMembershipLevel();
        double discountRate = getDiscountPercentage() * 100;

        // Construir la lista de productos
        StringBuilder productsDetails = new StringBuilder();
        // Usar un mapa para consolidar productos duplicados y contar cantidades si es necesario
        // Pero para simplificar, usaremos la lista actual productsToSell:
        for (Product p : productsToSell) {
            // Asumo que tienes un método getPrice() en Product
            productsDetails.append(String.format("  - %s (x1) @ $%.2f\n", 
                    p.getName(), p.getPrice()));
        }

        String invoiceContent
                = "----------------------------------------\n"
                + "      🍿 CINEMA UCR - Product Sales\n"
                + "----------------------------------------\n"
                + "Client: " + customerName + "\n"
                + "Client ID: " + foundCustomer.getId() + "\n"
                + "Membership: " + membership + String.format(" (%.0f%%)\n", discountRate)
                + "Date: " + sdf.format(new Date()) + "\n"
                + "----------------------------------------\n"
                + "PRODUCTS SOLD:\n"
                + productsDetails.toString() // Lista de productos
                + "----------------------------------------\n"
                + String.format("SUBTOTAL: $%.2f\n", subtotalBase)
                + String.format("DISCOUNT: -$%.2f\n", discountAmount) // Descuento aplicado
                + "----------------------------------------\n"
                + String.format("SUBTOTAL NETO: $%.2f\n", (subtotalBase - discountAmount)) // Subtotal post-descuento
                + String.format("TAX (13%%): $%.2f\n", tax)
                + "----------------------------------------\n"
                + String.format("TOTAL: $%.2f\n", totalFinal) // Total final con descuento y tax
                + "----------------------------------------\n"
                + "Thank you for your purchase! 🎬";

        JTextArea txt = new JTextArea(invoiceContent);
        txt.setEditable(false);
        txt.setFont(new Font("Consolas", Font.PLAIN, 16));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JScrollPane(txt), BorderLayout.CENTER);

        // Panel y botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        // --- Botón de Imprimir ---
        JButton btnPrint = new JButton("🖨 Print Invoice");
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPrint.setBackground(navyBlue);
        btnPrint.setForeground(Color.WHITE);
        btnPrint.addActionListener(e -> printInvoice(invoiceContent));

        // --- Botón de Cerrar ---
        JButton btnClose = new JButton("❌ Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(red);
        btnClose.setForeground(Color.WHITE);
        btnClose.addActionListener(e -> invoiceFrame.dispose());

        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        invoiceFrame.add(contentPanel);
        invoiceFrame.setVisible(true);

        saveInvoiceToFile(invoiceContent);
    }

    // =========================================================
    // LÓGICA DE IMPRESIÓN Y GUARDADO
    // =========================================================
    private void printInvoice(String content) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new StringPrintable(content));

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "✅ Invoice sent to printer.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error during printing: "
                        + ex.getMessage(), "Printing Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveInvoiceToFile(String content) {
        try {
            File folder = new File(INVOICE_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder,
                    "invoice_PRODUCT_" + System.currentTimeMillis() + ".txt");

            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println(content);
            pw.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error saving file: " + ex.getMessage());
        }
    }

    // =========================================================
    // CLASE INTERNA PARA MANEJAR LA INTERFAZ PRINTABLE
    // =========================================================
    private static class StringPrintable implements Printable {

        private final String content;
        private final Font printFont = new Font("Monospaced", Font.PLAIN, 10);
        private final int LINE_HEIGHT = 12;

        public StringPrintable(String content) {
            this.content = content;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            g2d.setFont(printFont);

            String[] lines = content.split("\n");
            int y = LINE_HEIGHT;

            for (String line : lines) {
                g2d.drawString(line, 0, y);
                y += LINE_HEIGHT;
            }

            return PAGE_EXISTS;
        }
    }
}