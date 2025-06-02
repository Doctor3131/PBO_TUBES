/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkout;

import java.util.List;
import model.CartItem;
import model.Transaksi;
import database.account;
/**
 *
 * @author Taki
 */


public class CheckoutService {
    /* ATRIBUT */
    private List<CartItem> products;
    private CheckoutView C_View;
    private Transaksi transaksi;
    private account cust;
    private boolean status;

    /* METHOD */
    // KONSTRUKTOR
    public CheckoutService() {
        this.products = transaksi.getItems();
        C_View.showDataTransaction(products);
    }

    // GETTER
    // SETTER
    public void setStatusSuccess() {
        this.status = true;
    }

    public void setStatusFailed() {
        this.status = false;
    }

    // METHOD LAIN
    public void pay() {
        /* KAMUS */
        int money;
        double totalBill;

        /* ALGORITMA */
        money = cust.getSaldo();
        totalBill = transaksi.getTotalBelanja();

        if (money >= totalBill) {
            money -= totalBill;
            cust.setSaldo(money);
            // transaksi.clearCart();
            setStatusSuccess();
        } else {
            setStatusFailed();
        }

        C_View.showCheckoutStatus(this.status);
    }

}

