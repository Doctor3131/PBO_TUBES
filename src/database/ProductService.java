package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private Connection koneksi;

    public ProductService() {
        koneksi = utility.getConnection();
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products WHERE name LIKE ?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error searching products: " + e.getMessage());
        }
        return products;
    }

    public Product getProductById(int id) {
        try {
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setDescription(rs.getString("description"));
                return product;
            }
        } catch (SQLException e) {
            System.out.println("Error getting product: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStock(int productId, int quantity) {
        try {
            String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
            return false;
        }
    }
} 