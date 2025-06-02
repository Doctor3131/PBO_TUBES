package services;

import java.util.List;
import java.util.Scanner;
import model.Produk;
import model.Transaksi; 

public class ProductServices {
    public static void tampilkanSemuaProduk(SqlServices productService) {
        System.out.println("\nMengambil data produk terbaru...");
        List<Produk> produkTerbaru = productService.getAllProducts();
        
        System.out.println("\n--- DAFTAR SEMUA PRODUK ---");
        if (produkTerbaru != null && !produkTerbaru.isEmpty()) {
            System.out.println("Berhasil menemukan " + produkTerbaru.size() + " item:");
            for (Produk produk : produkTerbaru) {
                System.out.println("  -> " + produk);
            }
        } else {
            System.out.println("Gagal mengambil data atau tidak ada produk.");
        }
    }
    
    public static void tambahKeranjang(SqlServices productService, List<Produk> keranjang, Scanner scanner) {
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
                for (Produk itemDiKeranjang : keranjang) {
                    if (itemDiKeranjang.getId() == idProduk) {
                        int kuantitasLama = itemDiKeranjang.getStok();
                        itemDiKeranjang.setStok(kuantitasLama + jumlah);
                        itemSudahAda = true;
                        System.out.println("Kuantitas produk di keranjang diperbarui.");
                        break;
                    }
                }

                if (!itemSudahAda) {
                    Produk produkDariDB = productService.getProductById(idProduk);
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
    
    public static void tampilkanKeranjang(List<Produk> keranjang) {
        System.out.println("\n--- ISI KERANJANG BELANJA ---");
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang Anda masih kosong.");
        } else {
            double totalBelanja = 0;

            System.out.printf("%-4s | %-30s | %-10s | %-15s\n", "ID", "Nama Produk", "Kuantitas", "Subtotal");
            System.out.println("----------------------------------------------------------------------");

            for (Produk item : keranjang) {
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
    public static void pembatalanKeranjang(SqlServices productService, List<Produk> keranjang) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang sudah kosong, tidak ada yang perlu dibatalkan.");
            return;
        }
        boolean isSuccess = productService.revertKeranjang(keranjang);
        if (isSuccess) {
            keranjang.clear(); 
            System.out.println("Keranjang berhasil dibatalkan. Semua item telah dikembalikan ke stok.");
        } else {
            System.out.println("Terjadi kesalahan saat membatalkan keranjang.");
        }
    }

    public static void checkout(List<Produk> keranjang, List<Transaksi> daftarTransaksi) {
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

    public static void exit(SqlServices productService, List<Produk> keranjang, Scanner scanner) {
        if (!keranjang.isEmpty()) {
            System.out.println("Keranjang tidak kosong. Mengembalikan item ke stok sebelum keluar...");
            
            boolean isSuccess = productService.revertKeranjang(keranjang);
            
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
