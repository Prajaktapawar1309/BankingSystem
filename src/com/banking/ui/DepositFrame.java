package com.banking.ui;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;

public class DepositFrame extends JFrame {
    private JTextField txtAmount, txtDescription;
    private JButton btnDeposit, btnBack;
    private User user;
    private Account account;
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public DepositFrame(User user, Account account) {
        this.user = user;
        this.account = account;

        setTitle("Deposit - Online Banking System");
        setSize(450, 380);
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

        JLabel lblTitle = new JLabel("💰 DEPOSIT MONEY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 25, 450, 40);
        mainPanel.add(lblTitle);

        JLabel lblBal = new JLabel("Current Balance: ₹ " + String.format("%.2f", account.getBalance()), SwingConstants.CENTER);
        lblBal.setFont(new Font("Arial", Font.BOLD, 14));
        lblBal.setForeground(new Color(0, 255, 150));
        lblBal.setBounds(0, 70, 450, 25);
        mainPanel.add(lblBal);

        JLabel lblAmount = new JLabel("Amount (₹):");
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setFont(new Font("Arial", Font.BOLD, 13));
        lblAmount.setBounds(70, 120, 120, 25);
        mainPanel.add(lblAmount);

        txtAmount = new JTextField();
        txtAmount.setBounds(200, 120, 180, 30);
        mainPanel.add(txtAmount);

        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(new Font("Arial", Font.BOLD, 13));
        lblDesc.setBounds(70, 170, 120, 25);
        mainPanel.add(lblDesc);

        txtDescription = new JTextField();
        txtDescription.setBounds(200, 170, 180, 30);
        mainPanel.add(txtDescription);

        btnDeposit = new JButton("DEPOSIT");
        btnDeposit.setBounds(80, 250, 130, 38);
        btnDeposit.setBackground(new Color(0, 180, 90));
        btnDeposit.setForeground(Color.WHITE);
        btnDeposit.setFont(new Font("Arial", Font.BOLD, 14));
        btnDeposit.setFocusPainted(false);
        mainPanel.add(btnDeposit);

        btnBack = new JButton("BACK");
        btnBack.setBounds(240, 250, 130, 38);
        btnBack.setBackground(new Color(255, 80, 80));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        mainPanel.add(btnBack);

        btnDeposit.addActionListener(e -> {
            String amtStr = txtAmount.getText().trim();
            String desc = txtDescription.getText().trim();

            if (amtStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amtStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double newBalance = account.getBalance() + amount;
                accountDAO.updateBalance(account.getAccountId(), newBalance);

                Transaction t = new Transaction();
                t.setAccountId(account.getAccountId());
                t.setTransactionType("DEPOSIT");
                t.setAmount(amount);
                t.setBalanceAfter(newBalance);
                t.setDescription(desc.isEmpty() ? "Deposit" : desc);
                transactionDAO.addTransaction(t);

                account.setBalance(newBalance);
                JOptionPane.showMessageDialog(this,
                        "Deposit Successful!\nNew Balance: ₹ " + String.format("%.2f", newBalance),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                new DashboardFrame(user, account).setVisible(true);
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardFrame(user, account).setVisible(true);
            dispose();
        });

        add(mainPanel);
    }
}