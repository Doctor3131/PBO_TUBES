package models;
 
public class CartItem {
    private Produk produk;
    private int quantity;

    public CartItem(Produk produk, int quantity) {
        this.produk = produk;
        this.quantity = quantity;
    }

    public Produk getProduk() {
        return produk;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return produk.getHarga() * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
               "produk=" + (produk != null ? produk.getNamaProduk() : "N/A") +
               ", quantity=" + quantity +
               ", subtotal=" + String.format("Rp%,.2f", getSubtotal()) +
               '}';
    }
}


