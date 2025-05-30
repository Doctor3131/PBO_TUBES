/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import database.utility;
import database.account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Fauzi Amrullah
 */
public class service {
    Connection koneksi = null;
    
    public service(){
        koneksi = utility.getConnection();
    }
    
      // Membuat objek mahasiswa dari ResultSet
    public account makeMhsObject(ResultSet rs) throws SQLException {
        account customer = new account();
        customer.setEmail(rs.getString("email"));
        customer.setPassword(rs.getString("password"));
        customer.setSaldo(rs.getInt("money"));
        return customer;
    }

    // Menambahkan data mahasiswa
    public void add(account customer) {
        try {
            String sql = "INSERT INTO accounts (email, password, money) VALUES (?, ?, ?)";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, customer.getEmail());
            ps.setString(2, customer.getPassword());
            ps.setInt(3, customer.getSaldo());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error tambah data: " + e.getMessage());
        }
    }
    
    public account getByEmailAndPassword(String email, String password) {
        try {
            String sql = "SELECT * FROM accounts WHERE email = ? AND password = ?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return makeMhsObject(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error saat login: " + e.getMessage());
        }
        return null; // jika tidak ditemukan
    }

}
