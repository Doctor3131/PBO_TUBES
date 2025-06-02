package fetchsearchproduct;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import model.ProductModel;
import model.Transaksi; 

public class FetchSearchProduct {

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        List<ProductModel> keranjang = new ArrayList<>();
        List<Transaksi> daftarTransaksi = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== APLIKASI MANAJEMEN PRODUK =====");
            System.out.println("1. Tampilkan Semua Produk");
            System.out.println("2. Tambah Produk ke Keranjang");
            System.out.println("3. Lihat Keranjang");
            System.out.println("4. Batalkan Keranjang");     
            System.out.println("5. Checkout");               
            System.out.println("6. Lihat Riwayat Transaksi"); 
            System.out.println("0. Keluar");
            System.out.print("--> Masukkan pilihan Anda: ");

            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tampilkanSemuaProduk(productService);
                    break;
                case "2":
                    prosesTambahKeKeranjang(productService, keranjang, scanner);
                    break;
                case "3":
                    tampilkanKeranjang(keranjang);
                    break;
                case "4":
                    prosesPembatalanKeranjang(productService, keranjang);
                    break;
                case "5":
                    prosesCheckout(keranjang, daftarTransaksi);
                    break;
                case "6":
                    tampilkanRiwayatTransaksi(daftarTransaksi);
                    break;
                case "0":
                    exit(productService, keranjang, scanner);
                default:
                    System.out.println("\nPilihan tidak valid. Silakan coba lagi.");
                    break;
            }
        }
    }
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
    
    public static void prosesTambahKeKeranjang(ProductService productService, List<ProductModel> keranjang, Scanner scanner) {
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
                boolean itemSudahAda = false;
                for (ProductModel itemDiKeranjang : keranjang) {
                    if (itemDiKeranjang.getId() == idProduk) {
                        int kuantitasLama = itemDiKeranjang.getStok();
                        itemDiKeranjang.setStok(kuantitasLama + jumlah);
                        itemSudahAda = true;
                        System.out.println("Kuantitas produk di keranjang diperbarui.");
                        break;
                    }
                }

                if (!itemSudahAda) {
                    ProductModel produkDariDB = productService.getProductById(idProduk);
                    if (produkDariDB != null) {
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
                int kuantitas = item.getStok();
                double subtotal = item.getHarga() * kuantitas;
                totalBelanja += subtotal;

                System.out.printf("%-4d | %-30s | %-10d | Rp%,15.2f\n", 
                    item.getId(), item.getNamaProduk(), kuantitas, subtotal
                );
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("TOTAL KESELURUHAN: Rp%,.2f\n", totalBelanja);
        }
    }
    public static void prosesPembatalanKeranjang(ProductService productService, List<ProductModel> keranjang) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang sudah kosong, tidak ada yang perlu dibatalkan.");
            return;
        }
        boolean isSuccess = productService.kembalikanStokBatch(keranjang);
        if (isSuccess) {
            keranjang.clear(); 
            System.out.println("Keranjang berhasil dibatalkan. Semua item telah dikembalikan ke stok.");
        } else {
            System.out.println("Terjadi kesalahan saat membatalkan keranjang.");
        }
    }

    public static void prosesCheckout(List<ProductModel> keranjang, List<Transaksi> daftarTransaksi) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong, tidak bisa checkout.");
            return;
        }
        Transaksi transaksiBaru = new Transaksi(keranjang);
        daftarTransaksi.add(transaksiBaru);
        keranjang.clear();

        System.out.println("\nCheckout Berhasil!");
        System.out.println(transaksiBaru); 
    }

    public static void tampilkanRiwayatTransaksi(List<Transaksi> daftarTransaksi) {
        System.out.println("\n--- RIWAYAT TRANSAKSI ---");
        if (daftarTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi yang selesai.");
        } else {
            for (Transaksi trx : daftarTransaksi) {
                System.out.println(trx);
            }
        }
    }

    public static void exit(ProductService productService, List<ProductModel> keranjang, Scanner scanner) {
        if (!keranjang.isEmpty()) {
            System.out.println("Keranjang tidak kosong. Mengembalikan item ke stok sebelum keluar...");
            
            boolean isSuccess = productService.kembalikanStokBatch(keranjang);
            
            if (!isSuccess) {
                System.err.println("PERINGATAN: Terjadi kegagalan saat mencoba mengembalikan stok produk.");
            } else {
                System.out.println("Semua item berhasil dikembalikan ke stok.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan aplikasi. Sampai jumpa!");
        scanner.close(); 
        
        System.exit(0); 
    }
}