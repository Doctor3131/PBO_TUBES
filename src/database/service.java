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
    public account makeAccount(ResultSet rs) throws SQLException {
        account customer = new account();
        customer.setEmail(rs.getString("email"));
        customer.setPassword(rs.getString("password"));
        customer.setAlamat(rs.getString("alamat"));
        customer.setSaldo(rs.getInt("money"));
        return customer;
    }

    // Menambahkan data mahasiswa
    public void add(account customer) {
        try {
            String sql = "INSERT INTO accounts (email, password, alamat, money) VALUES (?, ?, ?,?)";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, customer.getEmail());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getAlamat());
            ps.setInt(4, customer.getSaldo());
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
                return makeAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error saat login: " + e.getMessage());
        }
        return null; // jika tidak ditemukan
    }
    
    public boolean isEmailExist(String email) {
        try {
            String sql = "SELECT * FROM accounts WHERE email = ?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true jika email ditemukan
        } catch (SQLException e) {
            System.out.println("Error cek email: " + e.getMessage());
        }
        return false;
    }
    
    public List<Product> loadProdukDariDatabase() {
        List<Product> daftarProduk = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setNama(rs.getString("nama"));
                p.setHarga(rs.getInt("harga"));
                p.setStok(rs.getInt("stok"));
                daftarProduk.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error load produk: " + e.getMessage());
        }
        return daftarProduk;
    }

}
