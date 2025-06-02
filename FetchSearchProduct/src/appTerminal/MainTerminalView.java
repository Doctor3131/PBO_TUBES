package appTerminal;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import model.*;
import services.*;

public class MainTerminalView {

    public static void main(String[] args) {
        SqlServices productService = new SqlServices();
        List<Produk> keranjang = new ArrayList<>();
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
                    ProductServices.tampilkanSemuaProduk(productService);
                    break;
                case "2":
                    ProductServices.tambahKeranjang(productService, keranjang, scanner);
                    break;
                case "3":
                    ProductServices.tampilkanKeranjang(keranjang);
                    break;
                case "4":
                    ProductServices.pembatalanKeranjang(productService, keranjang);
                    break;
                case "5":
                    ProductServices.checkout(keranjang, daftarTransaksi);
                    break;
                case "6":
                    ProductServices.tampilkanRiwayatTransaksi(daftarTransaksi);
                    break;
                case "0":
                    ProductServices.exit(productService, keranjang, scanner);
                default:
                    System.out.println("\nPilihan tidak valid. Silakan coba lagi.");
                    break;
            }
        }
    }
}