package UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class Start extends javax.swing.JFrame {

    public Start() {
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {

        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        javax.swing.JLabel logoLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel logoLabel2 = new javax.swing.JLabel();
        javax.swing.JPanel sloganPanel = new javax.swing.JPanel();
        javax.swing.JLabel sloganLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel sloganLabel2 = new javax.swing.JLabel();
        javax.swing.JButton shoppingButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(130, 207, 255));
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel); 

        GridBagConstraints gbc = new GridBagConstraints();

        javax.swing.JPanel logoPanel = new javax.swing.JPanel();
        logoPanel.setOpaque(false); 
        logoLabel1.setFont(new Font("ROG Fonts", 0, 100));
        logoLabel1.setForeground(Color.WHITE);
        logoLabel1.setText("siriel");
        logoLabel2.setFont(new Font("ROG Fonts", 0, 86));
        logoLabel2.setForeground(Color.WHITE);
        logoLabel2.setText("shop");
        logoPanel.add(logoLabel1);
        logoPanel.add(logoLabel2);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 45, 0); 
        mainPanel.add(logoPanel, gbc);
        
        sloganPanel.setBackground(Color.WHITE);
        sloganPanel.setLayout(new GridBagLayout()); 
        sloganLabel1.setFont(new Font("Segoe UI", 1, 18));
        sloganLabel1.setText("Belanja onglen gak pake ribet");
        sloganLabel2.setFont(new Font("Segoe UI", 1, 18));
        sloganLabel2.setText("tinggal klak klik klak klik beresss!");
        
        GridBagConstraints gbcSlogan = new GridBagConstraints();
        gbcSlogan.anchor = GridBagConstraints.WEST;
        gbcSlogan.insets = new Insets(10, 15, 5, 15);
        gbcSlogan.gridy = 0;
        sloganPanel.add(sloganLabel1, gbcSlogan);
        gbcSlogan.insets = new Insets(0, 15, 10, 15);
        gbcSlogan.gridy = 1;
        sloganPanel.add(sloganLabel2, gbcSlogan);

        gbc.gridy = 1;
        mainPanel.add(sloganPanel, gbc);

        shoppingButton.setText("Belanja Sekarang!");
        shoppingButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        shoppingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shoppingButton.addActionListener(evt -> {
            this.dispose();
            new Register().setVisible(true);
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(25, 0, 0, 0); 
        mainPanel.add(shoppingButton, gbc);
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
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Start().setVisible(true);
        });
    }
}