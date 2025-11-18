package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SellProductUI extends JFrame {

    private final TicketSalesModule module;
    private double total = 0.0;
    private JLabel totalLabel;

    public SellProductUI(TicketSalesModule module) {
        this.module = module;

        setTitle("🍿 Sell Product - Cinema POS");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);
        Color darkGray = new Color(0x2E2E2E);
        Color white = Color.WHITE;

        JLabel title = new JLabel("🍫 Product Sales Menu", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(navy);
        title.setForeground(white);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel productPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        productPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        productPanel.setBackground(white);

        JButton btnPopcorn = createProductButton("🍿 Popcorn - $3.00", 3.00, red, white);
        JButton btnSoda = createProductButton("🥤 Soda - $2.00", 2.00, darkGray, white);
        JButton btnCandy = createProductButton("🍬 Candy - $1.50", 1.50, navy, white);
        JButton btnNachos = createProductButton("🧀 Nachos - $3.50", 3.50, red.darker(), white);

        productPanel.add(btnPopcorn);
        productPanel.add(btnSoda);
        productPanel.add(btnCandy);
        productPanel.add(btnNachos);

        add(productPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        bottomPanel.setBackground(white);

        totalLabel = new JLabel("Total: $0.00", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(darkGray);
        bottomPanel.add(totalLabel, BorderLayout.CENTER);

        JButton btnFinish = new JButton("✅ Finish Purchase");
        btnFinish.setBackground(red);
        btnFinish.setForeground(white);
        btnFinish.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFinish.setFocusPainted(false);
        btnFinish.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFinish.setPreferredSize(new Dimension(200, 50));
        btnFinish.addActionListener(this::finishPurchase);

        bottomPanel.add(btnFinish, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createProductButton(String label, double price, Color bg, Color fg) {
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        button.addActionListener((ActionEvent e) -> {
            total += price;
            updateTotalLabel();
            JOptionPane.showMessageDialog(this,
                    label.split("-")[0].trim() + " added!");
        });

        return button;
    }

    private void updateTotalLabel() {
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void finishPurchase(ActionEvent e) {
        if (total <= 0) {
            JOptionPane.showMessageDialog(this,
                    "No products selected.",
                    "Empty Purchase",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Ticket t = new Ticket();
        t.setTicketId("P" + System.currentTimeMillis());
        t.setMovieTitle("Snack Purchase");
        t.setPrice(total);
        t.setDate(new Date());
        t.setStatus("Paid");
        t.setPaymentMethod("Cash");
        t.setType("Product");

        module.addTicket(t);

        showInvoice(t);
        saveInvoiceToFile(t);

        dispose();
    }

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

        double tax = Math.round(t.getPrice() * 0.13 * 100.0) / 100.0;
        double total = Math.round((t.getPrice() + tax) * 100.0) / 100.0;

        String invoiceText =
                "----------------------------------------\n" +
                "             🍫 CINEMA INVOICE\n" +
                "----------------------------------------\n" +
                "Purchase: Snack Products\n" +
                String.format("Date: %s\n", sdf.format(t.getDate())) +
                "Payment: Cash\n" +
                "----------------------------------------\n" +
                String.format("SUBTOTAL: $%.2f\n", t.getPrice()) +
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

    private void saveInvoiceToFile(Ticket t) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String fileName = "invoice_product_" + sdf.format(t.getDate()) + ".txt";

        double tax = Math.round(t.getPrice() * 0.13 * 100.0) / 100.0;
        double total = Math.round((t.getPrice() + tax) * 100.0) / 100.0;

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("----------------------------------------\n");
            writer.write("             🍫 CINEMA INVOICE\n");
            writer.write("----------------------------------------\n");
            writer.write("Purchase: Snack Products\n");
            writer.write("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(t.getDate()) + "\n");
            writer.write("Payment: Cash\n");
            writer.write("----------------------------------------\n");
            writer.write(String.format("SUBTOTAL: $%.2f\n", t.getPrice()));
            writer.write(String.format("TAX (13%%): $%.2f\n", tax));
            writer.write("----------------------------------------\n");
            writer.write(String.format("TOTAL: $%.2f\n", total));
            writer.write("\n----------------------------------------\n");
            writer.write("Thank you for your purchase! 🍿");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving invoice file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new SellProductUI(new TicketSalesModule()).setVisible(true));
    }
}
