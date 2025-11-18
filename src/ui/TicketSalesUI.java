package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import proyecto1.cinema.TicketSalesModule;

public class TicketSalesUI extends JFrame {

    private final TicketSalesModule module;

    public TicketSalesUI() {
        this.module = new TicketSalesModule();

        setTitle("🎟️ Sales Module");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==== COLORS ====
        Color red = new Color(200, 30, 30);
        Color darkGray = new Color(45, 45, 45);
        Color navyBlue = new Color(10, 25, 60);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // ==== TITLE ====
        JLabel title = new JLabel("🎟️ Sales Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== BUTTON PANEL ====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnSellTicket = createBigButton("🎬 Sell Ticket", red, white);
        JButton btnSellProduct = createBigButton("🍿 Sell Product", navyBlue, white);
        JButton btnInvoiceList = createBigButton("📋 Invoice List", darkGray, white);
        JButton btnProductList = createBigButton("📦 Product List", black, white);

        buttonPanel.add(btnSellTicket);
        buttonPanel.add(btnSellProduct);
        buttonPanel.add(btnInvoiceList);
        buttonPanel.add(btnProductList);

        add(buttonPanel, BorderLayout.CENTER);

        // ==== BACK BUTTON PANEL ====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅️ Back to Main Menu");
        btnBack.setFont(new Font("Inter", Font.PLAIN, 16));
        btnBack.setBackground(Color.LIGHT_GRAY);
        btnBack.setForeground(Color.BLACK);
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
        add(backPanel, BorderLayout.SOUTH);

        // ==== EVENTS ====
        btnSellTicket.addActionListener(e -> new SellTicketUI(module).setVisible(true));
        btnSellProduct.addActionListener(e -> new SellProductUI(module).setVisible(true));
        btnInvoiceList.addActionListener(e -> new InvoiceListUI().setVisible(true));
        btnProductList.addActionListener(e -> new ProductListUI().setVisible(true));

        btnBack.addActionListener(e -> dispose());
    }

    // ==== CREATE BIG BUTTON ====
    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);

        button.setFont(new Font("Inter", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);

        // Ajuste tamaño razonable
        button.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        button.setPreferredSize(new Dimension(350, 120));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Hover effect
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
        SwingUtilities.invokeLater(() -> new TicketSalesUI().setVisible(true));
    }
}
