package appTerminal;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import model.Produk;
import model.CartItem;
import model.Transaksi;
import services.ProductServices;
import services.SqlServices;

public class MainTerminalRun {

    public static void main(String[] args) {
        SqlServices sqlServices = new SqlServices(); 
        ProductServices productServices = new ProductServices(sqlServices); 
        List<CartItem> keranjang = new ArrayList<>();
        List<Transaksi> daftarTransaksi = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
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
                    tampilkanSemuaProdukUI(productServices);
                    break;
                case "2":
                    tambahProdukKeKeranjangUI(productServices, keranjang, scanner);
                    break;
                case "3":
                    lihatKeranjangUI(keranjang);
                    break;
                case "4":
                    batalkanKeranjangUI(productServices, keranjang);
                    break;
                case "5":
                    checkoutUI(productServices, keranjang, daftarTransaksi);
                    break;
                case "6":
                    lihatRiwayatTransaksiUI(daftarTransaksi);
                    break;
                case "0":
                    isRunning = keluarAplikasiUI(productServices, keranjang, scanner);
                    break;
                default:
                    System.out.println("\nPilihan tidak valid. Silakan coba lagi.");
                    break;
            }
        }
        System.out.println("\nAplikasi ditutup.");
    }

    private static void tampilkanSemuaProdukUI(ProductServices productServices) {
        System.out.println("\nMengambil data produk terbaru...");
        List<Produk> produkList = productServices.getAllProducts();

        System.out.println("\n--- DAFTAR SEMUA PRODUK ---");
        if (produkList != null && !produkList.isEmpty()) {
            System.out.println("Berhasil menemukan " + produkList.size() + " item:");
            for (Produk produk : produkList) {
                System.out.println("  -> " + produk); 
            }
        } else if (produkList == null) {
            System.out.println("Gagal mengambil data produk dari database.");
        } else {
            System.out.println("Tidak ada produk yang tersedia saat ini.");
        }
    }

    private static void tambahProdukKeKeranjangUI(ProductServices productServices, List<CartItem> keranjang, Scanner scanner) {
        try {
            System.out.print("Masukkan ID Produk yang akan ditambahkan: ");
            int idProduk = Integer.parseInt(scanner.nextLine());

            System.out.print("Masukkan Jumlah: ");
            int jumlah = Integer.parseInt(scanner.nextLine());

            int status = productServices.addItemToCart(keranjang, idProduk, jumlah);

            switch (status) {
                case 0:
                    Produk p = productServices.getAllProducts().stream().filter(prod -> prod.getId() == idProduk).findFirst().orElse(null);
                    String namaProduk = (p != null) ? p.getNamaProduk() : "Produk ID " + idProduk;
                    System.out.println("Produk '" + namaProduk + "' (" + jumlah + " pcs) berhasil ditambahkan/diperbarui di keranjang.");
                    break;
                case 1:
                    System.out.println("Error: Produk dengan ID " + idProduk + " tidak ditemukan.");
                    break;
                case 2:
                    System.out.println("Error: Stok tidak mencukupi untuk produk ID " + idProduk + ".");
                    break;
                case 3:
                    System.out.println("Error: Terjadi kesalahan pada database saat memperbarui stok.");
                    break;
                case 4:
                     System.out.println("Error: Jumlah produk harus lebih dari 0.");
                     break;
                default:
                    System.out.println("Error: Terjadi kesalahan yang tidak diketahui saat menambahkan produk ke keranjang.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.err.println("Input tidak valid. Harap masukkan angka untuk ID dan Jumlah.");
        }
    }

    private static void lihatKeranjangUI(List<CartItem> keranjang) {
        System.out.println("\n--- ISI KERANJANG BELANJA ---");
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang Anda masih kosong.");
        } else {
            double totalBelanja = 0;
            System.out.printf("%-4s | %-30s | %-10s | %-15s | %-15s\n", "ID", "Nama Produk", "Kuantitas", "Harga Satuan", "Subtotal");
            System.out.println("-----------------------------------------------------------------------------------------");

            for (CartItem item : keranjang) {
                Produk produk = item.getProduk();
                System.out.printf("%-4d | %-30s | %-10d | Rp%,15.2f | Rp%,15.2f\n",
                        produk.getId(),
                        produk.getNamaProduk(),
                        item.getQuantity(),
                        produk.getHarga(),
                        item.getSubtotal());
                totalBelanja += item.getSubtotal();
            }
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.printf("TOTAL KESELURUHAN: Rp%,.2f\n", totalBelanja);
        }
    }

    private static void batalkanKeranjangUI(ProductServices productServices, List<CartItem> keranjang) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang sudah kosong, tidak ada yang perlu dibatalkan.");
            return;
        }
        boolean isSuccess = productServices.cancelCart(keranjang);
        if (isSuccess) {
            keranjang.clear();
            System.out.println("Keranjang berhasil dibatalkan. Semua item telah dikembalikan ke stok.");
        } else {
            System.out.println("Terjadi kesalahan saat membatalkan keranjang dan mengembalikan stok.");
        }
    }

    private static void checkoutUI(ProductServices productServices, List<CartItem> keranjang, List<Transaksi> daftarTransaksi) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong, tidak bisa checkout.");
            return;
        }
        Transaksi transaksiBaru = productServices.processCheckout(keranjang);
        if (transaksiBaru != null) {
            daftarTransaksi.add(transaksiBaru);
            keranjang.clear(); 
            System.out.println("\nCheckout Berhasil!");
            System.out.println(transaksiBaru); 
        } else {
            System.out.println("Gagal melakukan checkout.");
        }
    }

    private static void lihatRiwayatTransaksiUI(List<Transaksi> daftarTransaksi) {
        System.out.println("\n--- RIWAYAT TRANSAKSI ---");
        if (daftarTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi yang selesai.");
        } else {
            for (Transaksi trx : daftarTransaksi) {
                System.out.println(trx); 
                System.out.println("---"); 
            }
        }
    }

    private static boolean keluarAplikasiUI(ProductServices productServices, List<CartItem> keranjang, Scanner scanner) {
        if (!keranjang.isEmpty()) {
            System.out.println("Keranjang tidak kosong. Apakah Anda ingin mengembalikan item ke stok sebelum keluar? (y/n)");
            String konfirmasi = scanner.nextLine().trim().toLowerCase();
            if (konfirmasi.equals("y")) {
                boolean revertSuccess = productServices.handleExit(keranjang);
                if (revertSuccess) {
                    keranjang.clear(); 
                    System.out.println("Semua item berhasil dikembalikan ke stok.");
                } else {
                    System.err.println("PERINGATAN: Terjadi kegagalan saat mencoba mengembalikan stok produk.");
                    System.out.println("Apakah Anda tetap ingin keluar? (y/n)");
                    if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                        return true; 
                    }
                }
            } else {
                 System.out.println("Item di keranjang tidak dikembalikan ke stok.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan aplikasi. Sampai jumpa!");
        scanner.close();
        return false; 
    }
}
