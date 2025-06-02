package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Asumsi Anda punya kelas ProductModel seperti ini
// import com.yourproject.ProductModel; 

public class ProductService {

    private static Connection koneksi;

    /**
     * Metode ini HANYA bertanggung jawab untuk membuat dan mengembalikan
     * sebuah koneksi database. Menggunakan pola Singleton sederhana.
     */
    public static Connection getConnection() {
        if (koneksi == null) {
            try {
                // 1. Gunakan driver modern
                Class.forName("com.mysql.cj.jdbc.Driver");

                // 2. Detail koneksi
                String url = "jdbc:mysql://localhost:3306/sirielshop";
                String user = "root";
                String password = "Profesor_31"; // Ganti jika perlu

                // 3. Buat koneksi
                koneksi = DriverManager.getConnection(url, user, password);
                
                System.out.println("Koneksi berhasil dibuat!");

            } catch (ClassNotFoundException e) {
                System.err.println("Gagal load driver: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Gagal koneksi ke database: " + e.getMessage());
            }
        }
        // 4. Kembalikan objek koneksi yang sudah ada atau yang baru dibuat
        return koneksi;
    }

    /**
     * Contoh metode untuk mengambil semua produk dari database.
     * Metode ini MENGGUNAKAN koneksi yang didapat dari getConnection().
     */
    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = new ArrayList<>();
        // Gunakan try-with-resources untuk memastikan Statement dan ResultSet tertutup otomatis
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) { // 5. Gunakan executeQuery

            // 6. Proses hasil query (ResultSet)
            while (rs.next()) {
                int id = rs.getInt("id"); // Ganti "id" sesuai nama kolom di tabel Anda
                String nama = rs.getString("nama_produk"); // Ganti "nama_produk"
                double harga = rs.getDouble("harga"); // Ganti "harga"
                
                // Buat objek ProductModel dan tambahkan ke list
                productList.add(new ProductModel(id, nama, harga));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data produk: " + e.getMessage());
        }
        
        return productList;
    }
}