package model; 
public class ProductModel {
    private int id;
    private String sku;
    private String namaProduk;
    private String kategori;
    private String deskripsi;
    private double harga;
    private int stok;

    public ProductModel(int id, String sku, String namaProduk, String kategori, String deskripsi, double harga, int stok) {
        this.id = id;
        this.sku = sku;
        this.namaProduk = namaProduk;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
    }

    public ProductModel(int id, String nama, double harga) {
        this.id = id;
        this.namaProduk = nama;
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
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
                ", Harga=" + harga +
                ", Stok=" + stok +
                ']';
    }
}