package ui;

import proyecto1.cinema.Ticket;
import proyecto1.cinema.TicketSalesModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

        // ===== COLORS =====
        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);
        Color darkGray = new Color(0x2E2E2E);
        Color white = Color.WHITE;

        // ===== TOP BAR =====
        JLabel title = new JLabel("🍫 Product Sales Menu", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(navy);
        title.setForeground(white);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ===== PRODUCT BUTTONS =====
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

        // ===== TOTAL + FINISH BUTTON =====
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

    // ===== Helper to create product buttons =====
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
            JOptionPane.showMessageDialog(this, label.split("-")[0].trim() + " added!");
        });
        return button;
    }

    private void updateTotalLabel() {
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    // ===== Finish purchase =====
    private void finishPurchase(ActionEvent e) {
        if (total <= 0) {
            JOptionPane.showMessageDialog(this,
                    "No products selected.",
                    "Empty Purchase",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create ticket-like record
        Ticket t = new Ticket();
        t.setTicketId("P" + System.currentTimeMillis());
        t.setMovieTitle("Snack Purchase");
        t.setPrice(total);
        t.setDate(new Date());
        t.setStatus("Paid");
        t.setPaymentMethod("Cash");
        t.setType("Product");

        module.addTicket(t);

        JOptionPane.showMessageDialog(this,
                "✅ Purchase completed!\nTotal: $" + String.format("%.2f", total),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // ===== MAIN FOR TESTING =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SellProductUI(new TicketSalesModule()).setVisible(true));
    }
}
