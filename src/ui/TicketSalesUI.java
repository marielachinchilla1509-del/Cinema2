package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import proyecto1.cinema.TicketSalesModule;

public class TicketSalesUI extends JFrame {

    private final TicketSalesModule module; // Reference to the backend sales module

    // Constructor: sets up the sales module interface
    public TicketSalesUI() {
        this.module = new TicketSalesModule();

        setTitle("🎟️ Sales Module"); // Window title
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setSize(1000, 650); // Window size
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout()); // Main layout
        getContentPane().setBackground(Color.WHITE); // Background color

        // ==== COLORS ====
        Color red = new Color(139, 0, 0);
        Color darkGray = new Color(45, 45, 45);
        Color navyBlue = new Color(100, 185, 230);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // ==== TITLE ====
        JLabel title = new JLabel("🎟️ Sales Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH); // Add title to top

        // ==== BUTTON PANEL ====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30)); // 2x2 grid with gaps
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120)); // Padding
        buttonPanel.setBackground(white);

        // Create main buttons
        JButton btnSellTicket = createBigButton("🎬 Sell Ticket", navyBlue, white);
        JButton btnSellProduct = createBigButton("🍿 Sell Product", navyBlue, white);
        JButton btnInvoiceList = createBigButton("📋 Invoice List", navyBlue, white);
        JButton btnProductList = createBigButton("📦 Product List", navyBlue, white);

        // Add buttons to panel
        buttonPanel.add(btnSellTicket);
        buttonPanel.add(btnSellProduct);
        buttonPanel.add(btnInvoiceList);
        buttonPanel.add(btnProductList);

        add(buttonPanel, BorderLayout.CENTER); // Add button panel to center

        // ==== BACK BUTTON PANEL ====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Right aligned
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅️ Back to Main Menu"); // Back button
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBackground(red);
        btnBack.setForeground(white);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(180, 40));

        // Hover effect for back button
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(Color.GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(Color.LIGHT_GRAY);
            }
        });

        backPanel.add(btnBack);
        add(backPanel, BorderLayout.SOUTH); // Add back button panel at bottom

        // ==== EVENTS ====
        btnSellTicket.addActionListener(e -> new SellTicketUI(module).setVisible(true)); // Open ticket sale UI
        btnSellProduct.addActionListener(e -> new SellProductUI(module).setVisible(true)); // Open product sale UI
        btnInvoiceList.addActionListener(e -> new InvoiceListUI().setVisible(true)); // Open invoice list
        btnProductList.addActionListener(e -> new ProductListUI().setVisible(true)); // Open product list

        btnBack.addActionListener(e -> dispose()); // Close this window and return
    }

    // ==== CREATE BIG BUTTON ====
    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);

        button.setFont(new Font("Inter", Font.BOLD, 22)); // Button font
        button.setBackground(bg); // Background color
        button.setForeground(fg); // Text color
        button.setFocusPainted(false);

        // Reasonable padding and size
        button.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        button.setPreferredSize(new Dimension(350, 120));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Hover effect: darken color on mouse enter
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });

        return button;
    }

    // ==== MAIN ====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketSalesUI().setVisible(true)); // Launch UI
    }
}
