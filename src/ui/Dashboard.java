/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Proyecto1Cinema - Main Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(2, 2, 20, 20));

        JButton btnCustomer = new JButton("Customer Module");
        JButton btnEmployee = new JButton("Employee Module");
        JButton btnProduct = new JButton("Product Module");
        JButton btnTicket = new JButton("Ticket Sales Module");

        btnCustomer.setBackground(new Color(37, 99, 235));
        btnCustomer.setForeground(Color.WHITE);

        btnEmployee.setBackground(new Color(220, 38, 38));
        btnEmployee.setForeground(Color.WHITE);

        btnProduct.setBackground(new Color(251, 191, 36));
        btnProduct.setForeground(Color.BLACK);

        btnTicket.setBackground(new Color(251, 191, 36));
        btnTicket.setForeground(Color.BLACK);

        panel.add(btnCustomer);
        panel.add(btnEmployee);
        panel.add(btnProduct);
        panel.add(btnTicket);

        add(panel, BorderLayout.CENTER);

        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(new Color(220, 38, 38));
        btnExit.setForeground(Color.WHITE);
        btnExit.addActionListener(e -> System.exit(0));

        add(btnExit, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}


