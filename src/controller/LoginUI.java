package controller;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginUI() {

        setTitle("ERP Grocery - Login");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("ERP Grocery Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        formPanel.add(loginButton);
        formPanel.add(registerButton);

        panel.add(formPanel, BorderLayout.CENTER);

        add(panel);

        // 🔹 Login Action
        loginButton.addActionListener(e -> loginUser());

        // 🔹 Register Action
        registerButton.addActionListener(e -> {
            new RegisterUI().setVisible(true);
        });
    }

    private void loginUser() {

        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!");
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean isLogin = userDAO.login(email, password);

        if (isLogin) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            dispose();
            new DashboardUI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!");
        }
    }
}
