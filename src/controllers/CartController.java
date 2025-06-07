package controllers;

import services.SqlServices; 
import java.util.List;
import models.CartItem;
import models.Transaksi;

public class CartController {
    private final SqlServices sqlServices; 
    private final List<CartItem> cartItems;

    public CartController(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.sqlServices = new SqlServices();
    }

    public boolean clearCart() {
        if (cartItems == null || cartItems.isEmpty()) {
            return true;
        }
        boolean isSuccess = sqlServices.revertStockForCartItems(cartItems); 
        if (isSuccess) {
            cartItems.clear(); 
        }
        return isSuccess;
    }

    public Transaksi performCheckout() {
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }
        Transaksi newTransaction = new Transaksi(cartItems); 
        cartItems.clear(); 
        return newTransaction;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public boolean handleExit() {
        if (cartItems != null && !cartItems.isEmpty()) {
            boolean revertSuccess = sqlServices.revertStockForCartItems(cartItems);
            if (revertSuccess) {
                cartItems.clear();
            }
            return revertSuccess;
        }
        return true;
    }
}