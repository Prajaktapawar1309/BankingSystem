package com.banking.ui;

import com.banking.model.Account;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User user;
    private Account account;

    public DashboardFrame(User user, Account account) {
        this.user = user;
        this.account = account;

        setTitle("Dashboard - Online Banking System");
        setSize(550, 500);
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

        // Title
        JLabel lblTitle = new JLabel("🏦 BANKING DASHBOARD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 550, 40);
        mainPanel.add(lblTitle);

        // Welcome
        JLabel lblWelcome = new JLabel("Welcome, " + user.getFullName(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 14));
        lblWelcome.setForeground(new Color(200, 220, 255));
        lblWelcome.setBounds(0, 60, 550, 25);
        mainPanel.add(lblWelcome);

        // Account Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(255, 255, 255, 50));
        infoPanel.setLayout(null);
        infoPanel.setBounds(50, 95, 450, 100);
        mainPanel.add(infoPanel);

        JLabel lblAccNo = new JLabel("Account No: " + (account != null ? account.getAccountNumber() : "N/A"));
        lblAccNo.setForeground(Color.WHITE);
        lblAccNo.setFont(new Font("Arial", Font.BOLD, 13));
        lblAccNo.setBounds(20, 15, 400, 25);
        infoPanel.add(lblAccNo);

        JLabel lblAccType = new JLabel("Account Type: " + (account != null ? account.getAccountType() : "N/A"));
        lblAccType.setForeground(Color.WHITE);
        lblAccType.setFont(new Font("Arial", Font.PLAIN, 13));
        lblAccType.setBounds(20, 40, 400, 25);
        infoPanel.add(lblAccType);

        JLabel lblBalance = new JLabel("Balance: ₹ " + (account != null ? String.format("%.2f", account.getBalance()) : "0.00"));
        lblBalance.setForeground(new Color(0, 255, 150));
        lblBalance.setFont(new Font("Arial", Font.BOLD, 16));
        lblBalance.setBounds(20, 65, 400, 25);
        infoPanel.add(lblBalance);

        // Buttons
        String[] btnNames = {"💰 Deposit", "💸 Withdraw", "🔄 Transfer", "📋 Transaction History", "🚪 Logout"};
        Color[] btnColors = {
                new Color(0, 180, 90),
                new Color(220, 50, 50),
                new Color(0, 150, 220),
                new Color(150, 80, 200),
                new Color(100, 100, 100)
        };

        int y = 220;
        for (int i = 0; i < btnNames.length; i++) {
            JButton btn = new JButton(btnNames[i]);
            btn.setBounds(150, y, 250, 38);
            btn.setBackground(btnColors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final int index = i;
            btn.addActionListener(e -> handleButton(index));
            mainPanel.add(btn);
            y += 48;
        }

        add(mainPanel);
    }

    private void handleButton(int index) {
        switch (index) {
            case 0:
                new DepositFrame(user, account).setVisible(true);
                dispose();
                break;
            case 1:
                new WithdrawFrame(user, account).setVisible(true);
                dispose();
                break;
            case 2:
                new TransferFrame(user, account).setVisible(true);
                dispose();
                break;
            case 3:
                new TransactionHistoryFrame(user, account).setVisible(true);
                dispose();
                break;
            case 4:
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?");
                if (confirm == JOptionPane.YES_OPTION) {
                    new LoginFrame().setVisible(true);
                    dispose();
                }
                break;
        }
    }
}