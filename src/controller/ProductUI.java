package controller;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductUI extends JFrame {

    private JTextField nameField, priceField, quantityField, gstField, supplierField;
    private JTable table;
    private DefaultTableModel tableModel;

    public ProductUI() {

        setTitle("Product Management");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        inputPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("GST %:"));
        gstField = new JTextField();
        inputPanel.add(gstField);

        inputPanel.add(new JLabel("Supplier ID:"));
        supplierField = new JTextField();
        inputPanel.add(supplierField);

        JButton addButton = new JButton("Add Product");
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Price", "Qty"});
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> addProduct());

        loadProducts();
    }

    private void addProduct() {

        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(quantityField.getText());
            double gst = Double.parseDouble(gstField.getText());
            int supplierId = Integer.parseInt(supplierField.getText());

            Product product = new Product(name, price, qty, gst, supplierId);
            ProductDAO dao = new ProductDAO();
            dao.addProduct(product);

            JOptionPane.showMessageDialog(this, "Product Added!");

            loadProducts();

            nameField.setText("");
            priceField.setText("");
            quantityField.setText("");
            gstField.setText("");
            supplierField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    private void loadProducts() {

        tableModel.setRowCount(0);

        try {
            ProductDAO dao = new ProductDAO();
            var con = util.DBConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM products");
            var rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
