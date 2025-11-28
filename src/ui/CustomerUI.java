package ui;

import proyecto1.cinema.Customer;
import proyecto1.cinema.Membership;
import proyecto1.cinema.TypeMembership;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.*;

public class CustomerUI extends JFrame {

    private Customer[] customers = new Customer[200];
    private Membership[] memberships = new Membership[200];
    private int count = 0;

    // Variables for dynamic list update
    private JTable customerTable;
    private JFrame listWindow;

    public CustomerUI() {

        setSize(1000, 650);
        setTitle("👤 Customer Module");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==== COLORS ====
        Color red = new Color(139, 0, 0);
        Color navyBlue = new Color(10, 25, 60);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // ==== TITLE ====
        JLabel title = new JLabel("👤 Customer Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== BUTTON PANEL (ADJUSTED TO 2x2: btnSave removed) ====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnRegister = createBigButton("👤 Register Customer", navyBlue, white);
        JButton btnList = createBigButton("📄 Customer List", navyBlue, white);
        JButton btnSearch = createBigButton("🔍 Search Customer", navyBlue, white);
        // btnSave has been removed

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnList);
        buttonPanel.add(btnSearch);
        // The fourth space is left empty or the layout is adjusted
        add(buttonPanel, BorderLayout.CENTER);

        // ==== BACK BUTTON ====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅ Back to Menu");
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBackground(red);
        btnBack.setForeground(white);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());

        backPanel.add(btnBack);
        add(backPanel, BorderLayout.SOUTH);

        // ==== EVENTS ====
        btnRegister.addActionListener(e -> openRegisterWindow());
        btnList.addActionListener(e -> listCustomers());
        btnSearch.addActionListener(e -> openSearchWindow());
        // The save event was removed
        loadDataOnStartup();
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
        reg.setSize(500, 750); // Increased size to fit new fields
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

        JPanel form = new JPanel(new GridLayout(10, 1, 10, 10)); // More rows
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        form.setBackground(Color.WHITE);

        // --- Radio Buttons for ID Type ---
        JRadioButton rbNacional = new JRadioButton("National (9 Digits)");
        JRadioButton rbExtranjero = new JRadioButton("Foreigner (10 Digits)");
        ButtonGroup group = new ButtonGroup();
        group.add(rbNacional);
        group.add(rbExtranjero);
        rbNacional.setSelected(true); // National by default

        JPanel idTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idTypePanel.setBackground(Color.WHITE);
        idTypePanel.add(rbNacional);
        idTypePanel.add(rbExtranjero);

        rbNacional.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbExtranjero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbNacional.setBackground(Color.WHITE);
        rbExtranjero.setBackground(Color.WHITE);

        // --- ID Field ---
        JTextField txtId = createField("ID (Cédula/Passport)");

        // MODIFICATION: Numeric-only restriction and 9/10 digit length
        DocumentFilter numericFilter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;

                int maxLen = rbNacional.isSelected() ? 9 : 10;
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                // 1. Accept only digits
                if (text.matches("\\d+")) {
                    // 2. Limit length
                    if (newText.length() <= maxLen) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        // Insert only the part that fits
                        int over = newText.length() - maxLen;
                        super.replace(fb, offset, length, text.substring(0, text.length() - over), attrs);
                    }
                }
            }
        };

        // Apply filter to ID field
        ((AbstractDocument) txtId.getDocument()).setDocumentFilter(numericFilter);

        // Listener to recalculate max length when changing radio button
        rbNacional.addActionListener(e -> {
            // Force the filter to apply the maximum length (this truncates if exceeded)
            ((AbstractDocument) txtId.getDocument()).setDocumentFilter(numericFilter);
            if (txtId.getText().length() > 9) {
                // Notify the user if the current ID is too long for National
                JOptionPane.showMessageDialog(reg,
                     "The current ID (10 digits) exceeds the limit for 'National' (9 digits). It will be truncated if you attempt to save.",
                     "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        rbExtranjero.addActionListener(e -> {
            ((AbstractDocument) txtId.getDocument()).setDocumentFilter(numericFilter);
        });


        JTextField txtName = createField("Name");
        JTextField txtEmail = createField("Email");

        // --- Phone Field ---
        JTextField txtPhone = createField("Phone (8 Digits)");

        // MODIFICATION: 8-digit phone number restriction (numeric only)
        ((AbstractDocument) txtPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                // Accept only digits and limit to 8 characters
                if (text.matches("\\d*") && newText.length() <= 8) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });


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

        chkVip.addActionListener(e -> membershipPanel.setVisible(chkVip.
                isSelected()));

        // Add the new panels to the form
        form.add(idTypePanel);
        form.add(txtId);
        form.add(txtName);
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

            String id = txtId.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String requiredDomain = "@ucr.ac.cr";

            // VALIDATION 1: Empty Fields
            if (id.isEmpty()
                    || txtName.getText().isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()) {

                JOptionPane.showMessageDialog(reg,
                        "❌ All fields must be filled",
                        "Empty Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDATION 2: ID and Phone correct length
            int expectedIdLength = rbNacional.isSelected() ? 9 : 10;
            if (id.length() != expectedIdLength) {
                JOptionPane.showMessageDialog(reg,
                        "❌ ID must have exactly " + expectedIdLength + " digits.",
                        "Invalid ID Length",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (phone.length() != 8) {
                JOptionPane.showMessageDialog(reg,
                        "❌ Phone number must have exactly 8 digits.",
                        "Invalid Phone Length",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            // VALIDATION 3: UCR Email Domain Check
            // Check: Email MUST end with "@ucr.ac.cr" and MUST have characters before it.
            if (!email.toLowerCase().endsWith(requiredDomain) || email.length() <= requiredDomain.length()) {
                 JOptionPane.showMessageDialog(reg,
                     "❌ The email must belong to the UCR domain and end with '" + requiredDomain + "'.",
                     "Validation Error", JOptionPane.ERROR_MESSAGE);
                 return; // Stops the saving process if validation fails
            }

            // VALIDATION 4: Duplicate ID
            for (int i = 0; i < count; i++) {
                if (customers[i] != null && customers[i].getId().equals(id)) {
                    JOptionPane.showMessageDialog(reg,
                            "❌ ID already exists",
                            "Duplicate ID",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Customer c = new Customer();
            Membership m = new Membership();

            c.setId(id);
            c.setName(txtName.getText());
            c.setEmail(email);
            c.setPhoneNumber(phone);
            c.setVip(chkVip.isSelected());

            if (chkVip.isSelected()) {
                m.setType(TypeMembership.valueOf(cbMembership.getSelectedItem().
                        toString()));
            } else {
                m.setType(null);
            }

            customers[count] = c;
            memberships[count] = m;
            count++;

            // Automatic saving after registration
            saveToTxt();

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
    // LIST CUSTOMERS (MODIFIED FOR DYNAMIC UPDATE)
    // ============================================================
    private void listCustomers() {

        // 1. Create or show the list window
        if (listWindow == null) {
            listWindow = new JFrame("📄 Customer List");
            listWindow.setSize(800, 500);
            listWindow.setLocationRelativeTo(null);
            listWindow.setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("📄 Customer List", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setOpaque(true);
            title.setBackground(new Color(10, 25, 60));
            title.setForeground(Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            listWindow.add(title, BorderLayout.NORTH);

            // Initialize the table
            customerTable = new JTable();

            // Configure styles and listener
            customerTable.setFont(new Font("Inter", Font.PLAIN, 15));
            customerTable.setRowHeight(35);

            // Listener for the delete button (column 6)
            customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int row = customerTable.rowAtPoint(e.getPoint());
                    int col = customerTable.columnAtPoint(e.getPoint());

                    if (col == 6) {
                        deleteCustomer(row); // Will delete and automatically update
                    }
                }
            });

            JScrollPane scroll = new JScrollPane(customerTable);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            listWindow.add(scroll, BorderLayout.CENTER);

            JButton btnClose = new JButton("Close");
            btnClose.setFont(new Font("Inter", Font.BOLD, 18));
            btnClose.addActionListener(e -> listWindow.dispose());

            JPanel bottom = new JPanel();
            bottom.add(btnClose);
            listWindow.add(bottom, BorderLayout.SOUTH);
        }

        // 2. Load or update the latest data in the table
        updateCustomerTable();

        // 3. Show the window
        listWindow.setVisible(true);
    }

    /**
     * Method to load and update data in the JTable.
     */
    private void updateCustomerTable() {

        String[] columns = {"ID", "Name", "Email", "Phone", "VIP", "Membership", "Delete"};
        String[][] data = new String[count][7];

        for (int i = 0; i < count; i++) {
            data[i][0] = customers[i].getId();
            data[i][1] = customers[i].getName();
            data[i][2] = customers[i].getEmail();
            data[i][3] = customers[i].getPhoneNumber();
            data[i][4] = String.valueOf(customers[i].isVip());
            data[i][5] = memberships[i].getType() != null
                    ? memberships[i].getType().toString() : "None";
            data[i][6] = "🗑";
        }

        // Create and set the model to force the update
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only the 'Delete' column is "editable"
            }
        };

        customerTable.setModel(model);

        // Ensure the header updates if necessary
        customerTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        customerTable.getTableHeader().setBackground(new Color(230, 230, 230));
    }

    // ============================================================
    // DELETE CUSTOMER
    // ============================================================
    private void deleteCustomer(int index) {

        JFrame confirm = new JFrame("🗑 Confirm Delete");
        confirm.setAlwaysOnTop(true);
        confirm.setLayout(new BorderLayout(10, 10));
        confirm.getContentPane().setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Delete this customer?", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(Color.BLACK);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        confirm.add(lbl, BorderLayout.NORTH);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setBackground(Color.WHITE);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setText(
                "ID: " + customers[index].getId() + "\n"
                        + "Name: " + customers[index].getName() + "\n"
                        + "Email: " + customers[index].getEmail() + "\n"
                        + "Phone: " + customers[index].getPhoneNumber() + "\n"
                        + "VIP: " + customers[index].isVip() + "\n"
                        + "Membership: " + (memberships[index].getType() != null
                        ? memberships[index].getType() : "None")
        );

        JScrollPane scroll = new JScrollPane(info);
        confirm.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setBackground(Color.WHITE);

        JButton btnDelete = new JButton("🗑 Delete");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setBackground(new Color(200, 30, 30));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCancel.setBackground(Color.LIGHT_GRAY);
        btnCancel.setForeground(Color.BLACK);

        buttons.add(btnDelete);
        buttons.add(btnCancel);

        confirm.add(buttons, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> confirm.dispose());

        btnDelete.addActionListener(e -> {
            // Array deletion logic
            for (int i = index; i < count - 1; i++) {
                customers[i] = customers[i + 1];
                memberships[i] = memberships[i + 1];
            }

            customers[count - 1] = null;
            memberships[count - 1] = null;
            count--;

            // Save to TXT after deletion (updates the file)
            updateTxtAfterDelete();

            // MODIFICATION: Update the table if the list window is open
            if (listWindow != null && listWindow.isVisible()) {
                updateCustomerTable();
            }

            confirm.dispose();
            JOptionPane.showMessageDialog(null,
                    "Customer deleted successfully!",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        confirm.pack();
        confirm.setSize(450, 350);
        confirm.setLocationRelativeTo(null);
        confirm.setVisible(true);
    }

    // ============================================================
    // SAVE / HELPERS
    // ============================================================
    // openSaveWindow was removed. saveToTxt is now used automatically.
    private void saveToTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("customers.txt",
                false))) {
            for (int i = 0; i < count; i++) {
                pw.println("ID: " + customers[i].getId());
                pw.println("Name: " + customers[i].getName());
                pw.println("Email: " + customers[i].getEmail());
                pw.println("Phone: " + customers[i].getPhoneNumber());
                pw.println("VIP: " + customers[i].isVip());
                pw.println("Membership: " + (memberships[i].getType() != null
                        ? memberships[i].getType() : "None"));
                pw.println("------------------------------");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving to file.");
        }
    }

    private void updateTxtAfterDelete() {
        // Uses the same code as saveToTxt
        saveToTxt();
    }

    /**
     * Automatic loading used when starting the module.
     */
    private void loadDataOnStartup() {
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {

            customers = new Customer[200];
            memberships = new Membership[200];
            count = 0;

            String line;
            Customer c = null;
            Membership m = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("ID: ")) {
                    c = new Customer();
                    c.setId(line.substring(4).trim());
                } else if (line.startsWith("Name: ")) {
                    if (c != null) {
                        c.setName(line.substring(6).trim());
                    }
                } else if (line.startsWith("Email: ")) {
                    if (c != null) {
                        c.setEmail(line.substring(7).trim());
                    }
                } else if (line.startsWith("Phone: ")) {
                    if (c != null) {
                        c.setPhoneNumber(line.substring(7).trim());
                    }
                } else if (line.startsWith("VIP: ")) {
                    if (c != null) {
                        c.setVip(Boolean.parseBoolean(line.substring(5).trim()));
                    }
                } else if (line.startsWith("Membership: ")) {
                    m = new Membership();
                    String memType = line.substring(12).trim();
                    if (!memType.equals("None")) {
                        try {
                            m.setType(TypeMembership.valueOf(memType));
                        } catch (IllegalArgumentException ex) {
                            m.setType(null); // unrecognized value -> null
                        }
                    } else {
                        m.setType(null);
                    }
                } else if (line.startsWith("----------------")) {
                    if (c != null && m != null) {
                        customers[count] = c;
                        memberships[count] = m;
                        count++;
                    }
                    c = null;
                    m = null;
                }
            }

            // For the last record if there is no separator
            if (c != null && m != null) {
                customers[count] = c;
                memberships[count] = m;
                count++;
            }

        } catch (FileNotFoundException e) {
            // No saved file: do not show error on startup
        } catch (Exception e) {
            // If something fails, do not break the app on startup; optional message:
            JOptionPane.showMessageDialog(null, "Error loading customers on startup.");
        }
    }

    // ============================================================
    // SEARCH CUSTOMER
    // ============================================================
    private void openSearchWindow() {

        JFrame win = new JFrame("🔍 Search Customer");
        win.setSize(600, 400);
        win.setLocationRelativeTo(null);
        win.setLayout(new BorderLayout(10, 10));
        win.getContentPane().setBackground(Color.WHITE);

        JLabel header = new JLabel("🔍 Search Customer by ID",
                SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setOpaque(true);
        header.setBackground(new Color(10, 25, 60));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        win.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField txtIdSearch = new JTextField();
        txtIdSearch.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtIdSearch.setBorder(BorderFactory.createTitledBorder("Enter Customer ID"));
        txtIdSearch.setPreferredSize(new Dimension(200, 50));

        JTextArea txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtResult.setBackground(new Color(245, 245, 245));
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtResult);

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSearch.setBackground(new Color(0x001F3F));
        btnSearch.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(txtIdSearch, BorderLayout.CENTER);
        topPanel.add(btnSearch, BorderLayout.EAST);

        center.add(topPanel, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        win.add(center, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String id = txtIdSearch.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(win, "Please enter an ID.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean found = false;
            for (int i = 0; i < count; i++) {
                if (customers[i].getId().equals(id)) {
                    txtResult.setText(
                            "ID: " + customers[i].getId() + "\n"
                                    + "Name: " + customers[i].getName() + "\n"
                                    + "Email: " + customers[i].getEmail() + "\n"
                                    + "Phone: " + customers[i].getPhoneNumber() + "\n"
                                    + "VIP: " + customers[i].isVip() + "\n"
                                    + "Membership: " + (memberships[i].getType() != null
                                    ? memberships[i].getType() : "None")
                    );
                    found = true;
                    break;
                }
            }

            if (!found) {
                txtResult.setText("❌ Customer not found with ID: " + id);
            }
        });

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.addActionListener(e -> win.dispose());

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnClose);
        win.add(bottom, BorderLayout.SOUTH);

        win.setVisible(true);
    }
}