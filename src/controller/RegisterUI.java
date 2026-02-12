package controller;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JFrame {

    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public RegisterUI() {

        setTitle("Register User");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"MANAGER", "CASHIER"});
        panel.add(roleBox);

        JButton registerButton = new JButton("Register");
        panel.add(new JLabel());
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(e -> registerUser());
    }

    private void registerUser() {

        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        String selectedRole = roleBox.getSelectedItem().toString();

        int roleId = selectedRole.equals("MANAGER") ? 2 : 3;

        UserDAO dao = new UserDAO();
        boolean success = dao.register(new User(name, email, password, roleId));

        if (success) {
            JOptionPane.showMessageDialog(this, "User Registered Successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed!");
        }
    }
}
