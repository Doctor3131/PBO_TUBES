/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.util.*;

public class cart {
    private List<Product> daftarProduk;
    
    public cart() {
        daftarProduk = new ArrayList<>();
    }

    public void tambahProduk(Product produk) {
        daftarProduk.add(produk);
    }

    public void hapusProduk(Product produk) {
        daftarProduk.remove(produk);
    }

    public List<Product> getDaftarProduk() {
        return daftarProduk;
    }

    public int getTotalHarga() {
        int total = 0;
        for (Product p : daftarProduk) {
            total += p.getHarga();
        }
        return total;
    }

    public void clear() {
        daftarProduk.clear();
    }
}
