package com.banking.ui;

import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionHistoryFrame extends JFrame {
    private User user;
    private Account account;
    private TransactionDAO transactionDAO = new TransactionDAO();

    public TransactionHistoryFrame(User user, Account account) {
        this.user = user;
        this.account = account;

        setTitle("Transaction History - Online Banking System");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(0, 51, 102));

        JLabel lblTitle = new JLabel("📋 TRANSACTION HISTORY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 15, 750, 35);
        mainPanel.add(lblTitle);

        JLabel lblAcc = new JLabel("Account: " + account.getAccountNumber() + "  |  Balance: ₹ " + String.format("%.2f", account.getBalance()), SwingConstants.CENTER);
        lblAcc.setFont(new Font("Arial", Font.BOLD, 13));
        lblAcc.setForeground(new Color(0, 255, 150));
        lblAcc.setBounds(0, 55, 750, 25);
        mainPanel.add(lblAcc);

        // Table
        String[] columns = {"#", "Type", "Amount (₹)", "Balance After (₹)", "Description", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(account.getAccountId());
        int sr = 1;
        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                    sr++,
                    t.getTransactionType(),
                    String.format("%.2f", t.getAmount()),
                    String.format("%.2f", t.getBalanceAfter()),
                    t.getDescription(),
                    t.getTransactionDate()
            });
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(0, 150, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 90, 710, 330);
        mainPanel.add(scrollPane);

        JButton btnBack = new JButton("⬅️ BACK TO DASHBOARD");
        btnBack.setBounds(270, 435, 210, 35);
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 13));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(btnBack);

        btnBack.addActionListener(e -> {
            new DashboardFrame(user, account).setVisible(true);
            dispose();
        });

        add(mainPanel);
    }
}