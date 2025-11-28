package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;
import proyecto1.cinema.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.print.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SellTicketUI extends JFrame {

    private final TicketSalesModule module;
    private final String INVOICE_FOLDER = "invoice";

    // Colores
    private final Color navy = new Color(0x001F3F);
    private final Color red = new Color(0xB22222);
    private final Color white = Color.WHITE;

    // ==== CAMPOS PARA CLIENTE =====
    private JTextField txtClientId;
    private JTextField txtClientName;
    private Customer foundCustomer = null;
    private String customerName = "N/A";
    private String foundMembershipLevel = "None"; // <-- NUEVA VARIABLE: Almacena el nivel de membresía real

    // ==== CAMPOS PARA TIQUETE =====
    private JComboBox<String> cbMovie;
    private JComboBox<String> cbPayment;
    private JTextField txtPrice;
    private JLabel totalLabel;
    private final String location = "Auditorio Ssur";
    private double currentBasePrice = 0.0;

    // ==== MAPA DE PRECIOS =====
    private final Map<String, Double> moviePrices = new HashMap<>();

    // ==== Asientos y Selección =====
    private JPanel seatPanel;
    private final Map<String, JButton> seatButtonsMap = new HashMap<>();

    private String currentSelectedSeat = null;

    private static final Map<String, Boolean> allSeatsStatus = new HashMap<>();

    public SellTicketUI(TicketSalesModule module) {
        this.module = module;

        // Inicializar Precios
        moviePrices.put("THE CONJURING", 5.00);
        moviePrices.put("DOLLHOUSE", 4.50);
        moviePrices.put("HOPE ON THE STAGE", 4.00);
        moviePrices.put("WICKED", 5.50);
        moviePrices.put("NOW YOU SEE ME 3", 4.75);
        moviePrices.put("HARRY POTTER", 6.00);

        // --- TÍTULO Y CONFIGURACIÓN ---
        setTitle("🎬 CINEMA UCR - Info. Empresarial");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(white);
        setLayout(new BorderLayout(15, 15));

        // ===== PANEL DE CLIENTE (NORTE) =====
        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        clientPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(navy),
                "👤 Customer Validation",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), navy
        ));
        clientPanel.setBackground(new Color(245, 245, 255));

        txtClientId = new JTextField(10);
        txtClientName = new JTextField("N/A", 18);
        txtClientName.setEditable(false);
        txtClientName.setBackground(Color.LIGHT_GRAY);

        JButton btnValidate = new JButton("Validate");
        btnValidate.setBackground(navy);
        btnValidate.setForeground(Color.WHITE);

        btnValidate.addActionListener(e -> validateClient());
        txtClientId.addActionListener(e -> validateClient());

        clientPanel.add(new JLabel("ID:"));
        clientPanel.add(txtClientId);
        clientPanel.add(btnValidate);
        clientPanel.add(new JLabel("Client:"));
        clientPanel.add(txtClientName);

        add(clientPanel, BorderLayout.NORTH);

        // ===== PANEL CENTRAL: IZQUIERDA (FORMULARIO) y DERECHA (ASIENTOS) =====
        JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mainPanel.setBackground(white);

        // --- IZQUIERDA: FORMULARIO ---
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- DERECHA: SELECCIÓN DE ASIENTOS ---
        JPanel seatWrapperPanel = createSeatSelectionPanel();
        mainPanel.add(seatWrapperPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        // ===== PANEL INFERIOR: TOTAL Y BOTÓN GUARDAR =====
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(white);

        totalLabel = new JLabel("Total (0 Seat): $0.00", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(red);

        JButton btnSave = new JButton("💾 Confirm and Pay");
        btnSave.setBackground(red);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(this::sellTickets);

        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(btnSave, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateTotalLabel();
    }

    // ======================================================
    // MÉTODOS DE CONSTRUCCIÓN DE UI
    // ======================================================
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBackground(white);

        JLabel lblLocation = new JLabel("📌 Location:");
        lblLocation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel txtLocation = new JLabel(location);
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLocation.setForeground(navy);
        formPanel.add(lblLocation);
        formPanel.add(txtLocation);

        JLabel lblMovie = new JLabel("🎬 Movie Title:");
        cbMovie = new JComboBox<>(moviePrices.keySet().toArray(new String[0]));

        JLabel lblPayment = new JLabel("💳 Payment Method:");
        cbPayment = new JComboBox<>(new String[]{"Cash", "Card"});

        JLabel lblPrice = new JLabel("💲 Base Price:");
        txtPrice = new JTextField();
        txtPrice.setEditable(false);

        cbMovie.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String movie = (String) e.getItem();
                currentBasePrice = moviePrices.getOrDefault(movie, 0.0);
                txtPrice.setText(String.format("%.2f", currentBasePrice));
                updateTotalLabel();
            }
        });

        cbMovie.setSelectedIndex(0);

        formPanel.add(lblMovie);
        formPanel.add(cbMovie);
        formPanel.add(lblPayment);
        formPanel.add(cbPayment);
        formPanel.add(lblPrice);
        formPanel.add(txtPrice);

        formPanel.add(new JLabel());
        formPanel.add(new JLabel());

        return formPanel;
    }

    private JPanel createSeatSelectionPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(white);
        wrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(navy),
                "💺 Seat Selection (Screen is at the top)",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), navy
        ));

        seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(14, 1, 5, 5));
        addSeats();

        JLabel screen = new JLabel("----- SCREEN -----", SwingConstants.CENTER);
        screen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        screen.setForeground(navy);

        wrapper.add(screen, BorderLayout.NORTH);
        wrapper.add(new JScrollPane(seatPanel), BorderLayout.CENTER);

        return wrapper;
    }

    private void addSeats() {
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                "L", "M", "N"};
        seatButtonsMap.clear();

        for (int r = 0; r < rows.length; r++) {
            JPanel rowPanel = new JPanel();
            int seatCount = (r == 0 || r == 13) ? 11 : 15;
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            for (int s = 1; s <= seatCount; s++) {
                JButton seatButton = new JButton(rows[r] + s);
                String seatName = rows[r] + s;

                seatButton.setPreferredSize(new Dimension(35, 35));
                seatButton.setMargin(new Insets(1, 1, 1, 1));

                if (!allSeatsStatus.containsKey(seatName)) {
                    allSeatsStatus.put(seatName, false);
                }

                if (allSeatsStatus.get(seatName)) {
                    seatButton.setBackground(Color.DARK_GRAY);
                    seatButton.setForeground(Color.WHITE);
                    seatButton.setEnabled(false);
                } else {
                    seatButton.setBackground(Color.LIGHT_GRAY);
                    seatButton.setForeground(Color.BLACK);
                    seatButton.addActionListener(e -> toggleSeat(seatButton));
                }

                rowPanel.add(seatButton);
                seatButtonsMap.put(seatName, seatButton);
            }
            seatPanel.add(rowPanel);
        }
    }

    // ======================================================
    // MÉTODOS DE LÓGICA DE VENTA Y VALIDACIÓN
    // ======================================================
    private void toggleSeat(JButton seatButton) {
        String seatName = seatButton.getText();

        if (seatName.equals(currentSelectedSeat)) {
            currentSelectedSeat = null;
            seatButton.setBackground(Color.LIGHT_GRAY);
            seatButton.setForeground(Color.BLACK);

        } else {
            if (currentSelectedSeat != null) {
                JButton oldButton = seatButtonsMap.get(currentSelectedSeat);
                if (oldButton != null) {
                    oldButton.setBackground(Color.LIGHT_GRAY);
                    oldButton.setForeground(Color.BLACK);
                }
            }

            currentSelectedSeat = seatName;
            seatButton.setBackground(navy.brighter());
            seatButton.setForeground(Color.WHITE);
        }
        updateTotalLabel();
    }

    /**
     * MODIFICADO para mapear Basic (5%), Pro (10%), Premium (15%) y None (0%).
     */
    private double getDiscountPercentage() {
        if (foundCustomer == null) {
            return 0.0;
        }

        // Mapeo de descuentos basado en el nivel real leído del archivo
        switch (foundMembershipLevel) {
            case "Premium":
                return 0.15; // 15%
            case "Pro":
                return 0.10; // 10%
            case "Basic":
                return 0.05; // 5%
            case "None":
            default:
                return 0.0; // 0% para clientes sin membresía o nivel no reconocido
        }
    }

    private double getDiscountAmount(double subtotal) {
        return subtotal * getDiscountPercentage();
    }

    /**
     * MODIFICADO para devolver el nivel real de membresía leído del archivo.
     */
    private String getMembershipLevel() {
        if (foundCustomer == null) {
            return "N/A";
        }

        return foundMembershipLevel;
    }

    private void updateTotalLabel() {
        int numSeats = (currentSelectedSeat != null) ? 1 : 0;
        double subtotal = numSeats * currentBasePrice;

        double discount = getDiscountAmount(subtotal); // Obtener descuento
        double subtotalAfterDiscount = subtotal - discount; // Aplicar descuento

        double tax = subtotalAfterDiscount * 0.13;
        double totalFinal = subtotalAfterDiscount + tax;

        if (numSeats == 0) {
            totalLabel.setText("Total (0 Seat): $0.00");
        } else {
            totalLabel.setText(String.format("Total (1 Seat): $%.2f", totalFinal));
        }
    }

    /**
     * MODIFICADO para incluir la lectura del campo "Membership:" en el archivo.
     */
    private Customer searchCustomerFromFile(String id) {
        File file = new File("customers.txt");
        if (!file.exists()) {
            this.foundMembershipLevel = "None"; // Limpiar si el archivo no existe
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String name = null, email = null, phone = null;
            boolean vip = false;
            String membership = "None"; // Temporal para la lectura

            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID: ") && line.substring(4).trim().equals(id)) {
                    
                    // Lee Name
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("Name: ")) {
                        name = line.substring(6).trim();
                    }
                    // Lee Email
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("Email: ")) {
                        email = line.substring(7).trim();
                    }
                    // Lee Phone
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("Phone: ")) {
                        phone
                                = line.substring(7).trim();
                    }
                    // Lee VIP
                    line = br.readLine();
                    if (line != null
                            && line.startsWith("VIP: ")) {
                        vip
                                = Boolean.parseBoolean(line.substring(5).trim());
                    }
                    
                    // *** INICIO DE CAMBIO ***
                    // Lee MEMBERSHIP
                    line = br.readLine(); 
                    if (line != null && line.startsWith("Membership: ")) {
                        membership = line.substring(12).trim();
                    }
                    
                    this.foundMembershipLevel = membership; // Asigna el nivel a la variable de instancia
                    
                    // *** FIN DE CAMBIO ***

                    br.readLine(); // Salta la línea separadora "----------"

                    return new Customer(id, name, email, phone, vip);
                }
            }
        } catch (Exception ignored) {
            this.foundMembershipLevel = "None"; // Limpiar en caso de error
        }
        this.foundMembershipLevel = "None"; // Limpiar si no se encuentra
        return null;
    }

    /**
     * MODIFICADO para limpiar la variable de estado de membresía.
     */
    private void validateClient() {
        String id = txtClientId.getText().trim();

        if (id.isEmpty()) {
            txtClientName.setText("N/A");
            foundMembershipLevel = "None"; // Limpiar estado
            JOptionPane.showMessageDialog(this, "Enter a valid ID.");
            return;
        }

        Customer c = searchCustomerFromFile(id);

        if (c == null) {
            foundCustomer = null;
            customerName = "N/A";
            txtClientName.setText("Not Found");
            foundMembershipLevel = "None"; // Limpiar estado
            JOptionPane.showMessageDialog(this, "❌ Customer not found.");
        } else {
            foundCustomer = c;
            customerName = c.getName();
            txtClientName.setText(customerName);
            String level = getMembershipLevel(); // Ahora usa el nivel real
            double discount = getDiscountPercentage() * 100;
            
            JOptionPane.showMessageDialog(this,
                    String.format("✅ Customer Found: %s\n🏷 Membership: %s (%.0f%% Discount)",
                        customerName, level, discount));
            updateTotalLabel();
        }
    }

    private void sellTickets(ActionEvent e) {
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "❌ You must validate a customer before selling.",
                    "Customer Required", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (currentSelectedSeat == null) {
            JOptionPane.showMessageDialog(this,
                    "Select a single seat.",
                    "No Seat Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String seat = currentSelectedSeat;
        double subtotal = currentBasePrice;

        String movieTitle = (String) cbMovie.getSelectedItem();
        String paymentMethod = (String) cbPayment.getSelectedItem();

        Ticket t = new Ticket();
        t.setTicketId("T" + System.currentTimeMillis() + "-" + seat);
        t.setMovieTitle(movieTitle);
        t.setRoomNumber(location);
        t.setSitNumber(seat);
        t.setType("General");
        t.setPaymentMethod(paymentMethod);
        t.setPrice(currentBasePrice);
        t.setDate(new Date());
        t.setStatus("Sold");

        // --- CÁLCULOS CON DESCUENTO APLICADO ---
        double discountRate = getDiscountPercentage();
        double discountAmount = subtotal * discountRate;
        double subtotalAfterDiscount = subtotal - discountAmount;

        double tax = subtotalAfterDiscount * 0.13;
        double totalFinal = subtotalAfterDiscount + tax;

        module.addTicket(t);

        markSeatAsSold(seat);

        showInvoice(t, subtotal, discountAmount, tax, totalFinal); // Parámetros actualizados

        currentSelectedSeat = null;
        updateTotalLabel();
        dispose();
    }

    private void markSeatAsSold(String seat) {
        allSeatsStatus.put(seat, true);

        JButton button = seatButtonsMap.get(seat);
        if (button != null) {
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            button.setEnabled(false);
        }
    }

    // =========================================================
    // FACTURA CONSOLIDAD (MODIFICADA PARA INCLUIR DESCUENTO)
    // =========================================================
    private void showInvoice(Ticket soldTicket, double subtotal, double discountAmount, double tax,
                double totalFinal) {
        JFrame invoiceFrame = new JFrame("🧾 Ticket Invoice");
        invoiceFrame.setSize(400, 550); 
        invoiceFrame.setLocationRelativeTo(null);
        invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String membership = getMembershipLevel();
        double discountRate = getDiscountPercentage() * 100;

        String invoiceContent
                = "----------------------------------------\n"
                + "      🎟 CINEMA UCR - Info. Empresarial\n"
                + "----------------------------------------\n"
                + "Client: " + customerName + "\n"
                + "Client ID: " + foundCustomer.getId() + "\n"
                + "Membership: " + membership + String.format(" (%.0f%%)\n", discountRate) 
                + "Date: " + sdf.format(new Date()) + "\n"
                + "----------------------------------------\n"
                + "Movie: " + soldTicket.getMovieTitle() + "\n"
                + "Location: " + location + "\n"
                + "Ticket Type: General\n"
                + "Seat: " + soldTicket.getSitNumber() + "\n"
                + "Payment: " + soldTicket.getPaymentMethod() + "\n"
                + "Base Price: $" + String.format("%.2f", currentBasePrice) + "\n"
                + "----------------------------------------\n"
                + String.format("SUBTOTAL: $%.2f\n", subtotal)
                + String.format("DISCOUNT: -$%.2f\n", discountAmount) // Descuento aplicado
                + "----------------------------------------\n"
                + String.format("SUBTOTAL NETO: $%.2f\n", (subtotal - discountAmount)) // Subtotal post-descuento
                + String.format("TAX (13%%): $%.2f\n", tax)
                + "----------------------------------------\n"
                + String.format("TOTAL: $%.2f\n", totalFinal) // Total final con descuento y tax
                + "----------------------------------------\n"
                + "Thank you for your purchase! 🍿";

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
        btnPrint.setBackground(navy);
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

        saveInvoiceToFile(soldTicket, invoiceContent);
    }

    // =========================================================
    // LÓGICA DE IMPRESIÓN
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

    private void saveInvoiceToFile(Ticket t, String content) {
        try {
            File folder = new File(INVOICE_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder,
                    "invoice_TICKET_" + System.currentTimeMillis() + ".txt");

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