## Deskripsi

**Sistem Penjualan Rumah** adalah aplikasi berbasis Java yang dirancang untuk memudahkan proses penjualan rumah mulai dari pengelolaan data rumah, karyawan, customer, hingga transaksi dan pelaporan. Aplikasi ini dikembangkan menggunakan NetBeans IDE.

## Fitur Utama

1. **Login dan Logout**  
   Sistem autentikasi pengguna untuk keamanan dan pengelolaan akses.

2. **Menu Utama**  
   Navigasi mudah ke seluruh fitur aplikasi.

3. **Form Master**  
   - Form Rumah: Manajemen data rumah.
   - Form Karyawan: Manajemen data karyawan.
   - Form Customer: Manajemen data customer.
   - Form Booking/Reservasi Rumah: Pencatatan pemesanan rumah.

4. **Transaksi**  
   - Form Penjualan Rumah: Proses penjualan rumah.
   - Form Pembayaran: Pencatatan pembayaran customer.

5. **Laporan**  
   - Laporan Data Rumah Tersedia
   - Laporan Data Karyawan
   - Laporan Penjualan Rumah
   - Laporan Pembayaran Customer

Tampilan dashboard aplikasi menampilkan statistik jumlah rumah tersedia, jumlah karyawan, dan rumah terjual serta menu navigasi di sidebar.

## Cara Menjalankan Aplikasi di Java NetBeans

1. **Persiapan**
   - Pastikan Java Development Kit (JDK) sudah terpasang di komputer Anda.
   - Install NetBeans IDE (disarankan versi terbaru).

2. **Clone atau Download Repository**
   - Clone repository ini atau download source code-nya.

3. **Buka Project di NetBeans**
   - Buka NetBeans IDE.
   - Pilih menu `File > Open Project`.
   - Arahkan ke folder hasil clone/download repository ini, lalu pilih project.

4. **Setup Database**
   - Buat database dengan nama `db_bestpropertyy` menggunakan MySQL atau database server yang digunakan pada aplikasi.
   - Import file SQL (db_bestpropertyy.sql) untuk membuat struktur dan data awal database.
   - Pastikan konfigurasi koneksi database pada aplikasi sudah sesuai dengan host, user, password, dan nama database.

5. **Install Library Pendukung**
   - Install library JasperReports dan iReport untuk kebutuhan cetak laporan.
     - JasperReports: [Download di sini](https://community.jaspersoft.com/project/jasperreports-library)
     - iReport: [Download di sini](https://community.jaspersoft.com/project/ireport-designer)
   - Tambahkan library JasperReports ke dalam libraries project di NetBeans (klik kanan pada Libraries → Add JAR/Folder).

6. **Jalankan Project**
   - Klik kanan pada nama project di NetBeans, lalu pilih `Run`.
   - Login dengan akun yang sudah terdaftar atau buat akun baru jika diperlukan.

## Catatan

- Pastikan koneksi database sudah benar agar aplikasi dapat berjalan dengan baik.
- Untuk kebutuhan cetak laporan, pastikan JasperReports dan iReport sudah terinstall serta library-nya sudah ditambahkan ke project.
- Jika terdapat file SQL di dalam source code, lakukan import ke database agar struktur dan data awal sesuai dengan kebutuhan aplikasi.

---

Jika ada kendala atau pertanyaan lebih lanjut, silakan gunakan halaman Issues di repository ini.
