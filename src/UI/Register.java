package UI;

import javax.swing.JOptionPane;
import controllers.RegisterController;

public class Register extends javax.swing.JFrame {
    private RegisterController registerController;
    private Login login; 


    public Register() {
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        this.registerController  = new RegisterController();
        this.login = new Login(this);

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
        
        jTextFieldAlamat.setText("masukkan alamat pengiriman");
        jTextFieldAlamat.setForeground(java.awt.Color.GRAY);

        jTextFieldAlamat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextFieldAlamat.getText().equals("masukkan alamat pengiriman")) {
                    jTextFieldAlamat.setText("");
                    jTextFieldAlamat.setForeground(java.awt.Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextFieldAlamat.getText().isEmpty()) {
                    jTextFieldAlamat.setForeground(java.awt.Color.GRAY);
                    jTextFieldAlamat.setText("masukkan alamat pengiriman");
                }
            }
        });
       
    }

    
    private void initComponents() {

        jLabelEmail = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JTextField();
        jButtonRegister = new javax.swing.JButton();
        jLabelPunyaAkun = new javax.swing.JLabel();
        jButtonLogin = new javax.swing.JButton();
        jLabelAlamat = new javax.swing.JLabel();
        jTextFieldAlamat = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabelNamaSistem = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabelEmail.setText("Email");

        jTextFieldEmail.setToolTipText("");
        jTextFieldEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmailActionPerformed(evt);
            }
        });

        jLabelPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabelPassword.setText("Password");

        jButtonRegister.setBackground(new java.awt.Color(130, 207, 255));
        jButtonRegister.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        jButtonRegister.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRegister.setText("REGISTER");
        jButtonRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegisterActionPerformed(evt);
            }
        });

        jLabelPunyaAkun.setText("Sudah punya akun?");

        jButtonLogin.setBackground(new java.awt.Color(130, 207, 255));
        jButtonLogin.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogin.setText("LOGIN");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jLabelAlamat.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabelAlamat.setText("Alamat");

        jTextFieldAlamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAlamatActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 83, 154));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 61));

        jLabelNamaSistem.setBackground(new java.awt.Color(255, 255, 255));
        jLabelNamaSistem.setFont(new java.awt.Font("ROG Fonts", 1, 18));
        jLabelNamaSistem.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNamaSistem.setText("SIRIEL SHOP");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelNamaSistem)
                .addGap(124, 124, 124))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabelNamaSistem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldAlamat, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPassword, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEmail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 118, Short.MAX_VALUE))))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPunyaAkun)
                            .addComponent(jButtonRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jButtonLogin)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelAlamat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jButtonRegister)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPunyaAkun)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLogin)
                .addGap(12, 12, 12))
        );

        pack();
    }

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {
        login.setVisible(rootPaneCheckingEnabled);
        dispose(); 
    }

    private void jTextFieldAlamatActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextFieldEmailActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButtonRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        String email = jTextFieldEmail.getText();
        String password = jTextFieldPassword.getText();
        String alamat = jTextFieldAlamat.getText();

        int message = registerController.handleRegistration(email, password, alamat);

        if (message == 1) { 
            JOptionPane.showMessageDialog(this, "Mohon isi semua field", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        else if (message == 2) {
            JOptionPane.showMessageDialog(this, "Peringatan Gunakan Format email yang benar", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        else if (message == 3) {
            JOptionPane.showMessageDialog(null, "Email sudah digunakan!");
            return;
        } 
        else {
            JOptionPane.showMessageDialog(null, "Registrasi berhasil! SIlahkan Login!");
            this.dispose(); 
            new Login(this).setVisible(true);
            jTextFieldEmail.setText("");
            jTextFieldPassword.setText("");
            jTextFieldAlamat.setText("");
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
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Register().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonRegister;
    private javax.swing.JLabel jLabelAlamat;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelNamaSistem;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelPunyaAkun;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextFieldAlamat;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldPassword;
}
