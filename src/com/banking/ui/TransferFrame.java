package com.banking.ui;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;

public class TransferFrame extends JFrame {
    private JTextField txtToAccount, txtAmount, txtDescription;
    private JButton btnTransfer, btnBack;
    private User user;
    private Account account;
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public TransferFrame(User user, Account account) {
        this.user = user;
        this.account = account;

        setTitle("Transfer - Online Banking System");
        setSize(450, 420);
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

        JLabel lblTitle = new JLabel("🔄 FUND TRANSFER", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 25, 450, 40);
        mainPanel.add(lblTitle);

        JLabel lblBal = new JLabel("Current Balance: ₹ " + String.format("%.2f", account.getBalance()), SwingConstants.CENTER);
        lblBal.setFont(new Font("Arial", Font.BOLD, 14));
        lblBal.setForeground(new Color(0, 255, 150));
        lblBal.setBounds(0, 70, 450, 25);
        mainPanel.add(lblBal);

        JLabel lblTo = new JLabel("To Account No:");
        lblTo.setForeground(Color.WHITE);
        lblTo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTo.setBounds(50, 115, 130, 25);
        mainPanel.add(lblTo);

        txtToAccount = new JTextField();
        txtToAccount.setBounds(190, 115, 190, 30);
        mainPanel.add(txtToAccount);

        JLabel lblAmount = new JLabel("Amount (₹):");
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setFont(new Font("Arial", Font.BOLD, 13));
        lblAmount.setBounds(50, 165, 130, 25);
        mainPanel.add(lblAmount);

        txtAmount = new JTextField();
        txtAmount.setBounds(190, 165, 190, 30);
        mainPanel.add(txtAmount);

        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(new Font("Arial", Font.BOLD, 13));
        lblDesc.setBounds(50, 215, 130, 25);
        mainPanel.add(lblDesc);

        txtDescription = new JTextField();
        txtDescription.setBounds(190, 215, 190, 30);
        mainPanel.add(txtDescription);

        btnTransfer = new JButton("TRANSFER");
        btnTransfer.setBounds(80, 300, 130, 38);
        btnTransfer.setBackground(new Color(0, 150, 220));
        btnTransfer.setForeground(Color.WHITE);
        btnTransfer.setFont(new Font("Arial", Font.BOLD, 14));
        btnTransfer.setFocusPainted(false);
        mainPanel.add(btnTransfer);

        btnBack = new JButton("BACK");
        btnBack.setBounds(240, 300, 130, 38);
        btnBack.setBackground(new Color(100, 100, 100));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        mainPanel.add(btnBack);

        btnTransfer.addActionListener(e -> {
            String toAccNo = txtToAccount.getText().trim();
            String amtStr = txtAmount.getText().trim();
            String desc = txtDescription.getText().trim();

            if (toAccNo.isEmpty() || amtStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (toAccNo.equals(account.getAccountNumber())) {
                JOptionPane.showMessageDialog(this, "Cannot transfer to same account!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amtStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (amount > account.getBalance()) {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Account toAccount = accountDAO.getAccountByNumber(toAccNo);
                if (toAccount == null) {
                    JOptionPane.showMessageDialog(this, "Receiver account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Deduct from sender
                double newSenderBalance = account.getBalance() - amount;
                accountDAO.updateBalance(account.getAccountId(), newSenderBalance);

                Transaction t1 = new Transaction();
                t1.setAccountId(account.getAccountId());
                t1.setTransactionType("TRANSFER_OUT");
                t1.setAmount(amount);
                t1.setBalanceAfter(newSenderBalance);
                t1.setDescription("Transfer to " + toAccNo + (desc.isEmpty() ? "" : " - " + desc));
                transactionDAO.addTransaction(t1);

                // Add to receiver
                double newReceiverBalance = toAccount.getBalance() + amount;
                accountDAO.updateBalance(toAccount.getAccountId(), newReceiverBalance);

                Transaction t2 = new Transaction();
                t2.setAccountId(toAccount.getAccountId());
                t2.setTransactionType("TRANSFER_IN");
                t2.setAmount(amount);
                t2.setBalanceAfter(newReceiverBalance);
                t2.setDescription("Transfer from " + account.getAccountNumber());
                transactionDAO.addTransaction(t2);

                account.setBalance(newSenderBalance);
                JOptionPane.showMessageDialog(this,
                        "Transfer Successful!\nNew Balance: ₹ " + String.format("%.2f", newSenderBalance),
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