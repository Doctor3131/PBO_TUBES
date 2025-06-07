package controllers;

import services.ProductServices;
import java.util.List;

import models.CartItem;
import models.Transaksi;

public class CartController {
    private final ProductServices productServices;
    private final List<CartItem> cartItems;

    public CartController(List<CartItem> cartItems, ProductServices productServices) {
        this.cartItems = cartItems;
        this.productServices = productServices;
    }

    public boolean clearCart() {
        return productServices.cancelCart(cartItems);
    }

    public Transaksi performCheckout() {
        return productServices.processCheckout(cartItems);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}