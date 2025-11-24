/*package ui;


import proyecto1.cinema.Product;
import javax.swing.*;
import java.awt.*;


public class RegisterProductUI extends JFrame {


public RegisterProductUI() {
setTitle("Register Product");
setSize(400, 500);
setLocationRelativeTo(null);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);


JTextField txtName = new JTextField();
JTextField txtCategory = new JTextField();
JTextField txtDescription = new JTextField();
JTextField txtCode = new JTextField();
JTextField txtStock = new JTextField();
JTextField txtPrice = new JTextField();
JTextField txtStatus = new JTextField();


JButton btnSave = new JButton("Save");
btnSave.addActionListener(e -> {
try {
Product p = new Product(
Double.parseDouble(txtPrice.getText()),
Integer.parseInt(txtStock.getText()),
txtCategory.getText(),
txtCode.getText(),
txtDescription.getText(),
txtName.getText(),
txtStatus.getText()
);


ProductStorage.addProduct(p);
JOptionPane.showMessageDialog(this, "Product saved");
dispose();
} catch (Exception ex) {
JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
}
});


setLayout(new GridLayout(8, 2, 5, 5));
add(new JLabel("Product Name:")); add(txtName);
add(new JLabel("Category:")); add(txtCategory);
add(new JLabel("Description:")); add(txtDescription);
add(new JLabel("Code:")); add(txtCode);
add(new JLabel("Stock:")); add(txtStock);
add(new JLabel("Price:")); add(txtPrice);
add(new JLabel("Status:")); add(txtStatus);
add(btnSave);
}
}
*/