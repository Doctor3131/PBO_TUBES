package UI;


import services.*;
import javax.swing.JOptionPane;

public class login extends javax.swing.JFrame {

    

    public login() {
        initComponents();
        
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

    
    private void initComponents() {

        jLabelNamaSistem = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JTextField();
        jButtonLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 153));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabelNamaSistem.setFont(new java.awt.Font("ROG Fonts", 1, 18)); 
        jLabelNamaSistem.setText("SIRIEL SHOP");

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabelEmail.setText("Email");

        jTextFieldEmail.setToolTipText("");

        jLabelPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabelPassword.setText("Password");

        jButtonLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        jButtonLogin.setText("LOGIN");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(137, 137, 137)
                            .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(127, 127, 127)
                            .addComponent(jLabelNamaSistem))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(116, 116, 116)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelEmail)
                                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelPassword)
                        .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabelNamaSistem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabelEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonLogin)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String email = jTextFieldEmail.getText().trim();
        String password = jTextFieldPassword.getText().trim();

        if (email.equals("masukkan username") || email.isEmpty() || password.equals("masukkan password") || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon isi semua field", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SqlServices service = new SqlServices();
        boolean acc = service.cekUser(email, password);

        if (acc) {
            JOptionPane.showMessageDialog(null, "Login berhasil!");
            new dashboard2().setVisible(true);
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(null, "Email atau password salah", "Login Gagal", JOptionPane.ERROR_MESSAGE);
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButtonLogin;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelNamaSistem;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldPassword;
}
