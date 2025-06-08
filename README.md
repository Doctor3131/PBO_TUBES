# !!!ALLERT!!!
### Untuk akses ke database, buka file config.propertis di folder src dan masukan data local sql kalian 
```properties
    #Database Configuration
    db.url=jdbc:mysql://localhost:{port}/{schema}
    db.user={biasanya root}
    db.password={password kalian}
    JDBC Driver
    #db.driver=com.mysql.cj.jdbc.Driver
```
### Ganti tiap {...} dengan data kalian
___

# <div align="center">Sequence Diagram Login</div>
```mermaid 
sequenceDiagram
    actor User
    participant StartUI as "start: UI"
    participant RegisterUI as "Register: UI"
    participant RegisterController as "regControl: RegisterController"
    participant LoginUI as "login: UI"
    participant LoginController as "loginControl: LoginController"
    participant SQL as "sqlServices: SqlServices"
    participant DB as "Database"

    User->>StartUI: Membuka Aplikasi
    activate StartUI
    StartUI-->>User: Menampilkan halaman awal
    deactivate StartUI

    User->>StartUI: Klik "Belanja Sekarang!"
    activate StartUI
    StartUI->>RegisterUI: new Register()
    deactivate StartUI
    activate RegisterUI
    RegisterUI-->>User: Menampilkan form registrasi
    
    User->>RegisterUI: Input data (email, password, alamat)
    User->>RegisterUI: Klik tombol "REGISTER"
    RegisterUI->>RegisterController: handleRegistration(email, pass, alamat)
    activate RegisterController
    
    RegisterController->>SQL: isEmailExist(email)
    activate SQL
    SQL->>DB: SELECT COUNT(*) FROM accounts WHERE email=?
    DB-->>SQL: Mengembalikan hasil
    SQL-->>RegisterController: return false (email belum ada)
    deactivate SQL
    
    RegisterController->>SQL: add(email, pass, alamat)
    activate SQL
    SQL->>DB: INSERT INTO accounts(...) VALUES(...)
    DB-->>SQL: Konfirmasi data masuk
    SQL-->>RegisterController: Selesai
    deactivate SQL
    
    RegisterController-->>RegisterUI: return REGISTRATION_SUCCESS
    deactivate RegisterController
    
    RegisterUI-->>User: Tampilkan pesan "Registrasi berhasil!"
    RegisterUI->>LoginUI: new Login()
    deactivate RegisterUI
    activate LoginUI
    LoginUI-->>User: Menampilkan form login
    
    User->>LoginUI: Input data (email, password)
    User->>LoginUI: Klik tombol "LOGIN"
    LoginUI->>LoginController: authenticateUser(email, pass)
    activate LoginController
    
    LoginController->>SQL: cekUser(email, pass)
    activate SQL
    SQL->>DB: SELECT password FROM accounts WHERE email=?
    DB-->>SQL: Mengembalikan password tersimpan
    SQL-->>LoginController: return true (autentikasi berhasil)
    deactivate SQL
    
    LoginController-->>LoginUI: return AUTH_SUCCESS
    deactivate LoginController
    
    LoginUI-->>User: Tampilkan pesan "Login berhasil!"
    LoginUI->>DashboardUI: new Dashboard()
    deactivate LoginUI
```

<br>
<br>

# <div align="center">Sequece Diagram CRUD Barang</div>
```mermaid
sequenceDiagram
    actor User
    participant DashboardUI as "Dashboard: UI"
    participant DashboardController as "dashControl: DashboardController"
    participant KeranjangUI as "keranjang: UI"
    participant KeranjangController as "cartControl: KeranjangController"
    participant SQL as "sqlServices: SqlServices"
    participant DB as "Database"
    participant Transaksi as "transaksi: Transaksi"

    activate DashboardUI
    DashboardUI->>DashboardController: getFilteredProducts(keyword)
    activate DashboardController
    DashboardController->>SQL: getAllProducts()
    activate SQL
    SQL->>DB: SELECT * FROM produk_elektronik
    DB-->>SQL: List<Produk>
    SQL-->>DashboardController: return produkList
    deactivate SQL
    DashboardController-->>DashboardUI: return filteredProdukList
    deactivate DashboardController
    DashboardUI-->>User: Menampilkan daftar produk

    User->>DashboardUI: Klik "Tambah ke Keranjang"
    DashboardUI-->>User: Minta input jumlah
    User->>DashboardUI: Masukkan jumlah

    DashboardUI->>DashboardController: addItemToCart(productId, quantity)
    activate DashboardController
    DashboardController->>SQL: reduceProductStock(productId, quantity)
    activate SQL
    SQL->>DB: UPDATE produk_elektronik SET stok = stok - ?
    DB-->>SQL: Konfirmasi update
    SQL-->>DashboardController: return 0 (sukses)
    deactivate SQL
    DashboardController->>DashboardUI: addToCartStatusCallback("Produk berhasil ditambahkan")
    deactivate DashboardController
    DashboardUI-->>User: Tampilkan notifikasi
    DashboardUI->>DashboardUI: Refresh daftar produk (tampilkan stok baru)

    User->>DashboardUI: Klik "Lihat Keranjang"
    DashboardUI->>KeranjangUI: new Keranjang(this, cartControl)
    deactivate DashboardUI
    activate KeranjangUI
    KeranjangUI->>KeranjangController: getCartItems()
    activate KeranjangController
    KeranjangController-->>KeranjangUI: return cartItems
    deactivate KeranjangController
    KeranjangUI-->>User: Menampilkan isi keranjang
    
    User->>KeranjangUI: Klik "Checkout"
    KeranjangUI->>KeranjangController: performCheckout()
    activate KeranjangController
    KeranjangController->>Transaksi: new Transaksi(cartItems)
    activate Transaksi
    Transaksi-->>KeranjangController: return transaksiBaru
    deactivate Transaksi
    KeranjangController->>KeranjangController: clearCart()
    KeranjangController-->>KeranjangUI: return transaksiBaru
    deactivate KeranjangController
    
    KeranjangUI-->>User: Tampilkan detail transaksi
    deactivate KeranjangUI
```