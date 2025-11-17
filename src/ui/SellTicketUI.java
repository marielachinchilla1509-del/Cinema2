package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SellTicketUI extends JFrame {

    private final TicketSalesModule module;

    public SellTicketUI(TicketSalesModule module) {
        this.module = module;

        setTitle("🎬 Sell Ticket - Cinema POS");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // ===== COLORS =====
        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);
        Color white = Color.WHITE;

        // ===== MOVIE PRICES =====
        Map<String, Double> moviePrices = new HashMap<>();
        moviePrices.put("THE CONJURING", 5.00);
        moviePrices.put("DOLLHOUSE", 4.50);
        moviePrices.put("HOPE ON THE STAGE", 4.00);
        moviePrices.put("WICKED", 5.50);
        moviePrices.put("NOW YOU SEE ME 3", 4.75);
        moviePrices.put("HARRY POTTER", 6.00);

        // ===== TOP TITLE BAR =====
        JLabel title = new JLabel("🎟 Ticket Selling Window", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setOpaque(true);
        title.setBackground(navy);
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 15, 15));
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
        txtPrice.setEditable(false); // 🔒 user cannot type manually

        // ===== Auto-fill price based on movie =====
        cbMovie.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedMovie = e.getItem().toString();
                double price = moviePrices.getOrDefault(selectedMovie, 0.0);
                txtPrice.setText(String.format("%.2f", price));
            }
        });

        // set initial price
        cbMovie.setSelectedIndex(0);

        // ===== ADD COMPONENTS =====
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
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(220, 50));

        // ===== FIXED ACTION =====
        btnSave.addActionListener((ActionEvent e) -> {
            try {
                // ✅ Corrige coma decimal según configuración regional
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

                // ✅ Show Invoice Window and save to file
                showInvoice(t);

                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Price could not be loaded.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(white);
        bottomPanel.add(btnSave);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ====== INVOICE WINDOW ======
    private void showInvoice(Ticket t) {
        JFrame invoiceFrame = new JFrame("🧾 Ticket Invoice");
        invoiceFrame.setSize(400, 500);
        invoiceFrame.setLocationRelativeTo(null);
        invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("🎬 CINEMA INVOICE", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(0x001F3F));
        panel.add(header, BorderLayout.NORTH);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat fileSdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);

        double tax = t.getPrice() * 0.13;
        double total = t.getPrice() + tax;

        String invoiceContent = 
                "----------------------------------------\n" +
                "         🎟  CINEMA INVOICE\n" +
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

        textArea.setText(invoiceContent);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ===== SAVE INVOICE BUTTON =====
        JButton btnSaveInvoice = new JButton("💾 Save Invoice to File");
        btnSaveInvoice.setBackground(new Color(0x228B22));
        btnSaveInvoice.setForeground(Color.WHITE);
        btnSaveInvoice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSaveInvoice.setFocusPainted(false);
        btnSaveInvoice.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSaveInvoice.addActionListener(e -> saveInvoiceToFile(t, invoiceContent));

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(0xB22222));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> invoiceFrame.dispose());

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(Color.WHITE);
        bottom.add(btnSaveInvoice);
        bottom.add(btnClose);
        panel.add(bottom, BorderLayout.SOUTH);

        invoiceFrame.add(panel);
        invoiceFrame.setVisible(true);
        
        // Auto-save invoice when window opens
        saveInvoiceToFile(t, invoiceContent);
    }

    // ===== SAVE INVOICE TO TXT FILE =====
    private void saveInvoiceToFile(Ticket t, String invoiceContent) {
        try {
            SimpleDateFormat fileSdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "invoice_" + fileSdf.format(t.getDate()) + ".txt";
            
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            // Write invoice content to file
            printWriter.println(invoiceContent);
            printWriter.println("\n=== SYSTEM GENERATED INVOICE ===");
            printWriter.println("Generated on: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            printWriter.println("File: " + fileName);
            
            printWriter.close();
            
            JOptionPane.showMessageDialog(this,
                "✅ Invoice saved successfully!\n" +
                "File: " + fileName,
                "Invoice Saved",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error saving invoice: " + e.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== MAIN FOR TESTING =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SellTicketUI(new TicketSalesModule()).setVisible(true));
    }
}