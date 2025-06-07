// src/UI/dashboard2.java
package UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import services.ProductServices; // Still needed for CartController and ProductServices constructor within DashboardController
import controllers.DashboardController; // Import the new controller
import models.CartItem;
import models.Produk;
import controllers.CartController; // Import CartController

public class dashboard2 extends javax.swing.JFrame {

    private JPanel panelProduk;
    private JScrollPane scrollPane;
    private final List<CartItem> keranjang = new ArrayList<>();
    private final DashboardController dashboardController; // Use the controller
    private JTextField searchField;
    private ProductServices productServices; // Keep a reference for CartController initialization

    public dashboard2() {
        this.productServices = new ProductServices(new services.SqlServices()); // Initialize here to pass to CartController
        this.dashboardController = new DashboardController(keranjang); // Pass the cart to the dashboard controller
        // Set the callback for add to cart status
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

        btnKeranjang.addActionListener(e -> {
            // Pass the cart items and productServices to the CartController
            CartController cartController = new CartController(keranjang, productServices);
            keranjang cartDialog = new keranjang(this, cartController); // Pass the controller
            cartDialog.setVisible(true);

            tampilkanProduk(searchField.getText()); // Refresh product list after cart interaction
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
        // Use the controller to get products
        List<Produk> produkList = dashboardController.getAllProducts();

        if (produkList == null) {
            JOptionPane.showMessageDialog(this, "Gagal memuat produk dari database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (keyword != null && !keyword.isEmpty()) {
            produkList = produkList.stream()
                .filter(p -> p.getNamaProduk().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        }

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
                        // Delegate to controller
                        dashboardController.addItemToCart(p.getId(), quantity);
                        // The callback handleAddToCartStatus will be called by the controller
                        tampilkanProduk(searchField.getText()); // Refresh view after adding
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

        panelProduk.revalidate();
        panelProduk.repaint();
    }

    // This method remains in the View as it's purely for UI feedback
    private void handleAddToCartStatus(int status) {
        String productName = ""; // In a real app, you might pass the product name back from the controller
        // or re-fetch product details if needed. For simplicity, we'll keep it generic here.
        // For accurate product name feedback, DashboardController.addItemToCart
        // could return the product name along with the status.

        switch (status) {
            case 0:
                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "Error: Produk tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Error: Stok tidak mencukupi.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(this, "Error: Terjadi kesalahan pada database.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 4:
                JOptionPane.showMessageDialog(this, "Error: Jumlah produk harus lebih dari 0.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Error: Terjadi kesalahan tidak diketahui.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelSaldo = new javax.swing.JLabel();
        jLabelInfoSaldo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldCari = new javax.swing.JTextField();
        jButtonCari = new javax.swing.JButton();
        jButtonKeranjang = new javax.swing.JButton();
        jPanelProduk = new javax.swing.JPanel();
        jButtonTambah = new javax.swing.JButton();
        jLabelNamaProduk = new javax.swing.JLabel();
        jLabelHarga = new javax.swing.JLabel();
        jLabelInfoHarga = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabelSaldo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSaldo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSaldo.setText("Saldo ");

        jLabelInfoSaldo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelInfoSaldo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInfoSaldo.setText("100000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelInfoSaldo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelSaldo)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelSaldo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInfoSaldo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 51, 255));

        jButtonCari.setText("Cari");
        jButtonCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCariActionPerformed(evt);
            }
        });

        jButtonKeranjang.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        jButtonKeranjang.setText("keranjang");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextFieldCari, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCari, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCari))))
                .addContainerGap())
        );

        jPanelProduk.setBackground(new java.awt.Color(255, 255, 255));

        jButtonTambah.setBackground(new java.awt.Color(51, 102, 255));
        jButtonTambah.setFont(new java.awt.Font("Segoe UI", 0, 8));
        jButtonTambah.setForeground(new java.awt.Color(255, 255, 255));
        jButtonTambah.setText(" tambah");

        jLabelNamaProduk.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabelNamaProduk.setText("Risol Mayo");

        jLabelHarga.setText("Harga : Rp.");

        jLabelInfoHarga.setText("500");

        javax.swing.GroupLayout jPanelProdukLayout = new javax.swing.GroupLayout(jPanelProduk);
        jPanelProduk.setLayout(jPanelProdukLayout);
        jPanelProdukLayout.setHorizontalGroup(
            jPanelProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProdukLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNamaProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelHarga)
                .addGap(10, 10, 10)
                .addComponent(jLabelInfoHarga)
                .addGap(18, 18, 18)
                .addComponent(jButtonTambah)
                .addContainerGap())
        );
        jPanelProdukLayout.setVerticalGroup(
            jPanelProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProdukLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabelNamaProduk)
                    .addComponent(jLabelHarga)
                    .addComponent(jLabelInfoHarga))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelProduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(193, Short.MAX_VALUE))
        );

        pack();
    }


    private void jButtonCariActionPerformed(java.awt.event.ActionEvent evt) {
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
            java.util.logging.Logger.getLogger(dashboard2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new dashboard2().setVisible(true);
        });
    }


    private javax.swing.JButton jButtonCari;
    private javax.swing.JButton jButtonKeranjang;
    private javax.swing.JButton jButtonTambah;
    private javax.swing.JLabel jLabelHarga;
    private javax.swing.JLabel jLabelInfoHarga;
    private javax.swing.JLabel jLabelInfoSaldo;
    private javax.swing.JLabel jLabelNamaProduk;
    private javax.swing.JLabel jLabelSaldo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelProduk;
    private javax.swing.JTextField jTextFieldCari;
}