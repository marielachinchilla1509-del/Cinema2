package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductUI extends JFrame {

    public ProductUI() {

        setTitle("📦 Product Module");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==== COLORS ====
        Color red = new Color(200, 30, 30);
        Color darkGray = new Color(45, 45, 45);
        Color blue = new Color(20, 40, 120);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // ==== TITLE ====
        JLabel title = new JLabel("📦 Product Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== GRID PANEL ====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnRegister = createBigButton("➕ Register Product", red, white);
        JButton btnInventory = createBigButton("📦 Product Inventory", blue, white);
        JButton btnEdit = createBigButton("✏️ Edit Product", darkGray, white);
        JButton btnDelete = createBigButton("🗑️ Delete Product", black, white);

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnInventory);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.CENTER);

        // ==== BACK BUTTON ====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅️ Back to Main Menu");
        btnBack.setFont(new Font("Inter", Font.PLAIN, 16));
        btnBack.setBackground(Color.LIGHT_GRAY);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(200, 40));

        // Hover effect
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

        btnRegister.addActionListener(e -> new RegisterProductUI().setVisible(true));

        btnInventory.addActionListener(e -> new InventoryProductUI().setVisible(true));

        btnEdit.addActionListener(e -> new EditProductUI().setVisible(true));

        btnDelete.addActionListener(e -> new DeleteProductUI().setVisible(true));
            
       

        btnBack.addActionListener(e -> dispose());
    }

    // ==== CREATE BIG BUTTON ====
    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);

        button.setFont(new Font("Inter", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);

        button.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        button.setPreferredSize(new Dimension(350, 120));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Hover
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductUI().setVisible(true));
    }
}
