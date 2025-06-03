package appTerminal;

import UI.login; 
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            login loginDialog = new login();
            loginDialog.setVisible(true);
        });
    }
}