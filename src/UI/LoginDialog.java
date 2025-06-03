package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import services.SqlServices;

public class LoginDialog extends JDialog {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final SqlServices sqlServices;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);

        this.sqlServices = new SqlServices();
        this.emailField = new JTextField(20);
        this.passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc); 

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc); 

        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        loginButton.addActionListener(this::performLogin);
        cancelButton.addActionListener(e -> {
            System.out.println("Login dibatalkan.");
            System.exit(0);
        });

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); 
    }

    private void performLogin(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan password tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (sqlServices.cekUser(email, password)) {
            dispose(); 
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Email atau password salah.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
} 