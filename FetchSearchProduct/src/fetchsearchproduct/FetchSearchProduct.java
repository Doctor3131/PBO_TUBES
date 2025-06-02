package fetchsearchproduct;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import model.ProductModel;
// Hapus 'import model.KeranjangItem;' jika ada

public class FetchSearchProduct {

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        // PERUBAHAN: Tipe list kembali menjadi ProductModel
        List<ProductModel> keranjang = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== APLIKASI MANAJEMEN PRODUK =====");
            System.out.println("1. Tampilkan Semua Produk");
            System.out.println("2. Tambah Produk ke Keranjang");
            System.out.println("3. Lihat Keranjang");
            System.out.println("0. Keluar");
            System.out.print("--> Masukkan pilihan Anda: ");

            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tampilkanSemuaProduk(productService);
                    break;
                case "2":
                    // PERUBAHAN: Mengirim List<ProductModel> keranjang
                    prosesTambahKeKeranjang(productService, scanner, keranjang);
                    break;
                case "3":
                    // PERUBAHAN: Mengirim List<ProductModel> keranjang
                    tampilkanKeranjang(keranjang);
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
    
    // Metode ini tidak berubah
    public static void tampilkanSemuaProduk(ProductService productService) {
        System.out.println("\nMengambil data produk terbaru...");
        List<ProductModel> produkTerbaru = productService.getAllProducts();
        System.out.println("\n--- DAFTAR SEMUA PRODUK ---");
        if (produkTerbaru != null && !produkTerbaru.isEmpty()) {
            System.out.println("Berhasil menemukan " + produkTerbaru.size() + " item:");
            for (ProductModel produk : produkTerbaru) {
                System.out.println("  -> " + produk);
            }
        } else {
            System.out.println("Gagal mengambil data atau tidak ada produk.");
        }
    }
    
    // PERUBAHAN LOGIKA UTAMA ADA DI SINI
    public static void prosesTambahKeKeranjang(ProductService productService, Scanner scanner, List<ProductModel> keranjang) {
        try {
            System.out.print("Masukkan ID Produk yang akan ditambahkan: ");
            int idProduk = Integer.parseInt(scanner.nextLine());

            System.out.print("Masukkan Jumlah: ");
            int jumlah = Integer.parseInt(scanner.nextLine());
            
            if (jumlah <= 0) {
                System.out.println("Jumlah harus lebih dari 0.");
                return;
            }

            boolean isSuccess = productService.updateProduct(idProduk, jumlah);

            if (isSuccess) {
                // Cek apakah produk sudah ada di keranjang
                boolean itemSudahAda = false;
                for (ProductModel itemDiKeranjang : keranjang) {
                    if (itemDiKeranjang.getId() == idProduk) {
                        // Jika sudah ada, tambahkan kuantitasnya (yang disimpan di 'stok')
                        int kuantitasLama = itemDiKeranjang.getStok();
                        itemDiKeranjang.setStok(kuantitasLama + jumlah);
                        itemSudahAda = true;
                        System.out.println("Kuantitas produk di keranjang diperbarui.");
                        break;
                    }
                }

                // Jika produk belum ada di keranjang
                if (!itemSudahAda) {
                    ProductModel produkDariDB = productService.getProductById(idProduk);
                    if (produkDariDB != null) {
                        // "Repurpose" field 'stok' untuk menyimpan 'jumlah' kuantitas
                        produkDariDB.setStok(jumlah);
                        keranjang.add(produkDariDB);
                        System.out.println("Produk '" + produkDariDB.getNamaProduk() + "' berhasil ditambahkan ke keranjang.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Input tidak valid. Harap masukkan angka untuk ID dan Jumlah.");
        }
    }
    
    public static void tampilkanKeranjang(List<ProductModel> keranjang) {
        System.out.println("\n--- ISI KERANJANG BELANJA ---");
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang Anda masih kosong.");
        } else {
            double totalBelanja = 0;
            System.out.printf("%-4s | %-30s | %-10s | %-15s\n", "ID", "Nama Produk", "Kuantitas", "Subtotal");
            System.out.println("----------------------------------------------------------------------");

            for (ProductModel item : keranjang) {
                // Di sini, item.getStok() berarti KUANTITAS, bukan sisa stok
                int kuantitas = item.getStok();
                double subtotal = item.getHarga() * kuantitas;
                totalBelanja += subtotal;

                System.out.printf("%-4d | %-30s | %-10d | Rp%,15.2f\n", 
                    item.getId(), 
                    item.getNamaProduk(), 
                    kuantitas, // <-- menggunakan getStok() sebagai kuantitas
                    subtotal
                );
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("TOTAL KESELURUHAN: Rp%,.2f\n", totalBelanja);
        }
    }
}