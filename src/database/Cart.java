package database;


import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> items;
    private List<Integer> quantities;

    public Cart() {
        this.items = new ArrayList<>();
        this.quantities = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        items.add(product);
        quantities.add(quantity);
    }

    public List<Product> getItems() {
        return items;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
        return total;
    }
} 