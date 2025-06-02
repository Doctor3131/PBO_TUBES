package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import model.Produk;
import model.CartItem;

public class SqlServices {

    private static final Properties dbProperties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/config.properties"; 

    static {
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            dbProperties.load(input);
        } catch (IOException ex) {
            System.err.println("Error: Could not load database configuration file: " + CONFIG_FILE_PATH);
            System.err.println("Application might not work correctly. Details: " + ex.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(dbProperties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver")); 
            String url = dbProperties.getProperty("db.url");
            String user = dbProperties.getProperty("db.user");
            String password = dbProperties.getProperty("db.password");

            if (url == null || user == null || password == null) {
                throw new SQLException("Database configuration (URL, user, password) not found in properties file.");
            }
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
             throw new SQLException("Gagal memuat driver JDBC: " + e.getMessage(), e);
        }
    }

    public List<Produk> getAllProducts() {
        List<Produk> productList = new ArrayList<>();
        String query = "SELECT * FROM produk_elektronik";
        try (Connection terkoneksi = getConnection();
             Statement stmt = terkoneksi.createStatement();
             ResultSet hasil = stmt.executeQuery(query)) {

            while (hasil.next()) {
                productList.add(new Produk(
                    hasil.getInt("id"),
                    hasil.getString("sku"),
                    hasil.getString("nama_produk"),
                    hasil.getString("kategori"),
                    hasil.getString("deskripsi"),
                    hasil.getDouble("harga"),
                    hasil.getInt("stok")
                ));
            }
        } catch (SQLException e) {
        }
        return productList;
    }

    public int reduceProductStock(int productId, int quantityToReduce) {
        String queryCekStok = "SELECT stok FROM produk_elektronik WHERE id = ? FOR UPDATE";
        String queryUpdateStok = "UPDATE produk_elektronik SET stok = ? WHERE id = ?";

        try (Connection terkoneksi = getConnection()) {
            terkoneksi.setAutoCommit(false);
            try {
                int stokSaatIni = -1;
                try (PreparedStatement psCek = terkoneksi.prepareStatement(queryCekStok)) {
                    psCek.setInt(1, productId);
                    try (ResultSet hasil = psCek.executeQuery()) {
                        if (hasil.next()) {
                            stokSaatIni = hasil.getInt("stok");
                        } else {
                            terkoneksi.rollback();
                            return 1; 
                        }
                    }
                }

                if (stokSaatIni < quantityToReduce) {
                    terkoneksi.rollback();
                    return 2; 
                }

                int stokBaru = stokSaatIni - quantityToReduce;
                try (PreparedStatement psUpdate = terkoneksi.prepareStatement(queryUpdateStok)) {
                    psUpdate.setInt(1, stokBaru);
                    psUpdate.setInt(2, productId);
                    psUpdate.executeUpdate();
                }

                terkoneksi.commit();
                return 0; 

            } catch (SQLException e) {
                terkoneksi.rollback();
                return -1; 
            }
        } catch (SQLException e) {
            return -1; 
        }
    }

    public Produk getProductById(int productId) {
        String query = "SELECT * FROM produk_elektronik WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produk(
                        rs.getInt("id"),
                        rs.getString("sku"),
                        rs.getString("nama_produk"),
                        rs.getString("kategori"),
                        rs.getString("deskripsi"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                    );
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean revertStockForCartItems(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return true; 
        }
        String queryUpdateStok = "UPDATE produk_elektronik SET stok = stok + ? WHERE id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(queryUpdateStok)) {
                for (CartItem item : items) {
                    ps.setInt(1, item.getQuantity());
                    ps.setInt(2, item.getProduk().getId());
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
