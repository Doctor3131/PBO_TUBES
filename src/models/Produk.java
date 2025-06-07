package models;

public class Produk {
    private int id;
    private String sku;
    private String namaProduk;
    private String kategori;
    private String deskripsi;
    private double harga;
    private int stok; 

    public Produk(int id, String sku, String namaProduk, String kategori, String deskripsi, double harga, int stok) {
        this.id = id;
        this.sku = sku;
        this.namaProduk = namaProduk;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
    }

    public Produk(int id, String nama, double harga) {
        this.id = id;
        this.namaProduk = nama;
        this.harga = harga;
    }


    public int getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public String getKategori() {
        return kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    @Override
    public String toString() {
        return "Produk [" +
               "ID=" + id +
               ", SKU='" + sku + '\'' +
               ", Nama='" + namaProduk + '\'' +
               ", Kategori='" + kategori + '\'' +
               ", Harga=" + String.format("Rp%,.2f", harga) +
               ", Stok DB=" + stok + 
               ']';
    }
}
