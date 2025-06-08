package UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import controllers.DashboardController;
import controllers.KeranjangController;
import models.CartItem;
import models.Produk;

public class Dashboard extends javax.swing.JFrame {

    private final JPanel panelProduk;
    private final JScrollPane scrollPane;
    private final List<CartItem> listItem = new ArrayList<>();
    private final DashboardController dashboardController;
    private final KeranjangController keranjangController;
    private final JTextField searchField;
    private final JButton btnCari;
    private final JButton btnKeranjang;
    private final List<JButton> addToCartButtons = new ArrayList<>();

    public Dashboard() {
        this.dashboardController = new DashboardController(listItem);
        this.keranjangController = new KeranjangController(listItem);

        dashboardController.setAddToCartStatusCallback(message -> {
            SwingUtilities.invokeLater(() -> handleAddToCartStatus(message));
        });

        setTitle("Dashboard Produk");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.searchField = new JTextField(15);
        this.btnCari = new JButton("Cari");
        this.btnKeranjang = new JButton("Lihat Keranjang");
        this.panelProduk = new JPanel();
        this.scrollPane = new JScrollPane(panelProduk);

        initDashboardLayout();
        tampilkanProduk(""); 
    }

    private void setUIEnabled(boolean enabled) {
        searchField.setEnabled(enabled);
        btnCari.setEnabled(enabled);
        btnKeranjang.setEnabled(enabled);
        for (JButton btn : addToCartButtons) {
            btn.setEnabled(enabled);
        }
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

        btnCari.addActionListener(e -> tampilkanProduk(searchField.getText()));

        btnKeranjang.addActionListener(e -> {
            new Keranjang(this, keranjangController).setVisible(true);
            tampilkanProduk(searchField.getText());
        });

        tombolPanel.add(new JLabel("Cari Produk:"));
        tombolPanel.add(searchField);
        tombolPanel.add(btnCari);
        tombolPanel.add(btnKeranjang);

        panelAtas.add(tombolPanel, BorderLayout.EAST);
        add(panelAtas, BorderLayout.NORTH);

        panelProduk.setLayout(new BoxLayout(panelProduk, BoxLayout.Y_AXIS));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void tampilkanProduk(String keyword) {
        setUIEnabled(false);
        panelProduk.removeAll();
        panelProduk.setLayout(new BorderLayout());
        JLabel loadingLabel = new JLabel("Memuat produk...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        panelProduk.add(loadingLabel, BorderLayout.CENTER);
        panelProduk.revalidate();
        panelProduk.repaint();

        SwingWorker<List<Produk>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Produk> doInBackground() throws Exception {
                return dashboardController.getFilteredProducts(keyword);
            }

            @Override
            protected void done() {
                try {
                    List<Produk> produkList = get();
                    populateProdukPanel(produkList, keyword);
                } catch (InterruptedException | ExecutionException e) {
                    panelProduk.removeAll();
                    JLabel errorLabel = new JLabel("Gagal memuat produk. Periksa koneksi database Anda.");
                    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    errorLabel.setForeground(Color.RED);
                    panelProduk.add(errorLabel, BorderLayout.CENTER);
                    JOptionPane.showMessageDialog(Dashboard.this, "Gagal memuat produk: " + e.getCause().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    setUIEnabled(true);
                    panelProduk.revalidate();
                    panelProduk.repaint();
                }
            }
        };
        worker.execute();
    }

    private void populateProdukPanel(List<Produk> produkList, String keyword) {
        panelProduk.removeAll();
        addToCartButtons.clear();

        if (produkList == null) {
            JOptionPane.showMessageDialog(this, "Gagal memuat produk dari database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (produkList.isEmpty()) {
            String message = keyword.isEmpty() ? "Tidak ada produk tersedia saat ini." : "Tidak ada produk ditemukan untuk '" + keyword + "'.";
            JLabel emptyLabel = new JLabel(message);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            panelProduk.setLayout(new BorderLayout());
            panelProduk.add(emptyLabel, BorderLayout.CENTER);
        } else {
            panelProduk.setLayout(new BoxLayout(panelProduk, BoxLayout.Y_AXIS));
            for (Produk p : produkList) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
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
                             if (quantity <= 0) {
                                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            
                            SwingWorker<Void, Void> cartWorker = new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() throws Exception {
                                    dashboardController.addItemToCart(p.getId(), quantity);
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    tampilkanProduk(searchField.getText());
                                }
                            };
                            setUIEnabled(false); 
                            cartWorker.execute();

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                addToCartButtons.add(btnTambah);

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