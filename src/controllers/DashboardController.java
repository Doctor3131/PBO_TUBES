package controllers;

import services.ProductServices;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import models.CartItem;
import models.Produk;

public class DashboardController {
    private final ProductServices productServices;
    private final List<CartItem> currentCart;
    // Callback now includes a message for better UI feedback
    private Consumer<String> addToCartStatusCallback; 

    public DashboardController(List<CartItem> currentCart) {
        this.productServices = new ProductServices();
        this.currentCart = currentCart;
    }

    // Setter for the callback
    public void setAddToCartStatusCallback(Consumer<String> callback) {
        this.addToCartStatusCallback = callback;
    }

    public List<Produk> getAllProducts() {
        return productServices.getAllProducts();
    }

    // New method to handle filtering
    public List<Produk> getFilteredProducts(String keyword) {
        List<Produk> allProducts = productServices.getAllProducts();
        if (allProducts == null) {
            return null; // Handle database errors
        }

        if (keyword != null && !keyword.isEmpty()) {
            return allProducts.stream()
                .filter(p -> p.getNamaProduk().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        }
        return allProducts;
    }

    public void addItemToCart(int productId, int quantity) {
        int status = productServices.addItemToCart(currentCart, productId, quantity);
        String message = "";
        switch (status) {
            case 0:
                // Optionally retrieve product name for a more specific success message
                Produk addedProduct = productServices.getAllProducts().stream()
                                    .filter(p -> p.getId() == productId)
                                    .findFirst()
                                    .orElse(null);
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