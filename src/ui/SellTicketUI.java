package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;
import proyecto1.cinema.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SellTicketUI extends JFrame {

    private final TicketSalesModule module;
    private final String INVOICE_FOLDER = "invoice";

    // ==== CAMPOS PARA CLIENTE =====
    private JTextField txtClientId;
    private JTextField txtClientName;
    private Customer foundCustomer = null;
    private String customerName = "N/A";

    public SellTicketUI(TicketSalesModule module) {
        this.module = module;

        setTitle("🎬 Sell Ticket - Cinema POS");
        setSize(600, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);
        Color white = Color.WHITE;

        // ===== PANEL DE CLIENTE ARRIBA =====
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

        // ===== MOVIE PRICES =====
        Map<String, Double> moviePrices = new HashMap<>();
        moviePrices.put("THE CONJURING", 5.00);
        moviePrices.put("DOLLHOUSE", 4.50);
        moviePrices.put("HOPE ON THE STAGE", 4.00);
        moviePrices.put("WICKED", 5.50);
        moviePrices.put("NOW YOU SEE ME 3", 4.75);
        moviePrices.put("HARRY POTTER", 6.00);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(white);

        JLabel lblMovie = new JLabel("🎬 Movie Title:");
        JComboBox<String> cbMovie = new JComboBox<>(new String[]{
                "THE CONJURING", "DOLLHOUSE", "HOPE ON THE STAGE",
                "WICKED", "NOW YOU SEE ME 3", "HARRY POTTER"
        });

        JLabel lblRoom = new JLabel("🏛 Room Number:");
        JComboBox<String> cbRoom = new JComboBox<>(new String[]{"1", "2", "3"});

        JLabel lblSeat = new JLabel("💺 Seat Number:");
        JComboBox<String> cbSeat = new JComboBox<>();
        for (int i = 1; i <= 50; i++) cbSeat.addItem(String.valueOf(i));

        JLabel lblType = new JLabel("👤 Ticket Type:");
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Adult", "Child", "Senior"});

        JLabel lblPayment = new JLabel("💳 Payment Method:");
        JComboBox<String> cbPayment = new JComboBox<>(new String[]{"Cash", "Card"});

        JLabel lblPrice = new JLabel("💲 Price:");
        JTextField txtPrice = new JTextField();
        txtPrice.setEditable(false);

        cbMovie.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                txtPrice.setText(String.format("%.2f", moviePrices.get(e.getItem())));
            }
        });
        cbMovie.setSelectedIndex(0);

        formPanel.add(lblMovie); formPanel.add(cbMovie);
        formPanel.add(lblRoom); formPanel.add(cbRoom);
        formPanel.add(lblSeat); formPanel.add(cbSeat);
        formPanel.add(lblType); formPanel.add(cbType);
        formPanel.add(lblPayment); formPanel.add(cbPayment);
        formPanel.add(lblPrice); formPanel.add(txtPrice);

        add(formPanel, BorderLayout.CENTER);

        // ===== SAVE BUTTON =====
        JButton btnSave = new JButton("💾 Register Sale");
        btnSave.setBackground(red);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSave.addActionListener((ActionEvent e) -> {
            if (foundCustomer == null) {
                JOptionPane.showMessageDialog(this,
                        "❌ You must validate a customer before selling.",
                        "Customer Required", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price = Double.parseDouble(txtPrice.getText().replace(",", "."));
            Ticket t = new Ticket();
            t.setMovieTitle(cbMovie.getSelectedItem().toString());
            t.setRoomNumber(cbRoom.getSelectedItem().toString());
            t.setSitNumber(cbSeat.getSelectedItem().toString());
            t.setType(cbType.getSelectedItem().toString());
            t.setPaymentMethod(cbPayment.getSelectedItem().toString());
            t.setPrice(price);
            t.setDate(new Date());
            t.setStatus("Sold");

            module.addTicket(t);

            showInvoice(t);
            dispose();
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(white);
        bottom.add(btnSave);
        add(bottom, BorderLayout.SOUTH);
    }

    // ======================================================
    // CLIENT SEARCH FROM FILE (REUTILIZADO DEL PRODUCT MODULE)
    // ======================================================
    private Customer searchCustomerFromFile(String id) {
        File file = new File("customers.txt");
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String name=null,email=null,phone=null;
            boolean vip=false;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID: ") && line.substring(4).trim().equals(id)) {

                    line = br.readLine();
                    if (line != null && line.startsWith("Name: "))
                        name = line.substring(6).trim();

                    line = br.readLine();
                    if (line != null && line.startsWith("Email: "))
                        email = line.substring(7).trim();

                    line = br.readLine();
                    if (line != null && line.startsWith("Phone: "))
                        phone = line.substring(7).trim();

                    line = br.readLine();
                    if (line != null && line.startsWith("VIP: "))
                        vip = Boolean.parseBoolean(line.substring(5).trim());

                    br.readLine(); // membership
                    br.readLine(); // ---- separator

                    return new Customer(id, name, email, phone, vip);
                }
            }
        } catch (Exception ignored) {}

        return null;
    }

    private void validateClient() {
        String id = txtClientId.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID.");
            return;
        }

        Customer c = searchCustomerFromFile(id);

        if (c == null) {
            foundCustomer = null;
            customerName = "N/A";
            txtClientName.setText("Not Found");
            JOptionPane.showMessageDialog(this, "❌ Customer not found.");
        } else {
            foundCustomer = c;
            customerName = c.getName();
            txtClientName.setText(customerName);
            JOptionPane.showMessageDialog(this, "✅ Customer Found: " + customerName);
        }
    }

    // =========================================================
    // FACTURA MODIFICADA — AHORA MUESTRA CLIENTE
    // =========================================================
    private void showInvoice(Ticket t) {
        JFrame invoiceFrame = new JFrame("🧾 Ticket Invoice");
        invoiceFrame.setSize(380, 520);
        invoiceFrame.setLocationRelativeTo(null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        double tax = t.getPrice() * 0.13;
        double total = t.getPrice() + tax;

        String invoiceContent =
                "----------------------------------------\n" +
                "          🎟 CINEMA INVOICE\n" +
                "----------------------------------------\n" +
                "Client: " + customerName + "\n" +
                "Client ID: " + foundCustomer.getId() + "\n" +
                "----------------------------------------\n" +
                "Movie: " + t.getMovieTitle() + "\n" +
                "Room: " + t.getRoomNumber() + "\n" +
                "Seat: " + t.getSitNumber() + "\n" +
                "Type: " + t.getType() + "\n" +
                "Payment: " + t.getPaymentMethod() + "\n" +
                "Date: " + sdf.format(t.getDate()) + "\n" +
                "----------------------------------------\n" +
                String.format("PRICE: $%.2f\n", t.getPrice()) +
                String.format("TAX (13%%): $%.2f\n", tax) +
                "----------------------------------------\n" +
                String.format("TOTAL: $%.2f\n", total) +
                "----------------------------------------\n" +
                "Thank you for your purchase! 🍿";

        JTextArea txt = new JTextArea(invoiceContent);
        txt.setEditable(false);
        txt.setFont(new Font("Consolas", Font.PLAIN, 16));

        invoiceFrame.add(new JScrollPane(txt));
        invoiceFrame.setVisible(true);

        saveInvoiceToFile(t, invoiceContent);
    }

    private void saveInvoiceToFile(Ticket t, String content) {
        try {
            File folder = new File(INVOICE_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder,
                    "invoice_" + System.currentTimeMillis() + ".txt");

            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println(content);
            pw.close();

            JOptionPane.showMessageDialog(this,
                    "Invoice saved:\n" + file.getAbsolutePath());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving file: " + ex.getMessage());
        }
    }
}