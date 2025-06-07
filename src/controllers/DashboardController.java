package controllers;

import services.SqlServices; // Now directly importing SqlServices
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import models.CartItem;
import models.Produk;

public class DashboardController {
    private final SqlServices sqlServices; // Direct dependency on SqlServices
    private final List<CartItem> currentCart;
    private Consumer<String> addToCartStatusCallback;

    public DashboardController(List<CartItem> currentCart) {
        this.sqlServices = new SqlServices(); // Initialize SqlServices directly
        this.currentCart = currentCart;
    }

    // Setter for the callback
    public void setAddToCartStatusCallback(Consumer<String> callback) {
        this.addToCartStatusCallback = callback;
    }

    public List<Produk> getAllProducts() {
        return sqlServices.getAllProducts(); // Directly call SqlServices
    }

    // New method to handle filtering
    public List<Produk> getFilteredProducts(String keyword) {
        List<Produk> allProducts = sqlServices.getAllProducts(); // Directly call SqlServices
        if (allProducts == null) {
            return null;
        }

        if (keyword != null && !keyword.isEmpty()) {
            return allProducts.stream()
                .filter(p -> p.getNamaProduk().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        }
        return allProducts;
    }

    // Logic for adding item to cart moved from ProductServices
    public void addItemToCart(int productId, int quantity) {
        int status = 0; // 0: success, 1: product not found, 2: insufficient stock, 3: DB error, 4: invalid quantity
        String message = "";

        if (quantity <= 0) {
            status = 4;
        } else {
            Produk produkDariDB = sqlServices.getProductById(productId);
            if (produkDariDB == null) {
                status = 1;
            } else if (produkDariDB.getStok() < quantity) {
                status = 2;
            } else {
                int stockUpdateStatus = sqlServices.reduceProductStock(productId, quantity);
                if (stockUpdateStatus != 0) {
                    if (stockUpdateStatus == 1) status = 1; // Product not found during stock update (shouldn't happen here)
                    if (stockUpdateStatus == 2) status = 2; // Insufficient stock during stock update (race condition)
                    if (stockUpdateStatus == -1) status = 3; // Database error
                } else {
                    boolean itemFoundInCart = false;
                    for (CartItem item : currentCart) {
                        if (item.getProduk().getId() == productId) {
                            item.setQuantity(item.getQuantity() + quantity);
                            itemFoundInCart = true;
                            break;
                        }
                    }

                    if (!itemFoundInCart) {
                        // Create a new Produk object for the cart based on the current state from DB
                        // (Note: stock in cart item would be the quantity added, not DB stock)
                        Produk productForCart = new Produk(produkDariDB.getId(), produkDariDB.getSku(), produkDariDB.getNamaProduk(),
                                                           produkDariDB.getKategori(), produkDariDB.getDeskripsi(),
                                                           produkDariDB.getHarga(), produkDariDB.getStok()); // Stok here is current DB stock, not what's in cart
                        currentCart.add(new CartItem(productForCart, quantity));
                    }
                    status = 0; // Success
                }
            }
        }

        switch (status) {
            case 0:
                Produk addedProduct = sqlServices.getProductById(productId); // Re-fetch for updated product name
                message = "Produk " + (addedProduct != null ? addedProduct.getNamaProduk() : "dengan ID " + productId) + " berhasil ditambahkan.";
                break;
            case 1:
                message = "Error: Produk dengan ID " + productId + " tidak ditemukan.";
                break;
            case 2:
                message = "Error: Stok tidak mencukupi untuk produk ID " + productId + ".";
                break;
            case 3:
                message = "Error: Terjadi kesalahan pada database saat memperbarui stok produk ID " + productId + ".";
                break;
            case 4:
                message = "Error: Jumlah produk harus lebih dari 0.";
                break;
            default:
                message = "Error: Terjadi kesalahan tidak diketahui saat menambahkan produk ID " + productId + ".";
                break;
        }
        if (addToCartStatusCallback != null) {
            addToCartStatusCallback.accept(message);
        }
    }
}