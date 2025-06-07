// src/UI/keranjang.java
package UI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import services.ProductServices; 
import controllers.CartController; // Import the new controller
import models.CartItem;
import models.Produk;
import models.Transaksi;

public class keranjang extends javax.swing.JDialog {
    private final CartController cartController; // Use the controller
    private JPanel panelIsi;
    private JLabel totalLabel;

    // Constructor now takes CartController
    public keranjang(Frame parent, CartController cartController) {
        super(parent, "Keranjang Belanja", true);
        this.cartController = cartController;

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
        List<CartItem> cartItems = cartController.getCartItems(); // Get cart items from controller

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
        List<CartItem> cartItems = cartController.getCartItems(); // Get cart items for check
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang sudah kosong.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin mengosongkan keranjang? Stok akan dikembalikan.",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean isSuccess = cartController.clearCart(); // Delegate to controller
            if (isSuccess) {
                cartItems.clear(); // Clear local list after successful operation
                JOptionPane.showMessageDialog(this, "Keranjang berhasil dikosongkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close the dialog
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengembalikan stok. Keranjang tidak dapat dikosongkan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performCheckout() {
        List<CartItem> cartItems = cartController.getCartItems(); // Get cart items for check
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong, tidak bisa checkout.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Transaksi transaksiBaru = cartController.performCheckout(); // Delegate to controller
        if (transaksiBaru != null) {
            cartItems.clear(); // Clear local list after successful checkout

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


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelSaldo = new javax.swing.JLabel();
        jLabelInfoSaldo = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelTotal = new javax.swing.JLabel();
        jLabelInfoTotal = new javax.swing.JLabel();
        jButtonCheckout = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelNamaProduk = new javax.swing.JLabel();
        jLabelNamaProduk1 = new javax.swing.JLabel();
        jLabelNamaProduk2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 83, 154));
        jPanel1.setPreferredSize(new java.awt.Dimension(390, 37));

        jLabelSaldo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelSaldo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSaldo.setText("Saldo :");

        jLabelInfoSaldo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelInfoSaldo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInfoSaldo.setText("100000");

        jButton1.setText("kembali");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("hapus ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelSaldo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInfoSaldo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelSaldo)
                        .addComponent(jLabelInfoSaldo))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 83, 154));
        jPanel2.setPreferredSize(new java.awt.Dimension(390, 37));

        jLabelTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotal.setText("Total :");

        jLabelInfoTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelInfoTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInfoTotal.setText("100000");

        jButtonCheckout.setText("Checkout");
        jButtonCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabelTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelInfoTotal)
                .addGap(18, 18, 18)
                .addComponent(jButtonCheckout)
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelInfoTotal)
                        .addComponent(jLabelTotal))
                    .addComponent(jButtonCheckout))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabelNamaProduk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNamaProduk.setText("Risol Mayo");

        jLabelNamaProduk1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNamaProduk1.setText("500");

        jLabelNamaProduk2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNamaProduk2.setText("Rp.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabelNamaProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelNamaProduk2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelNamaProduk1)
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNamaProduk)
                    .addComponent(jLabelNamaProduk1)
                    .addComponent(jLabelNamaProduk2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButtonCheckoutActionPerformed(java.awt.event.ActionEvent evt) {
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
            java.util.logging.Logger.getLogger(keranjang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            services.SqlServices sqlServices = new services.SqlServices();
            services.ProductServices productServices = new services.ProductServices(sqlServices);

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

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonCheckout;
    private javax.swing.JLabel jLabelInfoSaldo;
    private javax.swing.JLabel jLabelInfoTotal;
    private javax.swing.JLabel jLabelNamaProduk;
    private javax.swing.JLabel jLabelNamaProduk1;
    private javax.swing.JLabel jLabelNamaProduk2;
    private javax.swing.JLabel jLabelSaldo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
}