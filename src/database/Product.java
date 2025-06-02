/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

public class Product {
    private String nama;
    private int harga;
    private int stok;

    // Constructor default
    public Product() {
        // Konstruktor kosong bisa digunakan untuk inisialisasi default jika diperlukan
    }

    // Constructor dengan parameter
    public Product(String nama, int harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // Getter dan Setter untuk nama
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    // Getter dan Setter untuk harga
    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    // Getter dan Setter untuk stok
    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    // Anda bisa menambahkan metode lain di sini jika diperlukan,
    // misalnya untuk menampilkan detail produk:
    @Override
    public String toString() {
        return "Product{" +
               "nama='" + nama + '\'' +
               ", harga=" + harga +
               ", stok=" + stok +
               '}';
    }
}