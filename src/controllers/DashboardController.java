package controllers;

import services.ProductServices;
import services.SqlServices; // Needed for ProductServices constructor
import java.util.List;
import java.util.function.Consumer; // For callback to update UI

import models.CartItem;
import models.Produk;

public class DashboardController {
    private final ProductServices productServices;
    private final List<CartItem> currentCart;
    private Consumer<Integer> addToCartStatusCallback;

    public DashboardController(List<CartItem> currentCart) {
        this.productServices = new ProductServices();
        this.currentCart = currentCart;
    }

    // Setter for the callback
    public void setAddToCartStatusCallback(Consumer<Integer> callback) {
        this.addToCartStatusCallback = callback;
    }

    public List<Produk> getAllProducts() {
        return productServices.getAllProducts();
    }

    public void addItemToCart(int productId, int quantity) {
        int status = productServices.addItemToCart(currentCart, productId, quantity);
        if (addToCartStatusCallback != null) {
            addToCartStatusCallback.accept(status);
        }
    }

}