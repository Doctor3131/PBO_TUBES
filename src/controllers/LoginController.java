package controllers;

import services.SqlServices;

public class LoginController {
    private final SqlServices sqlServices;

    public static final int AUTH_SUCCESS = 0;
    public static final int AUTH_INVALID_CREDENTIALS = 1;
    public static final int AUTH_BLANK = 2;

    public LoginController() {
        this.sqlServices = new SqlServices();
    }

    public int authenticateUser(String email, String password) {
        if (email == null || email.trim().isEmpty() || email.equals("masukkan username") ||
            password == null || password.trim().isEmpty() || password.equals("masukkan password")) {
            return AUTH_BLANK;
        }

        boolean authenticated = sqlServices.cekUser(email, password);

        if (authenticated) {
            return AUTH_SUCCESS;
        } else {
            return AUTH_INVALID_CREDENTIALS;
        }
    }
}