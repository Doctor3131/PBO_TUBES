package controllers;

import java.util.ArrayList;
import java.util.List;
import models.CartItem;
import models.Transaksi;
import services.SqlServices;

public class KeranjangController {
    private final List<CartItem> cartItems;
    private final SqlServices sqlServices;

    public KeranjangController(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.sqlServices = new SqlServices();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public boolean clearCart() {
        if (cartItems.isEmpty()) {
            return true; 
        }
        boolean success = sqlServices.revertStockForCartItems(cartItems);
        if (success) {
            cartItems.clear(); 
        }
        return success;
    }

    public Transaksi performCheckout() {
        if (cartItems.isEmpty()) {
            return null;
        }

        Transaksi transaksi = new Transaksi(new ArrayList<>(cartItems));

        cartItems.clear();

        return transaksi;
    }
}