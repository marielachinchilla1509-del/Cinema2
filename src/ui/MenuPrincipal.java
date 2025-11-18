package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Main Dashboard for Proyecto1Cinema
 * Redesigned: Large square buttons as main focus
 * Color palette: Navy Blue, Soft Pink, Dark Green, White, Dark Gray
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Proyecto1Cinema - Main Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🎨 Color palette
        Color navyBlue = new Color(15, 32, 62);
        Color softPink = new Color(255, 182, 193);
        Color darkGreen = new Color(0, 100, 0);
        Color white = Color.WHITE;
        Color darkGray = new Color(40, 40, 40);

        // 🧱 Header
        JPanel header = new JPanel();
        header.setBackground(navyBlue);
        header.setPreferredSize(new Dimension(getWidth(), 80));
        JLabel title = new JLabel("🎬 Proyecto1Cinema - Main Dashboard", SwingConstants.CENTER);
        title.setForeground(white);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // 🏠 Main panel with large buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new GridLayout(2, 2, 40, 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));

        // 🟦 Buttons
        JButton btnEmployees = createMainButton("Employee Management", navyBlue, white);
        JButton btnProducts = createMainButton("Product Management", darkGreen, white);
        JButton btnCustomers = createMainButton("Customer Management", softPink, darkGray);
        JButton btnTickets = createMainButton("Sales", darkGray, white);

        mainPanel.add(btnEmployees);
        mainPanel.add(btnProducts);
        mainPanel.add(btnCustomers);
        mainPanel.add(btnTickets);

        add(mainPanel, BorderLayout.CENTER);

        // 🔚 Footer with Exit button
        JPanel footer = new JPanel();
        footer.setBackground(white);
        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(new Color(220, 53, 69));
        btnExit.setForeground(white);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnExit.setFocusPainted(false);
        btnExit.setPreferredSize(new Dimension(180, 50));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(e -> System.exit(0));
        footer.add(btnExit);
        add(footer, BorderLayout.SOUTH);

        // ⚙️ Actions to open modules
        btnEmployees.addActionListener(e -> openModule("EmployeeUI"));
        btnProducts.addActionListener(e -> openModule("ProductUI"));
        btnCustomers.addActionListener(e -> openModule("CustomerUI"));
        btnTickets.addActionListener(e -> openModule("TicketSalesUI"));
    }

    /**
     * Creates large, squared, modern dashboard buttons.
     */
    private JButton createMainButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 200));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 4),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // 🖱 Hover effect
        button.addChangeListener(e -> {
            if (button.getModel().isRollover()) {
                button.setBackground(bg.brighter());
            } else {
                button.setBackground(bg);
            }
        });

        return button;
    }

    /**
     * Opens another module window if it exists.
     */
    private void openModule(String moduleName) {
        try {
            Class<?> moduleClass = Class.forName("ui." + moduleName);
            JFrame moduleWindow = (JFrame) moduleClass.getDeclaredConstructor().newInstance();
            moduleWindow.setVisible(true);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "The module '" + moduleName + "' is not available yet.",
                    "Module Missing", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening module: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal dashboard = new MenuPrincipal();
            dashboard.setVisible(true);
        });
    }
}

