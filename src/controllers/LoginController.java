package controllers;

import services.SqlServices;
import UI.dashboard2; 
import javax.swing.JOptionPane;
import javax.swing.JFrame; 

public class LoginController {
    private final SqlServices sqlServices;
    private JFrame loginFrame; 

    public LoginController(JFrame loginFrame) {
        this.sqlServices = new SqlServices();
        this.loginFrame = loginFrame;
    }

    public void authenticateUser(String email, String password) {
        if (email.equals("masukkan username") || email.isEmpty() || password.equals("masukkan password") || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon isi semua field", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean authenticated = sqlServices.cekUser(email, password);

        if (authenticated) {
            JOptionPane.showMessageDialog(null, "Login berhasil!");
            new dashboard2().setVisible(true);
            loginFrame.dispose(); 
        } else {
            JOptionPane.showMessageDialog(null, "Email atau password salah", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}