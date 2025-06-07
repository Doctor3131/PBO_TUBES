package controllers;

import services.SqlServices;
// Removed UI imports
// import javax.swing.JOptionPane;
// import javax.swing.JFrame; 

public class LoginController {
    private final SqlServices sqlServices;
    // Removed reference to JFrame as it no longer directly interacts with it for display/dispose
    // private JFrame loginFrame; 

    public static final int AUTH_SUCCESS = 0;
    public static final int AUTH_INVALID_CREDENTIALS = 1;
    public static final int AUTH_DB_ERROR = 2;
    // No need for "empty field" status here as UI handles initial check

    public LoginController() { // Constructor simplified, no longer needs JFrame
        this.sqlServices = new SqlServices();
    }

    // Returns an integer status indicating the authentication result
    public int authenticateUser(String email, String password) {
        // Basic input validation for empty fields/placeholders can remain in UI layer
        // since it's about UI input state.
        // If the UI sends valid (non-placeholder/non-empty) strings, proceed to DB check.

        boolean authenticated = sqlServices.cekUser(email, password);

        if (authenticated) {
            return AUTH_SUCCESS;
        } else {
            // Check for database error or invalid credentials
            // Assuming cekUser shows an error dialog itself for DB connection issues,
            // otherwise, differentiate between "not found" and "db error" more explicitly.
            // For now, if cekUser returns false, it's either invalid credentials or a DB error handled internally by cekUser.
            // Let's assume false primarily means invalid credentials for clearer UI messaging here.
            return AUTH_INVALID_CREDENTIALS;
        }
    }
}