package UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import controllers.DashboardController;
import controllers.KeranjangController;
import models.CartItem;
import models.Produk;

public class Dashboard extends javax.swing.JFrame {

    private JPanel panelProduk;
    private JScrollPane scrollPane;
    private final List<CartItem> listItem = new ArrayList<>();
    private final DashboardController dashboardController;
    private JTextField searchField;
    private KeranjangController keranjangController;

    public Dashboard() {
        this.dashboardController = new DashboardController(listItem);
        // Initialize the KeranjangController with the shared list of cart items
        this.keranjangController = new KeranjangController(listItem);
        dashboardController.setAddToCartStatusCallback(this::handleAddToCartStatus);

        setTitle("Dashboard Produk");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initDashboardLayout();
        tampilkanProduk("");
    }

    private void initDashboardLayout() {
        setLayout(new BorderLayout());

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBackground(new Color(0, 83, 154));
        panelAtas.setPreferredSize(new Dimension(400, 50));
        panelAtas.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel titleLabel = new JLabel("Siriel Shop");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelAtas.add(titleLabel, BorderLayout.WEST);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tombolPanel.setOpaque(false);
        searchField = new JTextField(15);
        JButton btnCari = new JButton("Cari");
        JButton btnKeranjang = new JButton("Lihat Keranjang");

        btnCari.addActionListener(e -> tampilkanProduk(searchField.getText()));

        // When the cart button is clicked, create and show a new Keranjang dialog
        btnKeranjang.addActionListener(e -> {
            new Keranjang(this, keranjangController).setVisible(true);
            // Refresh the product list in case stock changed
            tampilkanProduk(searchField.getText());
        });

        tombolPanel.add(new JLabel("Cari Produk:"));
        tombolPanel.add(searchField);
        tombolPanel.add(btnCari);
        tombolPanel.add(btnKeranjang);

        panelAtas.add(tombolPanel, BorderLayout.EAST);
        add(panelAtas, BorderLayout.NORTH);

        panelProduk = new JPanel();
        panelProduk.setLayout(new BoxLayout(panelProduk, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(panelProduk);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void tampilkanProduk(String keyword) {
        panelProduk.removeAll();
        List<Produk> produkList = dashboardController.getFilteredProducts(keyword);

        if (produkList == null) {
            JOptionPane.showMessageDialog(this, "Gagal memuat produk dari database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (produkList.isEmpty() && !keyword.isEmpty()) {
            JLabel noResultsLabel = new JLabel("Tidak ada produk ditemukan untuk '" + keyword + "'.");
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noResultsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noResultsLabel.setForeground(Color.GRAY);
            panelProduk.setLayout(new BorderLayout());
            panelProduk.add(noResultsLabel, BorderLayout.CENTER);
        } else if (produkList.isEmpty()) {
            JLabel emptyLabel = new JLabel("Tidak ada produk tersedia saat ini.");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            panelProduk.setLayout(new BorderLayout());
            panelProduk.add(emptyLabel, BorderLayout.CENTER);
        } else {
            panelProduk.setLayout(new BoxLayout(panelProduk, BoxLayout.Y_AXIS));
            for (Produk p : produkList) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10))
                );
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
                panel.setBackground(Color.WHITE);

                JLabel nama = new JLabel(p.getNamaProduk());
                nama.setFont(new Font("Segoe UI", Font.BOLD, 16));

                JLabel harga = new JLabel(String.format("Rp%,.2f", p.getHarga()));
                harga.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JLabel stok = new JLabel("Stok: " + p.getStok());
                stok.setFont(new Font("Segoe UI", Font.ITALIC, 12));

                JButton btnTambah = new JButton("Tambah ke Keranjang");
                btnTambah.setBackground(new Color(51, 102, 255));
                btnTambah.setForeground(Color.WHITE);
                btnTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnTambah.addActionListener(e -> {
                    String quantityStr = JOptionPane.showInputDialog(this, "Masukkan Jumlah untuk " + p.getNamaProduk() + ":", "Tambah Keranjang", JOptionPane.PLAIN_MESSAGE);
                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(quantityStr);
                            dashboardController.addItemToCart(p.getId(), quantity);
                            tampilkanProduk(searchField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false);
                infoPanel.add(nama);
                infoPanel.add(stok);

                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                actionPanel.setOpaque(false);
                actionPanel.add(harga);
                actionPanel.add(btnTambah);

                panel.add(infoPanel, BorderLayout.CENTER);
                panel.add(actionPanel, BorderLayout.EAST);

                panelProduk.add(panel);
            }
        }

        panelProduk.revalidate();
        panelProduk.repaint();
    }

    private void handleAddToCartStatus(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}