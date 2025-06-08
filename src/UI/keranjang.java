package UI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.KeranjangController;
import models.CartItem;
import models.Produk;
import models.Transaksi;

public class Keranjang extends javax.swing.JDialog {
    private final KeranjangController keranjangController;
    private JPanel panelIsi;
    private JLabel totalLabel;

    public Keranjang(Frame parent, KeranjangController keranjangController) {
        super(parent, "Keranjang Belanja", true);
        this.keranjangController = keranjangController;

        setTitle("Keranjang Belanja");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        initComponents1();
        tampilkanIsiKeranjang();
    }

    private void initComponents1() {
        setLayout(new BorderLayout());

        JPanel panelAtas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAtas.setBackground(new Color(240, 240, 240));
        panelAtas.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JButton btnHapus = new JButton("Kosongkan Keranjang");
        btnHapus.addActionListener(e -> clearCart());

        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> this.dispose());

        panelAtas.add(btnHapus);
        panelAtas.add(btnKembali);

        add(panelAtas, BorderLayout.NORTH);

        panelIsi = new JPanel();
        panelIsi.setLayout(new BoxLayout(panelIsi, BoxLayout.Y_AXIS));
        panelIsi.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(panelIsi);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(new Color(0, 83, 154));
        panelBawah.setPreferredSize(new Dimension(0, 50));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        totalLabel = new JLabel("Total : Rp0.00");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckout.addActionListener(e -> performCheckout());

        panelBawah.add(totalLabel, BorderLayout.CENTER);
        panelBawah.add(btnCheckout, BorderLayout.EAST);

        add(panelBawah, BorderLayout.SOUTH);
    }

    private void tampilkanIsiKeranjang() {
        panelIsi.removeAll();
        double total = 0;
        List<CartItem> cartItems = keranjangController.getCartItems();

        if (cartItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Keranjang Anda kosong.");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            panelIsi.setLayout(new BorderLayout());
            panelIsi.add(emptyLabel, BorderLayout.CENTER);
        } else {
            panelIsi.setLayout(new BoxLayout(panelIsi, BoxLayout.Y_AXIS));
            for (CartItem item : cartItems) {
                Produk p = item.getProduk();
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10))
                );
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                JLabel nama = new JLabel(p.getNamaProduk());
                nama.setFont(new Font("Segoe UI", Font.BOLD, 14));

                String detail = String.format("%d pcs @ Rp%,.2f", item.getQuantity(), p.getHarga());
                JLabel harga = new JLabel(detail);
                harga.setFont(new Font("Segoe UI", Font.PLAIN, 12));

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false);
                infoPanel.add(nama);
                infoPanel.add(harga);

                JLabel subtotal = new JLabel(String.format("Rp%,.2f", item.getSubtotal()));
                subtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));

                panel.add(infoPanel, BorderLayout.CENTER);
                panel.add(subtotal, BorderLayout.EAST);

                panelIsi.add(panel);
                total += item.getSubtotal();
            }
        }

        totalLabel.setText(String.format("Total : Rp%,.2f", total));
        panelIsi.revalidate();
        panelIsi.repaint();
    }

    private void clearCart() {
        List<CartItem> cartItems = keranjangController.getCartItems();
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang sudah kosong.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin mengosongkan keranjang? Stok akan dikembalikan.",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean isSuccess = keranjangController.clearCart();
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Keranjang berhasil dikosongkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengembalikan stok. Keranjang tidak dapat dikosongkan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performCheckout() {
        List<CartItem> cartItems = keranjangController.getCartItems();
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong, tidak bisa checkout.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Transaksi transaksiBaru = keranjangController.performCheckout();
        if (transaksiBaru != null) {
            JTextArea transactionDetails = new JTextArea(transaksiBaru.toString());
            transactionDetails.setEditable(false);
            transactionDetails.setBackground(this.getBackground());
            JScrollPane scrollPane = new JScrollPane(transactionDetails);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Checkout Berhasil!", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal melakukan checkout.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Keranjang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            services.SqlServices sqlServices = new services.SqlServices();
            java.util.List<CartItem> testCart = new java.util.ArrayList<>();

            models.Produk sampleProduct1 = sqlServices.getProductById(1);
            if (sampleProduct1 != null) {
                testCart.add(new CartItem(sampleProduct1, 2));
            }

            models.Produk sampleProduct2 = sqlServices.getProductById(3);
            if (sampleProduct2 != null) {
                testCart.add(new CartItem(sampleProduct2, 1));
            }
        });
    }

}