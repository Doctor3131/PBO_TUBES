package controllers;

import services.SqlServices;
import java.util.regex.Pattern;

/**
 * Controller untuk menangani logika di balik fungsionalitas registrasi.
 * Ini memvalidasi input pengguna dan berinteraksi dengan layanan database.
 */
public class RegisterController {

    private final SqlServices sqlServices;

    // Pola regex sederhana untuk validasi format email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public RegisterController() {
        this.sqlServices = new SqlServices();
    }

    public int handleRegistration(String email, String password, String alamat) {


        // Pesan 1: Cek jika ada field yang kosong
        if (email == null || email.isEmpty() || email.equals("masukkan username") ||
            password == null || password.isEmpty() || password.equals("masukkan password") ||
            alamat == null || alamat.isEmpty() || alamat.equals("masukkan alamat pengiriman")) {
            return 1; // Mengindikasikan field tidak boleh kosong
        }

        // Pesan 2: Cek format email menggunakan regex
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return 2; // Mengindikasikan format email salah
        }

        // Pesan 3: Cek apakah email sudah ada di database
        if (sqlServices.isEmailExist(email)) {
            return 3; // Mengindikasikan email sudah digunakan
        }

        // Jika semua validasi berhasil, tambahkan pengguna baru ke database
        sqlServices.add(email, password, alamat);
        
        return 0; // Mengindikasikan registrasi berhasil
    }
}