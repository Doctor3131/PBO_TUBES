package fetchsearchproduct;

import java.util.List;
import java.util.Scanner;
import model.ProductModel;

public class FetchSearchProduct {

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        List<ProductModel> daftarProduk = productService.getAllProducts();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== APLIKASI MANAJEMEN PRODUK =====");
            System.out.println("1. Tampilkan Semua Produk");
            System.out.println("0. Keluar");
            System.out.print("--> Masukkan pilihan Anda: ");

            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tampilkanSemuaProduk(daftarProduk);
                    break;
                case "0":
                    System.out.println("\nTerima kasih telah menggunakan aplikasi. Sampai jumpa!");
                    scanner.close(); 
                    return; 
                default:
                    System.out.println("\nPilihan tidak valid. Silakan coba lagi.");
                    break;
            }
        }
    }

    
    public static void tampilkanSemuaProduk(List<ProductModel> daftarProduk) {
        System.out.println("\n--- DAFTAR SEMUA PRODUK ---");
        if (daftarProduk != null && !daftarProduk.isEmpty()) {
            System.out.println("Berhasil menemukan " + daftarProduk.size() + " item:");
            for (ProductModel produk : daftarProduk) {
                System.out.println("  -> " + produk);
            }
        } else {
            System.out.println("Gagal mengambil data. Tidak ada produk di database atau koneksi bermasalah.");
        }
    }
}