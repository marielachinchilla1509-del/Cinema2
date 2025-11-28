package ui;

import proyecto1.cinema.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import java.util.Random;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class EmployeeUI extends JFrame {

    private Vector<Employee> employees = new Vector<>();
    private JTable employeeTable;
    private JFrame listWindow;

    public EmployeeUI() {

        setSize(1300, 750);
        setTitle("👨‍💼 Employee Module");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==== COLORS ====
        Color red = new Color(139, 0, 0);
        Color navyBlue = new Color(10, 25, 60);
        Color black = new Color(0, 0, 0);
        Color white = Color.WHITE;

        // ==== TITLE ====
        JLabel title = new JLabel("👨‍💼 Employee Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ====================================================================
        // ==== BUTTON PANEL (FINAL: 2 arriba, 1 abajo, COMPACTO) ====
        // ====================================================================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(white);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Dimensiones base para TODOS los botones (Tamaño grande y uniforme)
        int buttonWidth = 400;
        int buttonHeight = 220;

        JButton btnRegister = createBigButton("➕ Register Employee", navyBlue, white, buttonWidth, buttonHeight);
        JButton btnList = createBigButton("📄 Employee List", navyBlue, white, buttonWidth, buttonHeight);
        JButton btnSearch = createBigButton("🔍 Search Employee", navyBlue, white, buttonWidth, buttonHeight);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre botones muy compacto (10px)
        gbc.fill = GridBagConstraints.NONE;

        // --- FILA 1 (Botones Superiores: Register y List) ---
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        // 1. Register Button (Arriba a la izquierda)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // CLAVE: Empuja el botón hacia la derecha de su celda
        centerPanel.add(btnRegister, gbc);

        // 2. List Button (Arriba a la derecha)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // CLAVE: Empuja el botón hacia la izquierda de su celda
        centerPanel.add(btnList, gbc);

        // --- FILA 2 (Botón: Search - Debajo de Register, de tamaño normal) ---
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // 3. Search Button (Abajo, a la izquierda)
        gbc.gridx = 0; // Columna 0 (debajo de Register)
        gbc.gridy = 1; // Fila 1
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST; // CLAVE: Centrado dentro de su celda (Columna 0)
        centerPanel.add(btnSearch, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ==== BACK BUTTON PANEL ====
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(white);

        JButton btnBack = new JButton("⬅️ Back to Menu");
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBackground(red);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);

        add(backPanel, BorderLayout.SOUTH);

        // EVENTOS
        btnRegister.addActionListener(e -> openRegisterWindow());
        btnList.addActionListener(e -> listEmployees());
        btnSearch.addActionListener(e -> openSearchWindow());

        loadFromTxt();
        setVisible(true);
    }

    // ================= HELPER METHOD: createBigButton =================
    /**
     * Implementación del createBigButton que acepta dimensiones personalizadas.
     */
    private JButton createBigButton(String text, Color bg, Color fg, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 28));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Dimensiones específicas
        button.setPreferredSize(new Dimension(width, height));
        button.setMinimumSize(new Dimension(width, height));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }

    // El resto del código de la clase (generateEmployeeId, openRegisterWindow, listEmployees, etc.)
    // se mantiene sin cambios.
    // ================= GENERADOR DE ID ALFANUMÉRICO ÚNICO =================
    private String generateEmployeeId() {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int ID_LENGTH = 5;
        String newId;

        do {
            StringBuilder sb = new StringBuilder(ID_LENGTH);
            for (int i = 0; i < ID_LENGTH; i++) {
                char charToAdd;

                // Estructura: LLDLD (Letra, Letra, Dígito, Letra, Dígito)
                if (i == 0 || i == 1 || i == 3) {
                    charToAdd = characters.charAt(random.nextInt(26));
                } else {
                    charToAdd = characters.charAt(26 + random.nextInt(10));
                }
                sb.append(charToAdd);
            }
            newId = sb.toString();
        } while (isEmployeeIdDuplicate(newId));

        return newId;
    }

    private boolean isEmployeeIdDuplicate(String id) {
        for (Employee emp : employees) {
            if (emp.getEmployeeId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    // ================= REGISTER UI =================
    private void openRegisterWindow() {
        // La lógica del formulario de registro se mantiene intacta.
        JFrame reg = new JFrame("➕ Register Employee");
        reg.setSize(500, 800);
        reg.setLocationRelativeTo(null);
        reg.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        reg.setLayout(new BorderLayout(10, 10));
        reg.getContentPane().setBackground(Color.WHITE);

        JLabel header = new JLabel("➕ Register New Employee", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setOpaque(true);
        header.setBackground(new Color(10, 25, 60));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        reg.add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(11, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.WHITE);

        // --- 1. Full Name ---
        JTextField txtName = createField("Full Name");
        ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }
                if (string.matches("[a-zA-Z\\sñÑáéíóúÁÉÍÓÚ]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }
                if (text.matches("[a-zA-Z\\sñÑáéíóúÁÉÍÓÚ]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // --- 2. ID Type ---
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

        // --- 3. Personal ID ---
        JTextField txtIdPersonal = createField("Personal ID");

        DocumentFilter numericIdFilter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }

                int maxLen = rbNacional.isSelected() ? 9 : 10;
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (text.matches("\\d+")) {
                    if (newText.length() <= maxLen) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        int over = newText.length() - maxLen;
                        super.replace(fb, offset, length, text.substring(0, text.length() - over), attrs);
                    }
                }
            }
        };

        ((AbstractDocument) txtIdPersonal.getDocument()).setDocumentFilter(numericIdFilter);
        rbNacional.addActionListener(e -> ((AbstractDocument) txtIdPersonal.getDocument()).setDocumentFilter(numericIdFilter));
        rbExtranjero.addActionListener(e -> ((AbstractDocument) txtIdPersonal.getDocument()).setDocumentFilter(numericIdFilter));

        // --- 4. Email ---
        JTextField txtEmail = createField("Email (must end in @ucr.ac.cr)");

        // --- 5. Phone ---
        JTextField txtPhone = createField("Phone (8 Digits)");
        ((AbstractDocument) txtPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (text.matches("\\d*") && newText.length() <= 8) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // --- 6. Position ---
        String[] positions = {"TicketStaff", "Seller", "Technical", "Administrator"};
        JComboBox<String> cbPosition = new JComboBox<>(positions);
        cbPosition.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbPosition.setBorder(BorderFactory.createTitledBorder("Position"));

        // --- 7. Employee ID (Generated, LAST FIELD) ---
        JTextField txtEmployeeId = createField("Employee ID (5-digit Alphanumeric)");
        txtEmployeeId.setEditable(false);
        txtEmployeeId.setForeground(Color.BLUE);
        txtEmployeeId.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // ===============================================
        // AÑADIR CAMPOS AL PANEL EN EL ORDEN REQUERIDO:
        // ===============================================
        panel.add(idTypePanel);
        panel.add(txtIdPersonal);
        panel.add(txtName);
        panel.add(txtEmail);
        panel.add(txtPhone);
        panel.add(cbPosition);
        panel.add(txtEmployeeId);

        // Generar y asignar el ID.
        String generatedId = generateEmployeeId();
        txtEmployeeId.setText(generatedId);

        JButton btnSave = new JButton("✅ Save Employee");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnSave.setBackground(new Color(139, 0, 0));
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {

            String name = txtName.getText().trim();
            String idPersonal = txtIdPersonal.getText().trim();
            String employeeIdFinal = txtEmployeeId.getText();

            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String pos = (String) cbPosition.getSelectedItem();

            // VALIDATION: Empty fields
            if (name.isEmpty() || idPersonal.isEmpty() || email.isEmpty() || phone.isEmpty() || pos == null || pos.isEmpty()) {
                JOptionPane.showMessageDialog(reg, "❌ All fields must be filled.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDATION: Email Strict Check
            if (!email.toLowerCase().endsWith("@ucr.ac.cr")) {
                JOptionPane.showMessageDialog(reg, "❌ Email must end with @ucr.ac.cr.", "Invalid Email Format", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDATION: ID Personal (9 or 10 digits)
            int expectedIdLength = rbNacional.isSelected() ? 9 : 10;
            if (idPersonal.length() != expectedIdLength) {
                JOptionPane.showMessageDialog(reg,
                        "❌ Personal ID must have exactly " + expectedIdLength + " digits.",
                        "Invalid ID Length",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDATION: Phone number (8 digits)
            if (phone.length() != 8) {
                JOptionPane.showMessageDialog(reg,
                        "❌ Phone number must have exactly 8 digits.",
                        "Invalid Phone Length",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDATION: Duplicate Personal IDs
            for (Employee emp : employees) {
                if (emp.getId().equalsIgnoreCase(idPersonal)) {
                    JOptionPane.showMessageDialog(reg, "❌ Personal ID (Cédula/Passport) already exists.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            double salary;
            String posLower = pos.toLowerCase();

            // Asignación de salarios
            switch (posLower) {
                case "ticketstaff" ->
                    salary = 500;
                case "seller" ->
                    salary = 500;
                case "technical" ->
                    salary = 700;
                case "administrator" ->
                    salary = 1000;
                default -> {
                    JOptionPane.showMessageDialog(reg, "❌ Invalid Position selected.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Employee emp = new Employee();
            emp.setName(name);
            emp.setId(idPersonal);

            emp.setEmployeeId(employeeIdFinal);

            emp.setEmail(email);
            emp.setPhoneNumber(phone);
            emp.setPosition(pos);
            emp.setSalary(salary);

            employees.add(emp);

            saveToTxt();

            JOptionPane.showMessageDialog(reg, "✅ Employee Registered Successfully! ID: " + employeeIdFinal);

            if (listWindow != null && listWindow.isVisible()) {
                updateEmployeeTable();
            }

            reg.dispose();
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnSave);

        reg.add(panel, BorderLayout.CENTER);
        reg.add(bottom, BorderLayout.SOUTH);
        reg.setVisible(true);
    }

    private JTextField createField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tf.setBorder(BorderFactory.createTitledBorder(placeholder));
        return tf;
    }

    // ================= LIST (JTABLE VERSION) =================
    private void listEmployees() {

        if (listWindow == null || !listWindow.isVisible()) {
            listWindow = new JFrame("📄 Employee List");
            listWindow.setSize(1200, 600);
            listWindow.setLocationRelativeTo(null);
            listWindow.setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("📄 Employee List", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setOpaque(true);
            title.setBackground(new Color(10, 25, 60));
            title.setForeground(Color.WHITE);
            title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            listWindow.add(title, BorderLayout.NORTH);

            employeeTable = new JTable();

            employeeTable.setFont(new Font("Inter", Font.PLAIN, 14));
            employeeTable.setRowHeight(30);

            employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int row = employeeTable.rowAtPoint(e.getPoint());
                    int col = employeeTable.columnAtPoint(e.getPoint());

                    if (col == employeeTable.getColumnCount() - 1 && row >= 0) {
                        deleteEmployee(row);
                    }
                }
            });

            JScrollPane scroll = new JScrollPane(employeeTable);
            scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            listWindow.add(scroll, BorderLayout.CENTER);

            JButton btnClose = new JButton("Close");
            btnClose.setFont(new Font("Inter", Font.BOLD, 18));
            btnClose.addActionListener(e -> listWindow.dispose());

            JPanel bottom = new JPanel();
            bottom.setBackground(Color.WHITE);
            bottom.add(btnClose);
            listWindow.add(bottom, BorderLayout.SOUTH);
            listWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }

        updateEmployeeTable();

        listWindow.setVisible(true);
    }

    /**
     * Method to load and update data in the JTable using the Vector.
     */
    private void updateEmployeeTable() {

        String[] columns = {"Emp. ID", "Name", "Personal ID", "Email", "Phone", "Position", "Salary", "🗑 Delete"};
        String[][] data = new String[employees.size()][8];

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            data[i][0] = emp.getEmployeeId();
            data[i][1] = emp.getName();
            data[i][2] = emp.getId();
            data[i][3] = emp.getEmail();
            data[i][4] = emp.getPhoneNumber();
            data[i][5] = emp.getPosition();
            data[i][6] = String.format("$%.2f", emp.getSalary());
            data[i][7] = "Click to Delete";
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        employeeTable.setModel(model);

        employeeTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        employeeTable.getTableHeader().setBackground(new Color(230, 230, 230));

        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(250);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(100);
    }

    // ================= SEARCH WINDOW =================
    private void openSearchWindow() {

        String idToSearch = JOptionPane.showInputDialog(this, "Enter Employee ID (e.g., A1B2C) or Personal ID:");

        if (idToSearch == null || idToSearch.trim().isEmpty()) {
            return;
        }

        for (Employee emp : employees) {
            if (emp.getEmployeeId().equalsIgnoreCase(idToSearch.trim()) || emp.getId().equalsIgnoreCase(idToSearch.trim())) {

                JOptionPane.showMessageDialog(this,
                        "FOUND! 🎯\n\nEmployee ID: " + emp.getEmployeeId()
                        + "\nName: " + emp.getName()
                        + "\nPersonal ID: " + emp.getId()
                        + "\nEmail: " + emp.getEmail()
                        + "\nPhone: " + emp.getPhoneNumber()
                        + "\nPosition: " + emp.getPosition()
                        + "\nSalary: $" + String.format("%.2f", emp.getSalary()),
                        "Employee Found",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Employee Not Found 😢", "Search Result", JOptionPane.ERROR_MESSAGE);
    }

    // ================= DELETE EMPLOYEE (FROM TABLE) =================
    private void deleteEmployee(int index) {

        String nameToDelete = employees.get(index).getName();

        int confirm = JOptionPane.showConfirmDialog(
                listWindow,
                "Do you really want to delete the employee: " + nameToDelete + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            employees.remove(index);

            saveToTxt();

            updateEmployeeTable();

            JOptionPane.showMessageDialog(listWindow, "Employee Deleted Successfully! 🗑", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ================= SAVE TO TXT (AUTOMATIC) =================
    private void saveToTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("employees.txt", false))) {

            pw.println("==========================================");
            pw.println("             Employee List                ");
            pw.println("==========================================\n");

            for (Employee emp : employees) {
                pw.println("EmployeeID: " + emp.getEmployeeId());
                pw.println("Name: " + emp.getName());
                pw.println("PersonalID: " + emp.getId());
                pw.println("Email: " + emp.getEmail());
                pw.println("Phone: " + emp.getPhoneNumber());
                pw.println("Position: " + emp.getPosition());
                pw.println("Salary: " + emp.getSalary());
                pw.println("------------------------------------------\n");
            }
        } catch (Exception e) {
            System.err.println("Error saving employee data to employees.txt: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error saving data to file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= LOAD FROM TXT (AUTOMATIC) =================
    private void loadFromTxt() {
        try (BufferedReader br = new BufferedReader(new FileReader("employees.txt"))) {

            employees.clear();

            String line;
            Employee emp = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("EmployeeID:")) {
                    emp = new Employee();
                    String idStr = line.substring(11).trim();
                    emp.setEmployeeId(idStr);
                } else if (line.startsWith("Name:")) {
                    if (emp != null) {
                        emp.setName(line.substring(6).trim());
                    }
                } else if (line.startsWith("PersonalID:")) {
                    if (emp != null) {
                        emp.setId(line.substring(11).trim());
                    }
                } else if (line.startsWith("Email:")) {
                    if (emp != null) {
                        emp.setEmail(line.substring(6).trim());
                    }
                } else if (line.startsWith("Phone:")) {
                    if (emp != null) {
                        emp.setPhoneNumber(line.substring(6).trim());
                    }
                } else if (line.startsWith("Position:")) {
                    if (emp != null) {
                        emp.setPosition(line.substring(10).trim());
                    }
                } else if (line.startsWith("Salary:")) {
                    if (emp != null) {
                        try {
                            emp.setSalary(Double.parseDouble(line.substring(7).trim()));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping salary due to invalid format: " + line);
                        }
                    }
                } else if (line.equals("------------------------------------------") && emp != null) {
                    employees.add(emp);
                    emp = null;
                }
            }

        } catch (FileNotFoundException e) {
            // Archivo no encontrado, lista vacía.
        } catch (Exception e) {
            System.err.println("Error loading employee data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading employee data from file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
