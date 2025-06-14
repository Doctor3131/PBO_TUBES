# <div align="center">======== !!!PERHATIAN!!! =========</div>
___

### Dummy data. Tambahkan ke schema database yang sudah ada
```sql
-- untuk membuat tabel accounts
drop table accounts;
create table accounts (
	email varchar(255) primary key,
    password varchar(15) not null,
    alamat varchar(255) not null,
    money int default 0
);

-- insert dummy data untuk accounts
INSERT INTO accounts (email, password, alamat,money) VALUES
('sirils@gmail.com', 'siriel', 'jakarta 123' ,500),
('ungguls@gmail.com', 'unggul', 'hadehh',120),
('regins@gmail.com', 'regina', 'mana aja boleh',1000),
('ninabae@gmail.com', 'ninskuy', 'dahlah',2000),
('takikun@gmail.com', 'taktak', 'wes ah',10);

-- untuk membuat tabel produk_elektronik
CREATE TABLE produk_elektronik (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(255) NOT NULL,
    kategori VARCHAR(100),
    sku VARCHAR(50) UNIQUE,
    deskripsi TEXT,
    harga DECIMAL(10, 2),accounts
    stok INT
);

-- insert dummy data untuk produk_elektronik
INSERT INTO produk_elektronik (nama_produk, kategori, sku, deskripsi, harga, stok) VALUES
('Arduino Uno R3', 'Mikrokontroler', 'SKU-UNO-R3', 'Papan pengembangan mikrokontroler berbasis ATmega328P.', 125000.00, 50),
('ESP32-WROOM-32', 'Mikrokontroler', 'SKU-ESP-32', 'Modul mikrokontroler dengan WiFi dan Bluetooth terintegrasi.', 95000.00, 75),
('Sensor Suhu DHT11', 'Sensor', 'SKU-DHT-11', 'Sensor untuk mengukur suhu dan kelembapan udara.', 15000.00, 200),
('Modul Relay 2 Channel', 'Modul', 'SKU-RLY-02', 'Modul relay dengan 2 channel untuk mengendalikan perangkat AC/DC.', 25000.00, 150),
('Motor Servo SG90', 'Aktuator', 'SKU-SRV-90', 'Motor servo kecil untuk proyek robotika dan otomasi.', 18000.00, 300),
('Sensor Ultrasonik HC-SR04', 'Sensor', 'SKU-HC-SR04', 'Sensor untuk mengukur jarak menggunakan gelombang ultrasonik.', 12000.00, 250),
('Resistor Kit (300 pcs)', 'Komponen Pasif', 'SKU-RES-KIT', 'Kumpulan resistor dengan berbagai nilai resistansi.', 35000.00, 100),
('LED Kit (100 pcs)', 'Komponen Pasif', 'SKU-LED-KIT', 'Kumpulan LED dengan berbagai warna.', 20000.00, 180),
('Breadboard 830 Point', 'Alat Prototipe', 'SKU-BRD-830', 'Papan roti untuk merakit sirkuit elektronik tanpa solder.', 22000.00, 120),
('Kabel Jumper Male-to-Male', 'Aksesori', 'SKU-JMP-MM', 'Satu set kabel jumper untuk menghubungkan komponen di breadboard.', 10000.00, 500);

``` 

___

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

### Menambahkan file .jar untuk koneksi ke mysql
1. Download dan tambahkan file mysql-connector.jar for java versi terbaru. [Link to download](https://dev.mysql.com/downloads/connector/j/). (jika tidak bisa, coba versi 8.3 yang ada di archive)

2. Buka NetBeans -> projects -> FetchSearchProduct -> Libraries. click kanan di Libraries dan pilih Add JAR/Folder

    <img src="image.png" width="300">

3. Pada menu pop-up, arahkan dimana file.jar berada, jika sudah ditemukan maka click file nya, lalu click open

    <img src="image-2.png" width="800">

4. Jika berhasil, maka file.jar akan muncul di Libraries

    <img src="image-3.png" width="400">
___

## <div align="center">Sequence Diagram Login</div>
```mermaid 
sequenceDiagram 
    actor User
    participant StartUI as ":S tart"
    participant RegisterUI as ":Register"
    participant RegisterController as ":RegisterController"
    participant LoginUI as ":Login"
    participant LoginController as ":LoginController"
    participant SQL as ":SqlServices"
    participant DB as "Database"
    participant DashboardUI as ":Dashboard"

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

## <div align="center">Sequece Diagram CRUD Barang</div>
```mermaid
sequenceDiagram
    actor User
    participant DashboardUI as ":Dashboard"
    participant DashboardController as ":DashboardController"
    participant KeranjangUI as ":keranjang"
    participant KeranjangController as ":KeranjangController"
    participant SQL as ":SqlServices"
    participant DB as "Database"
    participant Transaksi as ":Transaksi"

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