package controllers;

import services.SqlServices;
import java.util.regex.Pattern;

public class RegisterController {
    private final SqlServices sqlServices;

    public static final int REGISTRATION_SUCCESS = 0;
    public static final int EMPTY_FIELDS = 1;
    public static final int INVALID_EMAIL_FORMAT = 2;
    public static final int EMAIL_ALREADY_EXISTS = 3;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public RegisterController() {
        this.sqlServices = new SqlServices();
    }

    public int handleRegistration(String email, String password, String alamat) {
        if (email == null || email.trim().isEmpty() || email.equals("masukkan username") ||
            password == null || password.trim().isEmpty() || password.equals("masukkan password") ||
            alamat == null || alamat.trim().isEmpty() || alamat.equals("masukkan alamat pengiriman")) {
            return EMPTY_FIELDS;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return INVALID_EMAIL_FORMAT;
        }

        if (sqlServices.isEmailExist(email)) {
            return EMAIL_ALREADY_EXISTS;
        }
        
        sqlServices.add(email, password, alamat);
        
        return REGISTRATION_SUCCESS;
    }
}