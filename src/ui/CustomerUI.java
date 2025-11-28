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

/**
 * Graphical Interface Module for Customer Management.
 * It allows registering, listing, searching, and deleting customers.
 * Includes specific validation rules for ID type, phone number (8 digits),
 * and email domain (UCR).
 */
public class CustomerUI extends JFrame {

    private Customer[] customers = new Customer[200];
    private Membership[] memberships = new Membership[200];
    private int count = 0; // Tracks the current number of registered customers

    // Variables for dynamic list update
    private JTable customerTable;
    private JFrame listWindow;

    /**
     * Main constructor for the Customer module.
     * Sets up the main visual interface, loads saved customer data,
     * and links the primary buttons to their actions.
     */
    public CustomerUI() {

        // --- UI Setup ---
        setSize(1000, 650);
        setTitle("👤 Customer Module");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes this window only
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- Color Palette ---
        Color red = new Color(139, 0, 0); // Dark red for 'Back'
        Color navyBlue = new Color(100, 185, 230); // Light blue for main buttons
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // --- Module Title ---
        JLabel title = new JLabel("👤 Customer Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // --- Main Button Panel (Grid 2x2) ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnRegister = createBigButton("👤 Register Customer", navyBlue, white);
        JButton btnList = createBigButton("📄 Customer List", navyBlue, white);
        JButton btnSearch = createBigButton("🔍 Search Customer", navyBlue, white);
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnList);
        buttonPanel.add(btnSearch);
        add(buttonPanel, BorderLayout.CENTER);

        // --- Back Button to Main Menu ---
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅ Back to Menu");
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBackground(red);
        btnBack.setForeground(white);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose()); // Closes the current module window

        backPanel.add(btnBack);
        add(backPanel, BorderLayout.SOUTH);

        // --- Event Configuration ---
        btnRegister.addActionListener(e -> openRegisterWindow());
        btnList.addActionListener(e -> listCustomers());
        btnSearch.addActionListener(e -> openSearchWindow());
        
        loadDataOnStartup(); // Loads persistent data when the module starts
    }

    /**
     * Creates a large, styled button for the main module menu.
     * @param text The text to display on the button.
     * @param bg The background color.
     * @param fg The foreground (text) color.
     * @return The configured JButton object.
     */
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
    /**
     * Opens the registration window for a new customer.
     * Includes logic for ID type (9 or 10 digits), phone validation (8 digits),
     * and UCR email domain check.
     */
    private void openRegisterWindow() {

        JFrame reg = new JFrame("👤 Register Customer");
        reg.setSize(500, 750); 
        reg.setLocationRelativeTo(null);
        reg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reg.setLayout(new BorderLayout(10, 10));
        reg.getContentPane().setBackground(Color.WHITE);

        Color navy = new Color(0x001F3F);
        Color red = new Color(0xB22222);

        // --- Header Setup ---
        JLabel header = new JLabel("👤 Register Customer", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setOpaque(true);
        header.setBackground(navy);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        reg.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(10, 1, 10, 10)); 
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        form.setBackground(Color.WHITE);

        // --- ID Type Selection ---
        JRadioButton rbNacional = new JRadioButton("National (9 Digits)");
        JRadioButton rbExtranjero = new JRadioButton("Foreigner (10 Digits)");
        ButtonGroup group = new ButtonGroup();
        group.add(rbNacional);
        group.add(rbExtranjero);
        rbNacional.setSelected(true); 

        JPanel idTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idTypePanel.setBackground(Color.WHITE);
        idTypePanel.add(rbNacional);
        idTypePanel.add(rbExtranjero);

        rbNacional.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbExtranjero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbNacional.setBackground(Color.WHITE);
        rbExtranjero.setBackground(Color.WHITE);

        // --- Input Fields ---
        JTextField txtId = createField("ID (Cédula/Passport)");

        // Filter to ensure ID is numeric and respects the 9 or 10 digit limit
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

                // 1. Only allow digits
                if (text.matches("\\d+")) {
                    // 2. Limit the total length
                    if (newText.length() <= maxLen) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        // Truncate the input if it exceeds the limit
                        int over = newText.length() - maxLen;
                        super.replace(fb, offset, length, text.substring(0, text.length() - over), attrs);
                    }
                }
            }
        };

        // Apply ID filter and add listener to handle length change
        ((AbstractDocument) txtId.getDocument()).setDocumentFilter(numericFilter);
        rbNacional.addActionListener(e -> {
            ((AbstractDocument) txtId.getDocument()).setDocumentFilter(numericFilter);
            if (txtId.getText().length() > 9) {
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

        JTextField txtPhone = createField("Phone (8 Digits)");

        // Filter to ensure phone number is numeric and exactly 8 digits
        ((AbstractDocument) txtPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                // Only allow digits and limit to 8 characters
                if (text.matches("\\d*") && newText.length() <= 8) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });


        // --- VIP Membership Options ---
        JCheckBox chkVip = new JCheckBox("VIP Customer");
        chkVip.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        chkVip.setBackground(Color.WHITE);

        JPanel membershipPanel = new JPanel(new GridLayout(1, 1));
        membershipPanel.setBackground(Color.WHITE);

        String[] membershipOptions = {"BASIC", "PRO", "PREMIUM"};
        JComboBox<String> cbMembership = new JComboBox<>(membershipOptions);
        cbMembership.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        membershipPanel.add(cbMembership);
        membershipPanel.setVisible(false); // Hidden by default

        // Toggles visibility of membership selector
        chkVip.addActionListener(e -> membershipPanel.setVisible(chkVip.
                isSelected()));

        // --- Add components to form ---
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

        // --- Save Logic on Button Click ---
        btnSave.addActionListener(e -> {

            String id = txtId.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String requiredDomain = "@ucr.ac.cr";

            // VALIDATION 1: Check for Empty Fields
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

            // VALIDATION 2: Check ID and Phone Length
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
            if (!email.toLowerCase().endsWith(requiredDomain) || email.length() <= requiredDomain.length()) {
                 JOptionPane.showMessageDialog(reg,
                     "❌ The email must belong to the UCR domain and end with '" + requiredDomain + "'.",
                     "Validation Error", JOptionPane.ERROR_MESSAGE);
                 return; 
            }

            // VALIDATION 4: Check for Duplicate ID
            for (int i = 0; i < count; i++) {
                if (customers[i] != null && customers[i].getId().equals(id)) {
                    JOptionPane.showMessageDialog(reg,
                            "❌ ID already exists",
                            "Duplicate ID",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // --- Object Creation and Data Storage ---
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
                m.setType(null); // No membership if not VIP
            }

            customers[count] = c;
            memberships[count] = m;
            count++;

            saveToTxt(); // Automatic saving after successful registration

            JOptionPane.showMessageDialog(reg, "✅ Customer Registered Successfully!");
            reg.dispose();
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnSave);
        reg.add(bottom, BorderLayout.SOUTH);

        reg.setVisible(true);
    }

    /**
     * Creates a standard text field with a titled border acting as a clear placeholder.
     * @param placeholder The title/placeholder text.
     * @return The configured JTextField.
     */
    private JTextField createField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tf.setBorder(BorderFactory.createTitledBorder(placeholder));
        return tf;
    }

    // ============================================================
    // LIST CUSTOMERS
    // ============================================================
    /**
     * Opens the window showing the list of registered customers in a JTable.
     * It includes a listener to trigger customer deletion directly from the table.
     */
    private void listCustomers() {

        // 1. Initialize the window if it's the first time
        if (listWindow == null) {
            listWindow = new JFrame("📄 Customer List");
            listWindow.setSize(800, 500);
            listWindow.setLocationRelativeTo(null);
            listWindow.setLayout(new BorderLayout(10, 10));

            // --- Header Setup ---
            JLabel title = new JLabel("📄 Customer List", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setOpaque(true);
            title.setBackground(new Color(10, 25, 60));
            title.setForeground(Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            listWindow.add(title, BorderLayout.NORTH);

            customerTable = new JTable();

            customerTable.setFont(new Font("Inter", Font.PLAIN, 15));
            customerTable.setRowHeight(35);

            // Listener to handle clicks on the 'Delete' column (column 6)
            customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int row = customerTable.rowAtPoint(e.getPoint());
                    int col = customerTable.columnAtPoint(e.getPoint());

                    if (col == 6) {
                        deleteCustomer(row); // Initiates deletion and update
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

        // 2. Load or update the latest data
        updateCustomerTable();

        // 3. Display the window
        listWindow.setVisible(true);
    }

    /**
     * Loads and refreshes customer data into the JTable from the internal arrays.
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
            data[i][6] = "🗑"; // Delete icon for the column
        }

        // Creates a non-editable model, except for the 'Delete' column
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only the 'Delete' column allows interaction
            }
        };

        customerTable.setModel(model);

        // Styling for the table header
        customerTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        customerTable.getTableHeader().setBackground(new Color(230, 230, 230));
    }

    // ============================================================
    // DELETE CUSTOMER
    // ============================================================
    /**
     * Displays a confirmation window and performs the permanent deletion
     * of a customer from the array and the text file.
     * @param index The array index (table row) of the customer to delete.
     */
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

        // Shows the details of the customer to be deleted
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
            // Array deletion logic: shifts elements to overwrite the deleted one
            for (int i = index; i < count - 1; i++) {
                customers[i] = customers[i + 1];
                memberships[i] = memberships[i + 1];
            }

            // Clears the last position and decrements the counter
            customers[count - 1] = null;
            memberships[count - 1] = null;
            count--;

            updateTxtAfterDelete(); // Saves the changes to the TXT file

            // Updates the list window if it is currently open
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
    // DATA PERSISTENCE (SAVE / LOAD)
    // ============================================================
    
    /**
     * Saves the current list of customers to the "customers.txt" file,
     * overwriting previous content. Used for registration and deletion.
     */
    private void saveToTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("customers.txt",
                false))) { // 'false' ensures overwriting
            for (int i = 0; i < count; i++) {
                pw.println("ID: " + customers[i].getId());
                pw.println("Name: " + customers[i].getName());
                pw.println("Email: " + customers[i].getEmail());
                pw.println("Phone: " + customers[i].getPhoneNumber());
                pw.println("VIP: " + customers[i].isVip());
                pw.println("Membership: " + (memberships[i].getType() != null
                        ? memberships[i].getType() : "None"));
                pw.println("------------------------------"); // Separator line
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving to file.");
        }
    }

    /**
     * Helper function to re-save the data file immediately after a customer is deleted.
     */
    private void updateTxtAfterDelete() {
        saveToTxt();
    }

    /**
     * Loads existing customer data from "customers.txt" when the module starts.
     * This ensures data persistence across application sessions.
     */
    private void loadDataOnStartup() {
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {

            // Reset arrays and counter before loading
            customers = new Customer[200];
            memberships = new Membership[200];
            count = 0;

            String line;
            Customer c = null;
            Membership m = null;

            // Reads line by line and parses data fields
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
                            m.setType(null); // Ignore unrecognized values
                        }
                    } else {
                        m.setType(null);
                    }
                } else if (line.startsWith("----------------")) {
                    // Separator found: save the complete object to the array
                    if (c != null && m != null) {
                        customers[count] = c;
                        memberships[count] = m;
                        count++;
                    }
                    c = null;
                    m = null;
                }
            }

            // Handles the last record if the file ends without a separator
            if (c != null && m != null) {
                customers[count] = c;
                memberships[count] = m;
                count++;
            }

        } catch (FileNotFoundException e) {
            // File doesn't exist: start with an empty list (silent on startup)
        } catch (Exception e) {
            // General error handling during loading
            JOptionPane.showMessageDialog(null, "Error loading customers on startup.");
        }
    }

    // ============================================================
    // SEARCH CUSTOMER
    // ============================================================
    /**
     * Opens a window to search for a customer by their ID.
     * Displays all customer details if found, or an error message otherwise.
     */
    private void openSearchWindow() {

        JFrame win = new JFrame("🔍 Search Customer");
        win.setSize(600, 400);
        win.setLocationRelativeTo(null);
        win.setLayout(new BorderLayout(10, 10));
        win.getContentPane().setBackground(Color.WHITE);

        // --- Header Setup ---
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

        // --- Search Logic on Button Click ---
        btnSearch.addActionListener(e -> {
            String id = txtIdSearch.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(win, "Please enter an ID.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean found = false;
            // Iterates through the customer array to find the matching ID
            for (int i = 0; i < count; i++) {
                if (customers[i].getId().equals(id)) {
                    // Displays customer details if found
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