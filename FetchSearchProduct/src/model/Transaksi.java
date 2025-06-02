// Simpan sebagai Transaksi.java (disarankan di dalam package model)
package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Transaksi {
    private final String idTransaksi;
    private final LocalDateTime waktuTransaksi;
    private final List<ProductModel> items; // Salinan item saat checkout
    private final double totalBelanja;

    public Transaksi(List<ProductModel> keranjang) {
        this.idTransaksi = "TRX-" + System.currentTimeMillis();
        this.waktuTransaksi = LocalDateTime.now();
        // Penting: Buat salinan keranjang agar tidak terpengaruh saat keranjang asli dikosongkan
        this.items = new ArrayList<>(keranjang); 
        this.totalBelanja = hitungTotal(keranjang);
    }

    private double hitungTotal(List<ProductModel> keranjang) {
        double total = 0;
        for (ProductModel item : keranjang) {
            total += item.getHarga() * item.getStok(); // Ingat: getStok() di sini adalah kuantitas
        }
        return total;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder detail = new StringBuilder();
        detail.append("--- Transaksi ---\n");
        detail.append("ID    : ").append(idTransaksi).append("\n");
        detail.append("Waktu : ").append(waktuTransaksi.format(formatter)).append("\n");
        detail.append("Total : Rp").append(String.format("%,.2f", totalBelanja)).append("\n");
        detail.append("Items : \n");
        for (ProductModel item : items) {
            detail.append(String.format("  -> (ID: %d) %s - %d pcs\n", item.getId(), item.getNamaProduk(), item.getStok()));
        }
        return detail.toString();
    }
}