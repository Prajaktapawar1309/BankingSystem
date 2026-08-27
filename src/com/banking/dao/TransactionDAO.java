package com.banking.dao;

import com.banking.db.DBConnection;
import com.banking.model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean addTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after, description) VALUES (?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getAccountId());
            ps.setString(2, t.getTransactionType());
            ps.setDouble(3, t.getAmount());
            ps.setDouble(4, t.getBalanceAfter());
            ps.setString(5, t.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Transaction Error: " + e.getMessage());
            return false;
        }
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id=? ORDER BY transaction_date DESC";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setAccountId(rs.getInt("account_id"));
                t.setTransactionType(rs.getString("transaction_type"));
                t.setAmount(rs.getDouble("amount"));
                t.setBalanceAfter(rs.getDouble("balance_after"));
                t.setDescription(rs.getString("description"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Get Transaction Error: " + e.getMessage());
        }
        return list;
    }
}