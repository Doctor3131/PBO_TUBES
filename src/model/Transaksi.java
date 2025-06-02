package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Transaksi {
    private final String idTransaksi;
    private final LocalDateTime waktuTransaksi;
    private final List<CartItem> items; 
    private final double totalBelanja;

    public Transaksi(List<CartItem> keranjang) {
        this.idTransaksi = "TRX-" + System.currentTimeMillis();
        this.waktuTransaksi = LocalDateTime.now();
        this.items = new ArrayList<>(keranjang); 
        this.totalBelanja = hitungTotal(keranjang);
    }

    private double hitungTotal(List<CartItem> keranjang) {
        double total = 0;
        for (CartItem item : keranjang) {
            total += item.getSubtotal(); 
        }
        return total;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public LocalDateTime getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items); 
    }

    public double getTotalBelanja() {
        return totalBelanja;
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
        for (CartItem item : items) {
            detail.append(String.format("  -> (ID: %d) %s - %d pcs @ Rp%,.2f = Rp%,.2f\n",
                    item.getProduk().getId(),
                    item.getProduk().getNamaProduk(),
                    item.getQuantity(),
                    item.getProduk().getHarga(),
                    item.getSubtotal()));
        }
        return detail.toString();
    }
}
