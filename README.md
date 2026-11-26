# BukuKu — Aplikasi Pengelola Buku Bacaan

| Atribut | Keterangan |
| :--- | :--- |
| **Nama** | Auliya Nissa Amanda Putri |
| **NIM** | 0102524009 |
| **Kelompok Topik** | Topik A — Buku Bacaan |

---

## 📱 Deskripsi Aplikasi
**BukuKu** adalah aplikasi Android berbasis **Jetpack Compose** dan **Material 3** yang dirancang untuk membantu pengguna mengelola daftar buku bacaan pribadi. Aplikasi ini memungkinkan pengguna untuk mencatat judul buku, penulis, kategori, serta memantau status bacaan (*sudah dibaca* atau *belum dibaca*).

Selain itu, aplikasi ini dilengkapi dengan pengaturan **Mode Tampilan Ringkas** (`ON`/`OFF`) yang disimpan secara persisten menggunakan *Preferences DataStore*, serta penyimpanan data buku lokal menggunakan **Room Database**. Aplikasi ini berjalan secara lokal tanpa menggunakan Firebase maupun REST API eksternal.

---

## ✨ Daftar Fitur Utama
1. **Daftar Buku (Home Screen)**:
   - Menampilkan seluruh daftar buku menggunakan `LazyColumn`.
   - Menampilkan informasi judul, penulis, kategori, dan indikator status bacaan.
   - Tombol cepat (checkbox / chip/tombol status) untuk mengubah status baca langsung dari daftar.
   - Pesan informatif (*Empty State*) ketika belum ada buku yang tersimpan.
2. **Toggle Mode Tampilan Ringkas**:
   - Mengubah kepadatan dan detail item pada daftar buku (Mode Normal vs Mode Ringkas).
   - Preferensi disimpan secara persisten menggunakan *Preferences DataStore*.
3. **Detail Buku**:
   - Menampilkan informasi lengkap buku yang dipilih.
   - Menyediakan tombol untuk mengubah status baca, tombol edit, dan tombol hapus.
4. **Form Tambah & Ubah Buku**:
   - Form input untuk judul, penulis, dan kategori.
   - **Validasi Input**: Judul buku divalidasi agar tidak boleh kosong atau hanya berisi spasi, dengan pesan error yang jelas.
5. **Navigasi Mulus (Navigation Compose)**:
   - Perpindahan antar tiga layar utama (**Daftar Buku**, **Detail Buku**, dan **Form Tambah/Ubah**) berjalan lancar dengan sistem *Back Stack* yang aman tanpa *crash*.

---

## 🛠️ Perangkat & Libraries yang Digunakan
- **Bahasa**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Navigasi**: Navigation Compose (`androidx.navigation:navigation-compose`)
- **Database Lokal**: Room Database (`androidx.room`) dengan KSP (*Kotlin Symbol Processing*)
- **Penyimpanan Lokal (Key-Value)**: Preferences DataStore (`androidx.datastore:datastore-preferences`)
- **Lifecycle & State**: ViewModel, `StateFlow`, dan `collectAsState()`
- **Penyimpanan Eksternal / Backend**: Tidak menggunakan Firebase maupun REST API

---

## 🚀 Cara Menjalankan Aplikasi
1. Clone atau buka proyek **BukuKu** di **Android Studio**.
2. Pastikan perangkat fisik (Android Phone dengan *USB Debugging* aktif) atau emulator Android siap digunakan.
3. Lakukan **Gradle Sync** apabila diperlukan.
4. Klik tombol **Run (`app`)** ▶️ di Android Studio untuk melakukan *build* dan menjalankan aplikasi ke perangkat.

---

## 🎥 Tautan Video Demo
- **Link Video Presentasi & Demo**: [Tonton Video Presentasi BukuKu di Google Drive / YouTube](GANTI_DENGAN_LINK_VIDEO_KAMU)

---

## 🖼️ Tangkapan Layar (Screenshots)

### 1. Layar Daftar Buku (Minimal 5 Data Buku)
![Layar Daftar Buku](screenshots/daftar_buku.png)

### 2. Form Tambah / Ubah Buku & Validasi Input
![Form Tambah / Ubah Buku](screenshots/form_buku.png)

### 3. Mode Tampilan Ringkas & Bukti Persistence
![Mode Tampilan Ringkas dan Persistence](screenshots/mode_ringkas_persistence.png)

*(Catatan: Sesuaikan nama file gambar di folder `screenshots/` dengan file tangkapan layar yang kamu simpan di repositori.)*

---

## 🤖 Deklarasi Penggunaan AI
- **Alat AI yang Digunakan**: Gemini / asisten AI.
- **Tujuan Penggunaan**: Membantu menyusun struktur dokumentasi, merapikan penjelasan alur arsitektur/kode.
- **Verifikasi Mandiri**: Seluruh kode, konfigurasi Room & DataStore, pengujian alur di perangkat, serta penyusunan video presentasi diperiksa, disesuaikan, dan diverifikasi sendiri oleh penyusun.

---

## 🎙️ Ringkasan Penjelasan Teknis per Komponen (Persiapan Video & Verifikasi Dosen)

### 1. Lapisan Data — Room Database & DataStore
- **`BukuEntity.kt`**: Mendefinisikan model tabel SQLite menggunakan `@Entity` dengan field utama seperti `id`, `judul`, `penulis`, `kategori`, dan `sudahDibaca`.
- **`BukuDao.kt`**: Menyediakan operasi database lokal (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) dengan `Flow` agar perubahan data terpantau secara reaktif.
- **`BukuDatabase.kt`**: Kelas turunan `RoomDatabase` yang diinisialisasi menggunakan pola *Singleton*.
- **`PreferencesManager.kt`**: Mengelola *Preferences DataStore* untuk menyimpan preferensi boolean **Mode Tampilan Ringkas** secara persisten.
- **`BukuRepository.kt`**: Menjembatani `ViewModel` dengan Room DAO dan *Preferences DataStore* sebagai sumber data terpusat.

### 2. Lapisan State & ViewModel
- **`BukuUiState.kt` / `BukuUiEvent.kt`**: Merepresentasikan state tampilan (daftar buku, mode ringkas, input form, pesan error validasi) dan event aksi pengguna.
- **`BukuViewModel.kt`**: Mengelola aliran data berbasis `StateFlow`, validasi input judul kosong/spasi, serta pemrosesan aksi tambah, ubah, hapus, status bacaan, dan mode tampilan ringkas.

### 3. Lapisan Antarmuka (Jetpack Compose & Navigation)
- **`Screen.kt` / Navigasi Compose**: Mendefinisikan rute navigasi antar layar daftar, detail, dan form tambah/ubah.
- **`DaftarBukuScreen.kt`**: Menampilkan *TopAppBar*, toggle **Mode Tampilan Ringkas**, `LazyColumn` daftar buku, aksi ubah status bacaan, dan *Empty State* saat data kosong.
- **`DetailBukuScreen.kt`**: Menampilkan detail buku beserta aksi ubah status bacaan, edit, dan hapus.
- **`FormBukuScreen.kt`**: Form input judul, penulis, dan kategori lengkap dengan validasi judul kosong dan tombol simpan.
- **`MainActivity.kt`**: Menginisialisasi komponen aplikasi dan `NavHost` untuk menghubungkan seluruh layar.
