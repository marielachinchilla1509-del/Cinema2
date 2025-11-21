package ui;

import proyecto1.cinema.Employee;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class EmployeeUI extends JFrame {

    private Employee[] employees = new Employee[200];
    private int count = 0;

    public EmployeeUI() {

        setSize(1000, 650);
        setTitle("👨‍💼 Employee Module");
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
        JLabel title = new JLabel("👨‍💼 Employee Module", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(black);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ==== BUTTON PANEL ====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        buttonPanel.setBackground(white);

        JButton btnRegister = createBigButton("➕ Register Employee", red, white);
        JButton btnList = createBigButton("📄 Employee List", navyBlue, white);
        JButton btnSearch = createBigButton("🔍 Search Employee", darkGray, white);
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
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);

        add(backPanel, BorderLayout.SOUTH);

        // ==== EVENTS ====
        btnRegister.addActionListener(e -> openRegisterWindow());
        btnList.addActionListener(e -> listEmployees());
        btnSearch.addActionListener(e -> openSearchWindow());
        btnSave.addActionListener(e -> saveToTxt());
        btnLoad.addActionListener(e -> loadFromTxt());

    }

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

    // ================= REGISTER UI =================
    private void openRegisterWindow() {

        JFrame reg = new JFrame("➕ Register Employee");
        reg.setSize(500, 700);
        reg.setLocationRelativeTo(null);
        reg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reg.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField txtName = createField("Full Name");
        JTextField txtId = createField("Employee ID");
        JTextField txtEmail = createField("Email");
        JTextField txtPhone = createField("Phone");
        JTextField txtPosition = createField("Position (Manager, Cashier, Security, Cleaning, Admin)");

        panel.add(txtName);
        panel.add(txtId);
        panel.add(txtEmail);
        panel.add(txtPhone);
        panel.add(txtPosition);

        JButton btnSave = new JButton("✅ Save Employee");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {

            String pos = txtPosition.getText().trim().toLowerCase();
            double salary;

            switch (pos) {
                case "manager" -> salary = 2500;
                case "cashier" -> salary = 1300;
                case "security" -> salary = 1200;
                case "cleaning" -> salary = 1000;
                case "admin" -> salary = 2000;
                default -> {
                    JOptionPane.showMessageDialog(reg, "❌ Position not valid!");
                    return;
                }
            }

            Employee emp = new Employee();
            emp.setName(txtName.getText());
            emp.setEmployeeId(txtId.getText());
            emp.setEmail(txtEmail.getText());
            emp.setPhoneNumber(txtPhone.getText());
            emp.setPosition(txtPosition.getText());
            emp.setSalary(salary);

            employees[count++] = emp;

            JOptionPane.showMessageDialog(reg, "Employee Registered Successfully!");
            reg.dispose();
        });

        JPanel bottom = new JPanel();
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

    // ================= LIST =================
    private void listEmployees() {

        JFrame win = new JFrame("📄 Employee List");
        win.setSize(650, 750);
        win.setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Inter", Font.PLAIN, 16));
        area.setEditable(false);

        for (int i = 0; i < count; i++) {
            area.append("Name: " + employees[i].getName() + "\n");
            area.append("ID: " + employees[i].getEmployeeId() + "\n");
            area.append("Email: " + employees[i].getEmail() + "\n");
            area.append("Phone: " + employees[i].getPhoneNumber() + "\n");
            area.append("Position: " + employees[i].getPosition() + "\n");
            area.append("Salary: $" + employees[i].getSalary() + "\n");
            area.append("-------------------------\n");
        }

        win.add(new JScrollPane(area));
        win.setVisible(true);
    }

    // ================= SEARCH WINDOW =================
    private void openSearchWindow() {

        String id = JOptionPane.showInputDialog(this, "Enter Employee ID:");

        if (id == null) return;

        for (int i = 0; i < count; i++) {
            if (employees[i].getEmployeeId().equals(id)) {

                JOptionPane.showMessageDialog(this,
                        "FOUND! 🎯\n\nName: " + employees[i].getName() +
                                "\nPosition: " + employees[i].getPosition() +
                                "\nSalary: $" + employees[i].getSalary()
                );
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Employee Not Found 😢");
    }

    // ================= SAVE =================
    private void saveToTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("employees.txt"))) {

            for (int i = 0; i < count; i++) {
                pw.println(employees[i].getName());
                pw.println(employees[i].getEmployeeId());
                pw.println(employees[i].getEmail());
                pw.println(employees[i].getPhoneNumber());
                pw.println(employees[i].getPosition());
                pw.println(employees[i].getSalary());
            }

            JOptionPane.showMessageDialog(this, "Saved to employees.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD =================
    private void loadFromTxt() {
        try (BufferedReader br = new BufferedReader(new FileReader("employees.txt"))) {

            count = 0;
            String line;

            while ((line = br.readLine()) != null) {

                Employee emp = new Employee();

                emp.setName(line);
                emp.setEmployeeId(br.readLine());
                emp.setEmail(br.readLine());
                emp.setPhoneNumber(br.readLine());
                emp.setPosition(br.readLine());
                emp.setSalary(Double.parseDouble(br.readLine()));

                employees[count++] = emp;
            }

            JOptionPane.showMessageDialog(this, "Employee list loaded!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file");
        }
    }

    public static void main(String[] args) {
        new EmployeeUI().setVisible(true);
    }
}
