package controller;

import util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BillingUI extends JFrame {

    private JTextField productIdField, quantityField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    double grandTotal = 0;

    public BillingUI() {

        setTitle("Billing System");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        topPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        topPanel.add(productIdField);

        topPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        topPanel.add(quantityField);

        JButton addButton = new JButton("Add Item");
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "Product Name", "Price", "Qty", "GST", "Total"
        });

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Grand Total: 0.0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton generateButton = new JButton("Generate Bill");

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(generateButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addItem());
        generateButton.addActionListener(e -> generateBill());
    }

    private void addItem() {

        try {
            int productId = Integer.parseInt(productIdField.getText());
            int qty = Integer.parseInt(quantityField.getText());

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT name, price, gst_percentage FROM products WHERE product_id=?"
            );

            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double gstPercent = rs.getDouble("gst_percentage");

                double subtotal = price * qty;
                double gstAmount = (subtotal * gstPercent) / 100;
                double total = subtotal + gstAmount;

                grandTotal += total;

                tableModel.addRow(new Object[]{
                        name,
                        price,
                        qty,
                        gstAmount,
                        total
                });

                totalLabel.setText("Grand Total: " + grandTotal);

            } else {
                JOptionPane.showMessageDialog(this, "Product Not Found!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    private void generateBill() {

        Connection con = null;

        try {

            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Insert sale
            String saleSQL = "INSERT INTO sales (user_id, total_amount) VALUES (?, ?)";
            PreparedStatement saleStmt =
                    con.prepareStatement(saleSQL, PreparedStatement.RETURN_GENERATED_KEYS);

            saleStmt.setInt(1, 1);
            saleStmt.setDouble(2, grandTotal);
            saleStmt.executeUpdate();

            ResultSet generatedKeys = saleStmt.getGeneratedKeys();
            int saleId = 0;

            if (generatedKeys.next()) {
                saleId = generatedKeys.getInt(1);
            }

            // Loop items
            for (int i = 0; i < tableModel.getRowCount(); i++) {

                String productName = tableModel.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                double total = Double.parseDouble(tableModel.getValueAt(i, 4).toString());

                PreparedStatement getIdStmt =
                        con.prepareStatement("SELECT product_id FROM products WHERE name=?");

                getIdStmt.setString(1, productName);
                ResultSet rs = getIdStmt.executeQuery();

                int productId = 0;
                if (rs.next()) {
                    productId = rs.getInt("product_id");
                }

                // Insert sale_items
                PreparedStatement itemStmt =
                        con.prepareStatement("INSERT INTO sale_items (sale_id, product_id, quantity, price) VALUES (?, ?, ?, ?)");

                itemStmt.setInt(1, saleId);
                itemStmt.setInt(2, productId);
                itemStmt.setInt(3, qty);
                itemStmt.setDouble(4, total);
                itemStmt.executeUpdate();

                // Update stock
                PreparedStatement updateStmt =
                        con.prepareStatement("UPDATE products SET quantity = quantity - ? WHERE product_id=?");

                updateStmt.setInt(1, qty);
                updateStmt.setInt(2, productId);
                updateStmt.executeUpdate();
            }

            con.commit();

            // 🔥 Create Bill File
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
            String timeStamp = dtf.format(LocalDateTime.now());

            String fileName = "Bill_" + timeStamp + ".txt";

            FileWriter writer = new FileWriter(fileName);

            writer.write("====================================\n");
            writer.write("          ERP GROCERY BILL          \n");
            writer.write("====================================\n");
            writer.write("Date: " + LocalDateTime.now() + "\n\n");
            writer.write("Product\tQty\tPrice\tTotal\n");
            writer.write("------------------------------------\n");

            for (int i = 0; i < tableModel.getRowCount(); i++) {

                String name = tableModel.getValueAt(i, 0).toString();
                String qty = tableModel.getValueAt(i, 2).toString();
                String price = tableModel.getValueAt(i, 1).toString();
                String total = tableModel.getValueAt(i, 4).toString();

                writer.write(name + "\t" + qty + "\t" + price + "\t" + total + "\n");
            }

            writer.write("------------------------------------\n");
            writer.write("Grand Total: " + grandTotal + "\n");
            writer.write("====================================\n");

            writer.close();

            JOptionPane.showMessageDialog(this,
                    "Bill Saved Successfully!\nFile: " + fileName);

            tableModel.setRowCount(0);
            grandTotal = 0;
            totalLabel.setText("Grand Total: 0.0");

        } catch (Exception e) {

            try {
                if (con != null) con.rollback();
            } catch (Exception ignored) {}

            JOptionPane.showMessageDialog(this, "Transaction Failed!");
            e.printStackTrace();

        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (Exception ignored) {}
        }
    }
}
