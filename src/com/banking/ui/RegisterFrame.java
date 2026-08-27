package com.banking.ui;

import com.banking.dao.AccountDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Account;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField txtName, txtEmail, txtPhone, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbAccountType;
    private JButton btnRegister, btnBack;
    private UserDAO userDAO = new UserDAO();
    private AccountDAO accountDAO = new AccountDAO();

    public RegisterFrame() {
        setTitle("Register - Online Banking System");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 102, 204),
                        getWidth(), getHeight(), new Color(0, 51, 102));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        JLabel lblTitle = new JLabel("CREATE NEW ACCOUNT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 500, 35);
        mainPanel.add(lblTitle);

        String[] labels = {"Full Name:", "Email:", "Phone:", "Username:", "Password:", "Account Type:"};
        int y = 70;
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            lbl.setBounds(60, y, 120, 25);
            mainPanel.add(lbl);
            y += 50;
        }

        txtName = new JTextField(); txtName.setBounds(190, 70, 200, 28); mainPanel.add(txtName);
        txtEmail = new JTextField(); txtEmail.setBounds(190, 120, 200, 28); mainPanel.add(txtEmail);
        txtPhone = new JTextField(); txtPhone.setBounds(190, 170, 200, 28); mainPanel.add(txtPhone);
        txtUsername = new JTextField(); txtUsername.setBounds(190, 220, 200, 28); mainPanel.add(txtUsername);
        txtPassword = new JPasswordField(); txtPassword.setBounds(190, 270, 200, 28); mainPanel.add(txtPassword);
        cmbAccountType = new JComboBox<>(new String[]{"SAVINGS", "CURRENT"});
        cmbAccountType.setBounds(190, 320, 200, 28); mainPanel.add(cmbAccountType);

        btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(100, 400, 130, 35);
        btnRegister.setBackground(new Color(0, 204, 102));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        mainPanel.add(btnRegister);

        btnBack = new JButton("BACK");
        btnBack.setBounds(270, 400, 130, 35);
        btnBack.setBackground(new Color(255, 80, 80));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        mainPanel.add(btnBack);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String accType = (String) cmbAccountType.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User();
            user.setFullName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setUsername(username);
            user.setPassword(password);

            boolean userCreated = userDAO.register(user);
            if (userCreated) {
                User newUser = userDAO.login(username, password);
                String accNumber = "ACC" + System.currentTimeMillis();
                Account account = new Account();
                account.setUserId(newUser.getUserId());
                account.setAccountNumber(accNumber);
                account.setAccountType(accType);
                account.setBalance(0.0);
                account.setStatus("ACTIVE");
                accountDAO.createAccount(account);
                JOptionPane.showMessageDialog(this, "Registration Successful!\nAccount Number: " + accNumber);
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(mainPanel);
    }
}