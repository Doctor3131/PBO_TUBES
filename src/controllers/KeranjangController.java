package controllers;

import java.util.ArrayList;
import java.util.List;
import models.CartItem;
import models.Transaksi;
import services.SqlServices;

/**
 * Controller to manage shopping cart logic.
 * It connects the Keranjang UI with the database services.
 */
public class KeranjangController {
    private final List<CartItem> cartItems;
    private final SqlServices sqlServices;

    public KeranjangController(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.sqlServices = new SqlServices();
    }

    /**
     * Returns the current list of items in the cart.
     * @return A list of CartItem objects.
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    /**
     * Clears all items from the cart and reverts the product stock in the database.
     * @return true if the cart was cleared and stock was reverted successfully, false otherwise.
     */
    public boolean clearCart() {
        if (cartItems.isEmpty()) {
            return true; // Nothing to clear
        }
        // Revert stock for all items that were in the cart
        boolean success = sqlServices.revertStockForCartItems(cartItems);
        if (success) {
            cartItems.clear(); // Clear the list only if stock revert is successful
        }
        return success;
    }

    /**
     * Finalizes the purchase by creating a transaction object.
     * In a real application, this would also involve saving the transaction to the database.
     * @return A new Transaksi object if checkout is successful, or null if the cart is empty.
     */
    public Transaksi performCheckout() {
        if (cartItems.isEmpty()) {
            return null;
        }

        // Create a new transaction object with a copy of the current cart items
        Transaksi transaksi = new Transaksi(new ArrayList<>(cartItems));

        // The cart is cleared after checkout
        cartItems.clear();

        return transaksi;
    }
}