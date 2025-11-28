package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.IOException; 
import javax.imageio.ImageIO; 

/**
 * Main Dashboard for Proyecto1Cinema Redesigned
 * Optimized for 15" Touch Screen (1280x800 resolution focus)
 */
public class MenuPrincipal extends JFrame {

    /**
     * Inner class for a panel with a full background image (now with sunflowers)
     */
    class FullBackgroundPanel extends JPanel {
        private Image backgroundImage;
        private String imagePath; 
        private final Color FALLBACK_COLOR = new Color(15, 32, 62); // Navy blue fallback if image not loaded

        public FullBackgroundPanel(String path) {
            this.imagePath = path;
            loadBackgroundImage();
            setLayout(new BorderLayout()); 
            setOpaque(true); 
        }

        // Load background image from resources
        private void loadBackgroundImage() {
            try {
                URL imgURL = getClass().getResource(imagePath);
                if (imgURL != null) {
                    backgroundImage = ImageIO.read(imgURL);
                } else {
                    System.err.println("Error: Could not find background image: " + imagePath);
                }
            } catch (IOException e) {
                System.err.println("Error loading background image: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Fill background with fallback color first
            g.setColor(FALLBACK_COLOR); 
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw the background image scaled to fill panel while keeping aspect ratio
            if (backgroundImage != null) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imageWidth = backgroundImage.getWidth(this);
                int imageHeight = backgroundImage.getHeight(this);

                double scaleX = (double) panelWidth / imageWidth;
                double scaleY = (double) panelHeight / imageHeight;
                double scale = Math.max(scaleX, scaleY); // Cover entire panel

                int scaledWidth = (int) (imageWidth * scale);
                int scaledHeight = (int) (imageHeight * scale);

                int x = (panelWidth - scaledWidth) / 2;
                int y = (panelHeight - scaledHeight) / 2; 

                g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, this);
            }
        }
    }

    /**
     * Helper method to load a header image (UCR emblem)
     */
    private JLabel loadHeaderImage(String path) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image originalImage = originalIcon.getImage();

                // Maintain aspect ratio
                int preferredHeight = 80; 
                int preferredWidth = (int) (originalImage.getWidth(null) * ((double) preferredHeight / originalImage.getHeight(null)));

                Image scaledImage = originalImage.getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JLabel imageLabel = new JLabel(scaledIcon, SwingConstants.CENTER);
                imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); 
                return imageLabel;
            } else {
                System.err.println("Error: Could not find header image: " + path);
                return new JLabel("IMAGE NOT FOUND", SwingConstants.CENTER);
            }
        } catch (Exception e) {
            System.err.println("Error loading header image: " + e.getMessage());
            return new JLabel("LOAD ERROR", SwingConstants.CENTER);
        }
    }

    public MenuPrincipal() {
        setTitle("CINEMA UCR - Business Info"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800); 
        setLocationRelativeTo(null);

        // ===== Color palette =====
        Color navyBlue = new Color(100, 185, 230);
        Color white = Color.WHITE;
        Color red = new Color(139, 0, 0);

        // ===== Header panel =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(navyBlue);
        header.setPreferredSize(new Dimension(getWidth(), 90)); 

        // Load UCR emblem
        JLabel headerImage = loadHeaderImage("ucr bonito.png"); 

        JPanel headerContent = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); 
        headerContent.setOpaque(false); 
        headerContent.add(headerImage);

        // Title text next to emblem
        JLabel titleText = new JLabel("UNIVERSIDAD DE COSTA RICA");
        titleText.setForeground(white);
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
        headerContent.add(titleText);

        header.add(headerContent, BorderLayout.CENTER);

        // ===== Full background panel with sunflower image =====
        FullBackgroundPanel fullBackground = new FullBackgroundPanel("girasoles.jpg");
        fullBackground.setLayout(new BorderLayout()); 
        fullBackground.add(header, BorderLayout.NORTH);

        // ===== Button panel in the center =====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 60, 60)); 
        buttonPanel.setOpaque(false); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80)); 

        // ===== Dashboard buttons =====
        JButton btnEmployees = createMainButton("Employee Management", navyBlue, white);
        JButton btnProducts = createMainButton("Product Management", navyBlue, white);
        JButton btnCustomers = createMainButton("Customer Management", navyBlue, white);
        JButton btnTickets = createMainButton("Sales", navyBlue, white);

        buttonPanel.add(btnEmployees);
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnCustomers);
        buttonPanel.add(btnTickets);

        fullBackground.add(buttonPanel, BorderLayout.CENTER);

        // ===== Footer with Exit button =====
        JPanel footer = new JPanel();
        footer.setBackground(navyBlue); 
        footer.setPreferredSize(new Dimension(getWidth(), 80)); 

        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(red);
        btnExit.setForeground(white);
        btnExit.setFont(new Font("Inter", Font.BOLD, 24));
        btnExit.setPreferredSize(new Dimension(250, 60)); 
        btnExit.setFocusPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Exit the application
        btnExit.addActionListener(e -> System.exit(0));
        footer.add(btnExit);
        fullBackground.add(footer, BorderLayout.SOUTH);

        // Add full background panel to frame
        add(fullBackground, BorderLayout.CENTER); 

        // ===== Actions for dashboard buttons =====
        btnEmployees.addActionListener(e -> openModule("EmployeeUI"));
        btnProducts.addActionListener(e -> openModule("ProductUI"));
        btnCustomers.addActionListener(e -> openModule("CustomerUI"));
        btnTickets.addActionListener(e -> openModule("TicketSalesUI"));
    }

    /**
     * Creates a large, modern dashboard button with hover effect
     */
    private JButton createMainButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bg); 
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 26)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(400, 250)); 

        // Custom border
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 6), 
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Hover effect: brighten background on rollover
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
     * Opens another module window if it exists
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
