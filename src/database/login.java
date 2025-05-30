package database;

import java.util.ArrayList;
import java.util.List;
import database.service;
import database.account;
import javax.swing.JOptionPane;

public class login extends javax.swing.JFrame {

    

    public login() {
        initComponents();
        
        service service = new service();
        
        jTextFieldEmail.setText("masukkan username");
        jTextFieldEmail.setForeground(java.awt.Color.GRAY);

        jTextFieldEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextFieldEmail.getText().equals("masukkan username")) {
                    jTextFieldEmail.setText("");
                    jTextFieldEmail.setForeground(java.awt.Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextFieldEmail.getText().isEmpty()) {
                    jTextFieldEmail.setForeground(java.awt.Color.GRAY);
                    jTextFieldEmail.setText("masukkan username");
                }
            }
        });
        
        jTextFieldPassword.setText("masukkan password");
        jTextFieldPassword.setForeground(java.awt.Color.GRAY);

        jTextFieldPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextFieldPassword.getText().equals("masukkan password")) {
                    jTextFieldPassword.setText("");
                    jTextFieldPassword.setForeground(java.awt.Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextFieldPassword.getText().isEmpty()) {
                    jTextFieldPassword.setForeground(java.awt.Color.GRAY);
                    jTextFieldPassword.setText("masukkan password");
                }
            }
        });
       
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelNamaSistem = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 153));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabelNamaSistem.setFont(new java.awt.Font("ROG Fonts", 1, 75)); // NOI18N
        jLabelNamaSistem.setText("SIRIEL SHOP");

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelEmail.setText("Email");

        jTextFieldEmail.setToolTipText("");

        jLabelPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelPassword.setText("Password");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("LOGIN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPassword, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(349, 349, 349))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(238, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelNamaSistem)
                        .addGap(213, 213, 213))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(443, 443, 443))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabelNamaSistem, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jLabelEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String email = jTextFieldEmail.getText().trim();
        String password = jTextFieldPassword.getText().trim();

        // Cek apakah user input kosong
        if (email.equals("masukkan username") || email.isEmpty() || password.equals("masukkan password") || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon isi semua field", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek ke database
        service service = new service(); // buat objek service
        account acc = service.getByEmailAndPassword(email, password);

        if (acc != null) {
            JOptionPane.showMessageDialog(null, "Login berhasil! Selamat datang, " + acc.getEmail());
            // TODO: Pindah ke halaman berikutnya
        } else {
            JOptionPane.showMessageDialog(null, "Email atau password salah", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelNamaSistem;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldPassword;
    // End of variables declaration//GEN-END:variables
}
