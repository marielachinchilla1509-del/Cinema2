package ui;

import proyecto1.cinema.Customer;
import proyecto1.cinema.Membership;
import proyecto1.cinema.TypeMembership;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CustomerUI extends JFrame {

    private Customer[] customers = new Customer[200];
    private Membership[] memberships = new Membership[200];
    private int count = 0;

    public CustomerUI() {

        setSize(1000, 650);
        setTitle("👤 Customer Module");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JLabel title = new JLabel("👤 Customer Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== BUTTON PANEL ====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnRegister = createBigButton("👤 Register Customer", red, white);
        JButton btnList = createBigButton("📄 Customer List", navyBlue, white);
        JButton btnSearch = createBigButton("🔍 Search Customer", darkGray, white);
        JButton btnSave = createBigButton("💾 Save to TXT", black, white);
        JButton btnLoad = createBigButton("📁 Load TXT", darkGray, white);

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnList);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoad);

        add(buttonPanel, BorderLayout.CENTER);

        // ==== BACK BUTTON ====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅️ Back to Menu");
        btnBack.setFont(new Font("Inter", Font.PLAIN, 16));
        btnBack.setBackground(Color.LIGHT_GRAY);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(180, 40));

        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);
        add(backPanel, BorderLayout.SOUTH);

        // ==== EVENTS ====
        btnRegister.addActionListener(e -> openRegisterWindow());
        btnList.addActionListener(e -> listCustomers());
        btnSearch.addActionListener(e -> openSearchWindow());
        btnSave.addActionListener(e -> openSaveWindow());
        btnLoad.addActionListener(e -> openLoadWindow());
    }

    // Button Style
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
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { button.setBackground(bg.darker()); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { button.setBackground(bg); }
        });
        return button;
    }

    // ============================================================
    // REGISTER WINDOW
    // ============================================================
    private void openRegisterWindow() {

        JFrame reg = new JFrame("👤 Register Customer");
        reg.setSize(500, 650);
        reg.setLocationRelativeTo(null);
        reg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reg.setLayout(new BorderLayout(10, 10));
        reg.getContentPane().setBackground(Color.WHITE);

        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);

        JLabel header = new JLabel("👤 Register Customer", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setOpaque(true);
        header.setBackground(navy);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        reg.add(header, BorderLayout.NORTH);

        // FORM PANEL
        JPanel form = new JPanel(new GridLayout(8, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        form.setBackground(Color.WHITE);

        JTextField txtName = createField("Name");
        JTextField txtId = createField("ID");
        JTextField txtEmail = createField("Email");
        JTextField txtPhone = createField("Phone");

        JCheckBox chkVip = new JCheckBox("VIP Customer");
        chkVip.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        chkVip.setBackground(Color.WHITE);

        // MEMBERSHIP PANEL (VISIBLE ONLY IF VIP)
        JPanel membershipPanel = new JPanel();
        membershipPanel.setLayout(new GridLayout(1,1));
        membershipPanel.setBackground(Color.WHITE);

        String[] membershipOptions = {"BASIC", "PRO", "PREMIUM"};
        JComboBox<String> cbMembership = new JComboBox<>(membershipOptions);
        cbMembership.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        membershipPanel.add(cbMembership);
        membershipPanel.setVisible(false); // initially hidden

        // Event: show/hide membership field
        chkVip.addActionListener(e -> {
            membershipPanel.setVisible(chkVip.isSelected());
        });

        form.add(txtName);
        form.add(txtId);
        form.add(txtEmail);
        form.add(txtPhone);
        form.add(chkVip);
        form.add(membershipPanel);

        reg.add(form, BorderLayout.CENTER);

        // SAVE BUTTON
        JButton btnSave = new JButton("✅ Register Customer");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSave.setBackground(red);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(200, 50));

        btnSave.addActionListener(e -> {

            Customer c = new Customer();
            Membership m = new Membership();

            c.setName(txtName.getText());
            c.setId(txtId.getText());
            c.setEmail(txtEmail.getText());
            c.setPhoneNumber(txtPhone.getText());
            c.setVip(chkVip.isSelected());

            // Only set membership if VIP
            if (chkVip.isSelected()) {
                m.setType(TypeMembership.valueOf(cbMembership.getSelectedItem().toString()));
            } else {
                m.setType(null); // no membership
            }

            customers[count] = c;
            memberships[count] = m;
            count++;

            JOptionPane.showMessageDialog(reg, "Customer Registered!");
            reg.dispose();
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnSave);

        reg.add(bottom, BorderLayout.SOUTH);
        reg.setVisible(true);
    }

    private JTextField createField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tf.setBorder(BorderFactory.createTitledBorder(placeholder));
        return tf;
    }

    // ============================================================
    // LIST CUSTOMERS
    // ============================================================
    private void listCustomers() {

        JFrame win = new JFrame("📄 Customer List");
        win.setSize(600, 700);
        win.setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Inter", Font.PLAIN, 16));
        area.setEditable(false);

        for (int i = 0; i < count; i++) {
            area.append("Name: " + customers[i].getName() + "\n");
            area.append("ID: " + customers[i].getId() + "\n");
            area.append("Email: " + customers[i].getEmail() + "\n");
            area.append("Phone: " + customers[i].getPhoneNumber() + "\n");
            area.append("VIP: " + customers[i].isVip() + "\n");
            area.append("Membership: " + memberships[i].getType() + "\n");
            area.append("-------------------------\n");
        }

        win.add(new JScrollPane(area));
        win.setVisible(true);
    }

    // ============================================================
    // SEARCH WINDOW
    // ============================================================
    private void openSearchWindow() {

        JFrame search = new JFrame("🔍 Search Customer");
        search.setSize(450, 300);
        search.setLocationRelativeTo(null);
        search.setLayout(new BorderLayout(10,10));
        search.getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("🔍 Search Customer", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setOpaque(true);
        title.setBackground(new Color(20, 20, 60));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel center = new JPanel(new GridLayout(2,1,10,10));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        JTextField txtSearch = new JTextField();
        txtSearch.setBorder(BorderFactory.createTitledBorder("Enter ID"));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSearch.setBackground(new Color(25, 25, 25));
        btnSearch.setForeground(Color.WHITE);

        center.add(txtSearch);
        center.add(btnSearch);

        search.add(title, BorderLayout.NORTH);
        search.add(center, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String id = txtSearch.getText();

            for (int i = 0; i < count; i++) {
                if (customers[i].getId().equals(id)) {

                    JFrame result = new JFrame("Customer Info");
                    result.setSize(400, 350);
                    result.setLocationRelativeTo(null);

                    JTextArea area = new JTextArea();
                    area.setFont(new Font("Inter", Font.PLAIN, 16));
                    area.setEditable(false);
                    area.setText(
                            "Name: " + customers[i].getName() +
                            "\nEmail: " + customers[i].getEmail() +
                            "\nPhone: " + customers[i].getPhoneNumber() +
                            "\nVIP: " + customers[i].isVip() +
                            "\nMembership: " + memberships[i].getType()
                    );

                    result.add(new JScrollPane(area));
                    result.setVisible(true);
                    return;
                }
            }

            JOptionPane.showMessageDialog(search, "Customer not found.");
        });

        search.setVisible(true);
    }

    // ============================================================
    // SAVE WINDOW
    // ============================================================
    private void openSaveWindow() {

        JFrame win = new JFrame("💾 Save Customers");
        win.setSize(400, 200);
        win.setLocationRelativeTo(null);
        win.setLayout(new GridLayout(2,1,10,10));

        JLabel lbl = new JLabel("Save list to customers.txt?", SwingConstants.CENTER);
        lbl.setFont(new Font("Inter", Font.BOLD, 18));

        JButton btn = new JButton("Save");
        btn.setFont(new Font("Inter", Font.BOLD, 18));

        btn.addActionListener(e -> {
            saveToTxt();
            win.dispose();
        });

        win.add(lbl);
        win.add(btn);
        win.setVisible(true);
    }

    private void saveToTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("customers.txt"))) {

            for (int i = 0; i < count; i++) {
                pw.println(customers[i].getName());
                pw.println(customers[i].getId());
                pw.println(customers[i].getEmail());
                pw.println(customers[i].getPhoneNumber());
                pw.println(customers[i].isVip());
                pw.println(memberships[i].getType());
            }

            JOptionPane.showMessageDialog(null, "Saved to customers.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // LOAD WINDOW
    // ============================================================
    private void openLoadWindow() {

        JFrame win = new JFrame("📁 Load Customers");
        win.setSize(400, 200);
        win.setLocationRelativeTo(null);
        win.setLayout(new GridLayout(2,1,10,10));

        JLabel lbl = new JLabel("Load customers.txt?", SwingConstants.CENTER);
        lbl.setFont(new Font("Inter", Font.BOLD, 18));

        JButton btn = new JButton("Load");
        btn.setFont(new Font("Inter", Font.BOLD, 18));

        btn.addActionListener(e -> {
            loadFromTxt();
            win.dispose();
        });

        win.add(lbl);
        win.add(btn);
        win.setVisible(true);
    }

    private void loadFromTxt() {
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {

            count = 0;
            String line;

            while ((line = br.readLine()) != null) {

                Customer c = new Customer();
                Membership m = new Membership();

                c.setName(line);
                c.setId(br.readLine());
                c.setEmail(br.readLine());
                c.setPhoneNumber(br.readLine());
                c.setVip(Boolean.parseBoolean(br.readLine()));

                String membershipType = br.readLine();
                if (!membershipType.equals("null")) {
                    m.setType(TypeMembership.valueOf(membershipType));
                } else {
                    m.setType(null);
                }

                customers[count] = c;
                memberships[count] = m;
                count++;
            }

            JOptionPane.showMessageDialog(null, "List loaded.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading file.");
        }
    }
}
