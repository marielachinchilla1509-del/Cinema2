package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import proyecto1.cinema.TicketSalesModule;

public class TicketSalesUI extends JFrame {

    private final TicketSalesModule module;

    public TicketSalesUI() {
        this.module = new TicketSalesModule();

        setTitle("🎟️ Ticket Sales Module - Proyecto1Cinema");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==== PALETA DE COLORES ====
        Color red = new Color(200, 30, 30);         // rojo principal
        Color darkGray = new Color(45, 45, 45);     // gris oscuro
        Color navyBlue = new Color(10, 25, 60);     // azul marino
        Color black = new Color(0, 0, 0);           // negro
        Color white = Color.WHITE;

        // ==== TÍTULO ====
        JLabel title = new JLabel("🎟️ Ticket Sales Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== PANEL DE BOTONES ====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        buttonPanel.setBackground(white);

        JButton btnSellTicket = createBigButton("🎬 Sell Ticket", red, white);
        JButton btnSellProduct = createBigButton("🍿 Sell Product", navyBlue, white);
        JButton btnList = createBigButton("📋 Ticket List", darkGray, white);
        JButton btnSave = createBigButton("💾 Save Ticket List", black, white);
        JButton btnLoad = createBigButton("📂 Load Ticket List", navyBlue, white);
        JButton btnBack = createBigButton("⬅️ Back to Main Menu", darkGray, white);

        buttonPanel.add(btnSellTicket);
        buttonPanel.add(btnSellProduct);
        buttonPanel.add(btnList);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.CENTER);

        // ==== EVENTOS DE LOS BOTONES ====
        btnSellTicket.addActionListener(e -> {
            new SellTicketUI(module).setVisible(true);
        });


         btnSellProduct.addActionListener(e -> {
            new SellProductUI(module).setVisible(true);
        });

        btnList.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "📋 Showing ticket list...");
        });

        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "💾 Saving ticket list...");
        });

        btnLoad.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "📂 Loading ticket list...");
        });

        btnBack.addActionListener(e -> dispose());
    }

    // ==== MÉTODO PARA CREAR BOTONES GRANDES ====
    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 100));

        // Efecto hover
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

    // ==== MAIN PARA PRUEBA ====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketSalesUI().setVisible(true));
    }
}

