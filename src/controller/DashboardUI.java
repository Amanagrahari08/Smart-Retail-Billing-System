package controller;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {

    public DashboardUI() {

        setTitle("ERP Grocery - Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Welcome to ERP Grocery System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton productButton = new JButton("Product Management");
        JButton billingButton = new JButton("Billing");
        JButton logoutButton = new JButton("Logout");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        panel.add(title);
        panel.add(productButton);
        panel.add(billingButton);
        panel.add(logoutButton);

        add(panel);

        // ✅ Product button
        productButton.addActionListener(e -> {
            new ProductUI().setVisible(true);
        });

        // ✅ Billing button (Ye connect karna tha)
        billingButton.addActionListener(e -> {
            new BillingUI().setVisible(true);
        });

        // ✅ Logout
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });
    }
}
