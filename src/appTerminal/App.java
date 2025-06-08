package appTerminal;

import UI.Start;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Start appStart = new Start();
            appStart.setVisible(true);
        });
    }
}