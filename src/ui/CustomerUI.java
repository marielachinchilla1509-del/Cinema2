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
        JPanel buttonPanel
                = new JPanel(new GridLayout(3, 2, 30, 30));
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

    private JButton createBigButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    // ============================================================
    // REGISTER CUSTOMER
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

        JPanel membershipPanel = new JPanel(new GridLayout(1, 1));
        membershipPanel.setBackground(Color.WHITE);

        String[] membershipOptions = {"BASIC", "PRO", "PREMIUM"};
        JComboBox<String> cbMembership = new JComboBox<>(membershipOptions);
        cbMembership.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        membershipPanel.add(cbMembership);
        membershipPanel.setVisible(false);

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

        JButton btnSave = new JButton("✅ Register Customer");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSave.setBackground(red);
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {

            // VALIDACION 1: Campos vacíos
            if (txtName.getText().isEmpty()
                    || txtId.getText().isEmpty()
                    || txtEmail.getText().isEmpty()
                    || txtPhone.getText().isEmpty()) {

                JOptionPane.showMessageDialog(reg,
                        "❌ All fields must be filled",
                        "Empty Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDACION 2: ID repetido
            for (int i = 0; i < count; i++) {
                if (customers[i].getId().equals(txtId.getText())) {
                    JOptionPane.showMessageDialog(reg,
                            "❌ ID already exists",
                            "Duplicate ID",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Customer c = new Customer();
            Membership m = new Membership();

            c.setName(txtName.getText());
            c.setId(txtId.getText());
            c.setEmail(txtEmail.getText());
            c.setPhoneNumber(txtPhone.getText());
            c.setVip(chkVip.isSelected());

            if (chkVip.isSelected()) {
                m.setType(TypeMembership.valueOf(cbMembership.getSelectedItem().toString()));
            } else {
                m.setType(null);
            }

            customers[count] = c;
            memberships[count] = m;
            count++;

            JOptionPane.showMessageDialog(reg, "✅ Customer Registered Successfully!");
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
    // LIST CUSTOMERS (AHORA CON BOTÓN DE ELIMINAR POR CADA FILA)
    // ============================================================
    private void listCustomers() {

        JFrame win = new JFrame("📄 Customer List");
        win.setSize(800, 500);
        win.setLocationRelativeTo(null);
        win.setLayout(new BorderLayout(10, 10));

        // ======== TÍTULO =========
        JLabel title = new JLabel("📄 Customer List", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(10, 25, 60));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        win.add(title, BorderLayout.NORTH);

        // ======== TABLA CON BOTÓN ELIMINAR =========
        String[] columnas = {"Name", "ID", "Email", "Phone", "VIP", "Membership", "Delete"};

        String[][] datos = new String[count][7];

        for (int i = 0; i < count; i++) {
            datos[i][0] = customers[i].getName();
            datos[i][1] = customers[i].getId();
            datos[i][2] = customers[i].getEmail();
            datos[i][3] = customers[i].getPhoneNumber();
            datos[i][4] = String.valueOf(customers[i].isVip());
            datos[i][5] = String.valueOf(memberships[i].getType());
            datos[i][6] = "🗑️️";
        }

        JTable table = new JTable(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;  // solo eliminar
            }
        };

        table.setFont(new Font("Inter", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(230, 230, 230));

        // Acción del basurero
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 6) {
                    deleteCustomer(row);
                    win.dispose();
                    listCustomers();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        win.add(scroll, BorderLayout.CENTER);

        // ======== BOTÓN SALIR =========
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Inter", Font.BOLD, 18));
        btnClose.addActionListener(e -> win.dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnClose);
        win.add(bottom, BorderLayout.SOUTH);

        win.setVisible(true);
    }

// ================================================
// CONFIRMACIÓN DE ELIMINAR (CON JFRAME)
// ================================================
    private void deleteCustomer(int index) {

        JFrame confirm = new JFrame("🗑️ Confirm Delete");
        confirm.setAlwaysOnTop(true); // NO SE ENVÍA AL FONDO
        confirm.setLayout(new BorderLayout(10, 10));
        confirm.getContentPane().setBackground(Color.WHITE);

        // ===== TITLE =====
        JLabel lbl = new JLabel("Delete this customer?", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(Color.BLACK);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        confirm.add(lbl, BorderLayout.NORTH);

        // ===== CUSTOMER INFO =====
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setBackground(Color.WHITE);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        info.setText(
                "Name: " + customers[index].getName() + "\n"
                + "ID: " + customers[index].getId() + "\n"
                + "Email: " + customers[index].getEmail() + "\n"
                + "Phone: " + customers[index].getPhoneNumber() + "\n"
                + "VIP: " + customers[index].isVip() + "\n"
                + "Membership: " + memberships[index].getType() + "\n"
        );

        JScrollPane scroll = new JScrollPane(info);
        confirm.add(scroll, BorderLayout.CENTER);

        // ===== BUTTONS PANEL =====
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setBackground(Color.WHITE);

        // ==== RED TRASH BUTTON ====  
        JButton btnDelete = new JButton("🗑️ Delete");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setBackground(new Color(200, 30, 30)); // rojo oscuro
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        // ==== CANCEL BUTTON ====
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCancel.setBackground(Color.LIGHT_GRAY);
        btnCancel.setForeground(Color.BLACK);

        buttons.add(btnDelete);
        buttons.add(btnCancel);

        confirm.add(buttons, BorderLayout.SOUTH);

        // ====== ACTIONS ======
        btnCancel.addActionListener(e -> confirm.dispose());

        btnDelete.addActionListener(e -> {

            // Mover elementos
            for (int i = index; i < count - 1; i++) {
                customers[i] = customers[i + 1];
                memberships[i] = memberships[i + 1];
            }

            customers[count - 1] = null;
            memberships[count - 1] = null;
            count--;

            // Actualizar el archivo
            updateTxtAfterDelete();

            confirm.dispose();

            JOptionPane.showMessageDialog(null,
                    "Customer deleted successfully!",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        confirm.pack();         // Se ajusta al contenido
        confirm.setSize(450, 350); // Tamaño agradable
        confirm.setLocationRelativeTo(null);
        confirm.setVisible(true);
    }

// ============================================================
// WRITE UPDATED LIST TO TXT (AFTER DELETE)
// ============================================================
    private void updateTxtAfterDelete() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("customers.txt", false))) {

            for (int i = 0; i < count; i++) {
                pw.println(customers[i].getName());
                pw.println(customers[i].getId());
                pw.println(customers[i].getEmail());
                pw.println(customers[i].getPhoneNumber());
                pw.println(customers[i].isVip());
                pw.println(memberships[i].getType());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error updating file after delete.");
        }
    }

    // ============================================================
    // SAVE (integración inteligente) - lo dejamos tal como estaba
    // ============================================================
    private void openSaveWindow() {

        JFrame win = new JFrame("💾 Save Customers");
        win.setSize(400, 200);
        win.setLocationRelativeTo(null);
        win.setLayout(new GridLayout(2, 1, 10, 10));

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

            pw.println("Name: " + customers[i].getName());
            pw.println("ID: " + customers[i].getId());
            pw.println("Email: " + customers[i].getEmail());
            pw.println("Phone: " + customers[i].getPhoneNumber());
            pw.println("VIP: " + customers[i].isVip());
            pw.println("Membership: " + memberships[i].getType());
            pw.println("------------------------------");
            pw.println(); // Línea vacía para separar clientes
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    // ============================================================
    // LOAD CUSTOMERS
    // ============================================================
    private void openLoadWindow() {

        JFrame win = new JFrame("📁 Load Customers");
        win.setSize(400, 200);
        win.setLocationRelativeTo(null);
        win.setLayout(new GridLayout(2, 1, 10, 10));

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
                if (membershipType != null && !membershipType.equals("null")) {
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

    // ============================================================
    // Helper: reescribir archivo EXACTAMENTE con lo que hay en memoria
    // ============================================================
    private void writeAllToFile() {
        File file = new File("customers.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (int i = 0; i < count; i++) {
                String membershipStr = (memberships[i] != null && memberships[i].getType() != null)
                        ? memberships[i].getType().toString() : "null";

                pw.println(customers[i].getName());
                pw.println(customers[i].getId());
                pw.println(customers[i].getEmail());
                pw.println(customers[i].getPhoneNumber());
                pw.println(String.valueOf(customers[i].isVip()));
                pw.println(membershipStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error writing file.");
        }
    }

    private void openSearchWindow() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}