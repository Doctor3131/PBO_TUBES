package fetchsearchproduct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import model.ProductModel;

public class ProductService {
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/sirielshop";
            String user = "root";
            String password = "Profesor_31"; // Ganti jika perlu
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Gagal memuat driver JDBC: " + e.getMessage(), e);
        }
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = new ArrayList<>();
        try (Connection terkoneksi = getConnection();
             Statement query = terkoneksi.createStatement();
             ResultSet hasil = query.executeQuery("SELECT * FROM produk_elektronik")) {

            while (hasil.next()) {
                int id = hasil.getInt("id");
                String sku = hasil.getString("sku");
                String namaProduk = hasil.getString("nama_produk");
                String kategori = hasil.getString("kategori");
                String deskripsi = hasil.getString("deskripsi");
                double harga = hasil.getDouble("harga");
                int stok = hasil.getInt("stok");
                productList.add(new ProductModel(id, sku, namaProduk, kategori, deskripsi, harga, stok));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data produk: " + e.getMessage());
        }
        return productList;
    }

    public boolean updateProduct(int productId, int kuantitas) {
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
                            System.err.println("Error: Produk dengan ID " + productId + " tidak ditemukan.");
                            terkoneksi.rollback();
                            return false;
                        }
                    }
                }

                if (stokSaatIni < kuantitas) {
                    System.err.println("Stok tidak mencukupi! Stok saat ini: " + stokSaatIni + ", Anda meminta: " + kuantitas);
                    terkoneksi.rollback();
                    return false;
                }

                int stokBaru = stokSaatIni - kuantitas;
                try (PreparedStatement psUpdate = terkoneksi.prepareStatement(queryUpdateStok)) {
                    psUpdate.setInt(1, stokBaru);
                    psUpdate.setInt(2, productId);
                    psUpdate.executeUpdate();
                }
                
                terkoneksi.commit(); 
                System.out.println("Berhasil! Stok produk ID " + productId + " telah diperbarui.");
                return true;

            } catch (SQLException e) {
                System.err.println("Terjadi error pada database. Transaksi dibatalkan.");
                terkoneksi.rollback(); 
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Gagal mendapatkan koneksi atau terjadi error besar pada transaksi.");
            return false;
        }
    }

    public ProductModel getProductById(int productId) {
        String query = "SELECT * FROM produk_elektronik WHERE id = ?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductModel(
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
            System.err.println("Error saat mencari produk by ID: " + e.getMessage());
        }
        return null; // Kembalikan null jika produk tidak ditemukan
    }

    public boolean kembalikanStokBatch(List<ProductModel> items) {
        if (items == null || items.isEmpty()) {
            return true; // Tidak ada yang perlu dilakukan
        }
        String queryUpdateStok = "UPDATE produk_elektronik SET stok = stok + ? WHERE id = ?";
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Memulai transaksi
            try (PreparedStatement ps = conn.prepareStatement(queryUpdateStok)) {
                for (ProductModel item : items) {
                    ps.setInt(1, item.getStok());    // kuantitas yang dikembalikan
                    ps.setInt(2, item.getId());      // id produk
                    ps.addBatch();                   // Tambahkan perintah ke dalam batch
                }
                ps.executeBatch(); // Eksekusi semua perintah sekaligus
                conn.commit();     // Simpan semua perubahan jika berhasil
                return true;
            } catch (SQLException e) {
                System.err.println("Gagal mengembalikan stok. Transaksi dibatalkan.");
                conn.rollback(); // Batalkan semua perubahan jika ada error
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Gagal mendapatkan koneksi untuk pengembalian stok.");
            return false;
        }
    }
}