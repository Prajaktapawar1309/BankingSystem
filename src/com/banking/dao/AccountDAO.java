package com.banking.dao;

import com.banking.db.DBConnection;
import com.banking.model.Account;
import java.sql.*;

public class AccountDAO {

    // Create new account
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, account_number, account_type, balance, status) VALUES (?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account.getUserId());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getAccountType());
            ps.setDouble(4, account.getBalance());
            ps.setString(5, account.getStatus());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Create Account Error: " + e.getMessage());
            return false;
        }
    }

    // Get account by user id
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE user_id=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getDouble("balance"));
                account.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Get Account Error: " + e.getMessage());
        }
        return account;
    }

    // Get account by account number
    public Account getAccountByNumber(String accountNumber) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_number=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getDouble("balance"));
                account.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Get Account Error: " + e.getMessage());
        }
        return account;
    }

    // Update balance
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance=? WHERE account_id=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Update Balance Error: " + e.getMessage());
            return false;
        }
    }
}