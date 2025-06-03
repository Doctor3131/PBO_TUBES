package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Produk;
import model.Transaksi;
import services.ProductServices;
import services.SqlServices;

public class MainFrame extends JFrame {

    private final ProductServices productServices;
    private final List<CartItem> keranjang = new ArrayList<>();
    private final List<Transaksi> daftarTransaksi = new ArrayList<>();

    private final JTable productTable;
    private final DefaultTableModel tableModel;

    public MainFrame() {
        SqlServices sqlServices = new SqlServices();
        productServices = new ProductServices(sqlServices);

        setTitle("Aplikasi Manajemen Produk");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Product table
        String[] columnNames = {"ID", "SKU", "Nama Produk", "Kategori", "Deskripsi", "Harga", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addToCartButton = new JButton("Tambah ke Keranjang");
        JButton viewCartButton = new JButton("Lihat Keranjang");
        JButton checkoutButton = new JButton("Checkout");
        JButton cancelCartButton = new JButton("Batalkan Keranjang");
        JButton historyButton = new JButton("Riwayat Transaksi");

        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(cancelCartButton);
        buttonPanel.add(historyButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add listeners to buttons
        addToCartButton.addActionListener(e -> tambahProdukKeKeranjangUI());
        viewCartButton.addActionListener(e -> lihatKeranjangUI());
        checkoutButton.addActionListener(e -> checkoutUI());
        cancelCartButton.addActionListener(e -> batalkanKeranjangUI());
        historyButton.addActionListener(e -> lihatRiwayatTransaksiUI());


        add(mainPanel);
        
        // Load initial data
        tampilkanSemuaProduk();
    }

    private void tampilkanSemuaProduk() {
        List<Produk> produkList = productServices.getAllProducts();
        tableModel.setRowCount(0); // Clear existing data

        if (produkList != null && !produkList.isEmpty()) {
            for (Produk produk : produkList) {
                Object[] row = {
                    produk.getId(),
                    produk.getSku(),
                    produk.getNamaProduk(),
                    produk.getKategori(),
                    produk.getDeskripsi(),
                    String.format("Rp%,.2f", produk.getHarga()),
                    produk.getStok()
                };
                tableModel.addRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada produk yang tersedia atau gagal mengambil data.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void tambahProdukKeKeranjangUI() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String quantityStr = JOptionPane.showInputDialog(this, "Masukkan Jumlah:", "Tambah ke Keranjang", JOptionPane.PLAIN_MESSAGE);

        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                int status = productServices.addItemToCart(keranjang, productId, quantity);

                switch (status) {
                    case 0:
                        JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan ke keranjang.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        tampilkanSemuaProduk(); // Refresh table to show updated stock
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
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Input tidak valid. Harap masukkan angka.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void lihatKeranjangUI() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang Anda masih kosong.", "Keranjang Belanja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog cartDialog = new JDialog(this, "Isi Keranjang Belanja", true);
        cartDialog.setSize(600, 400);
        cartDialog.setLocationRelativeTo(this);

        String[] columns = {"ID", "Nama Produk", "Kuantitas", "Harga Satuan", "Subtotal"};
        DefaultTableModel cartTableModel = new DefaultTableModel(columns, 0);
        JTable cartTable = new JTable(cartTableModel);
        
        double totalBelanja = 0;
        for (CartItem item : keranjang) {
            Produk produk = item.getProduk();
            Object[] row = {
                produk.getId(),
                produk.getNamaProduk(),
                item.getQuantity(),
                String.format("Rp%,.2f", produk.getHarga()),
                String.format("Rp%,.2f", item.getSubtotal())
            };
            cartTableModel.addRow(row);
            totalBelanja += item.getSubtotal();
        }

        JScrollPane scrollPane = new JScrollPane(cartTable);
        JLabel totalLabel = new JLabel(String.format("TOTAL KESELURUHAN: Rp%,.2f", totalBelanja));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        cartDialog.add(scrollPane, BorderLayout.CENTER);
        cartDialog.add(totalLabel, BorderLayout.SOUTH);
        cartDialog.setVisible(true);
    }

    private void batalkanKeranjangUI() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang sudah kosong.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin membatalkan keranjang?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean isSuccess = productServices.cancelCart(keranjang);
            if (isSuccess) {
                keranjang.clear();
                tampilkanSemuaProduk();
                JOptionPane.showMessageDialog(this, "Keranjang berhasil dibatalkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membatalkan keranjang.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void checkoutUI() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong, tidak bisa checkout.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Transaksi transaksiBaru = productServices.processCheckout(keranjang);
        if (transaksiBaru != null) {
            daftarTransaksi.add(transaksiBaru);
            keranjang.clear();
            tampilkanSemuaProduk();
            
            JTextArea transactionDetails = new JTextArea(transaksiBaru.toString());
            transactionDetails.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(transactionDetails);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Checkout Berhasil!", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this, "Gagal melakukan checkout.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void lihatRiwayatTransaksiUI() {
        if (daftarTransaksi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada riwayat transaksi.", "Riwayat Transaksi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JDialog historyDialog = new JDialog(this, "Riwayat Transaksi", true);
        historyDialog.setSize(600, 400);
        historyDialog.setLocationRelativeTo(this);

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        for(Transaksi trx : daftarTransaksi) {
            historyArea.append(trx.toString() + "\n--- \n");
        }

        historyDialog.add(new JScrollPane(historyArea));
        historyDialog.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}