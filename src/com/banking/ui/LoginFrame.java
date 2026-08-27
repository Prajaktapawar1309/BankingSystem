package com.banking.ui;

import com.banking.dao.AccountDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Account;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Online Banking System - Login");
        setSize(450, 350);
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

        JLabel lblTitle = new JLabel("🏦 ONLINE BANKING SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 30, 450, 40);
        mainPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 13));
        lblUser.setBounds(80, 110, 100, 25);
        mainPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(180, 110, 180, 30);
        mainPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.BOLD, 13));
        lblPass.setBounds(80, 155, 100, 25);
        mainPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(180, 155, 180, 30);
        mainPanel.add(txtPassword);

        btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(100, 220, 100, 35);
        btnLogin.setBackground(new Color(0, 204, 102));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        mainPanel.add(btnLogin);

        btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(240, 220, 110, 35);
        btnRegister.setBackground(new Color(255, 153, 0));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        mainPanel.add(btnRegister);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password!");
                return;
            }
            User user = userDAO.login(username, password);
            if (user != null) {
                AccountDAO accountDAO = new AccountDAO();
                Account account = accountDAO.getAccountByUserId(user.getUserId());
                new DashboardFrame(user, account).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }
}