package fetchsearchproduct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.ProductModel;


public class ProductService {

    private static Connection koneksi;

    public static Connection getConnection() {
        if (koneksi == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String url = "jdbc:mysql://localhost:3306/sirielshop";
                String user = "root";
                String password = "Profesor_31"; 

                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi berhasil dibuat!");

            } catch (ClassNotFoundException e) {
                System.err.println("Gagal load driver: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Gagal koneksi ke database: " + e.getMessage());
            }
        }
        return koneksi;
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = new ArrayList<>();
        try (Connection terkoneksi = getConnection();
             Statement query = terkoneksi.createStatement();
             ResultSet rs = query.executeQuery("SELECT * FROM produk_elektronik")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String sku = rs.getString("sku");
                String namaProduk = rs.getString("nama_produk");
                String kategori = rs.getString("kategori");
                String deskripsi = rs.getString("deskripsi");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");

                ProductModel produk = new ProductModel(id, sku, namaProduk, kategori, deskripsi, harga, stok);
                productList.add(produk);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data produk: " + e.getMessage());
        }
        
        return productList;
    }
}