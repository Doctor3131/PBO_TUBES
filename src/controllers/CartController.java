package controllers;

import services.SqlServices; // Now directly importing SqlServices
import java.util.List;

import models.CartItem;
import models.Transaksi;

public class CartController {
    private final SqlServices sqlServices; // Direct dependency on SqlServices
    private final List<CartItem> cartItems;

    // Constructor now takes SqlServices directly
    public CartController(List<CartItem> cartItems, SqlServices sqlServices) {
        this.cartItems = cartItems;
        this.sqlServices = sqlServices;
    }

    // Logic for clearing cart moved from ProductServices
    public boolean clearCart() {
        if (cartItems == null || cartItems.isEmpty()) {
            return true;
        }
        boolean isSuccess = sqlServices.revertStockForCartItems(cartItems); // Directly call SqlServices
        if (isSuccess) {
            cartItems.clear(); // Clear local list after successful operation
        }
        return isSuccess;
    }

    // Logic for performing checkout moved from ProductServices
    public Transaksi performCheckout() {
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }
        Transaksi newTransaction = new Transaksi(cartItems); // Instantiate Transaksi directly
        cartItems.clear(); // Clear local list after successful checkout
        return newTransaction;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // New method for handling exit, similar to cancelCart but for application exit
    public boolean handleExit() {
        if (cartItems != null && !cartItems.isEmpty()) {
            boolean revertSuccess = sqlServices.revertStockForCartItems(cartItems); // Directly call SqlServices
            if (revertSuccess) {
                cartItems.clear();
            }
            return revertSuccess;
        }
        return true;
    }
}