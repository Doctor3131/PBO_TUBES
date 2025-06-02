package services;

import java.util.List;
import model.Produk;
import model.CartItem;
import model.Transaksi;


public class ProductServices {

    private SqlServices sqlServices;

    public ProductServices(SqlServices sqlServices) {
        this.sqlServices = sqlServices;
    }

    public List<Produk> getAllProducts() {
        return sqlServices.getAllProducts();
    }

    public int addItemToCart(List<CartItem> keranjang, int productId, int quantity) {
        if (quantity <= 0) {
            return 4; 
        }

        Produk produkDariDB = sqlServices.getProductById(productId);
        if (produkDariDB == null) {
            return 1; 
        }

        if (produkDariDB.getStok() < quantity) {
            return 2; 
        }

        int stockUpdateStatus = sqlServices.reduceProductStock(productId, quantity);
        if (stockUpdateStatus != 0) {
            if (stockUpdateStatus == 1) return 1;
            if (stockUpdateStatus == 2) return 2;
            return 3; 
        }

        boolean itemFoundInCart = false;
        for (CartItem item : keranjang) {
            if (item.getProduk().getId() == productId) {
                item.setQuantity(item.getQuantity() + quantity);
                itemFoundInCart = true;
                break;
            }
        }

        if (!itemFoundInCart) {
            Produk productForCart = new Produk(produkDariDB.getId(), produkDariDB.getSku(), produkDariDB.getNamaProduk(),
                                               produkDariDB.getKategori(), produkDariDB.getDeskripsi(),
                                               produkDariDB.getHarga(), produkDariDB.getStok());
            keranjang.add(new CartItem(productForCart, quantity));
        }
        return 0; 
    }

    public Transaksi processCheckout(List<CartItem> keranjang) {
        if (keranjang == null || keranjang.isEmpty()) {
            return null; 
        }
        return new Transaksi(keranjang);
    }

    public boolean cancelCart(List<CartItem> keranjang) {
        if (keranjang == null || keranjang.isEmpty()) {
            return true; 
        }
        return sqlServices.revertStockForCartItems(keranjang);
    }

    public boolean handleExit(List<CartItem> keranjang) {
        if (keranjang != null && !keranjang.isEmpty()) {
            return sqlServices.revertStockForCartItems(keranjang);
        }
        return true; 
    }
}
