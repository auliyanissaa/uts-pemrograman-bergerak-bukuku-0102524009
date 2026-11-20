# BukuKu — Aplikasi Pengelola Buku Bacaan

| Atribut | Keterangan |
| :--- | :--- |
| **Nama** | Auliya Nissa Amanda Putri |
| **NIM** | 0102524009 |
| **Kelompok Topik** | Topik A — Buku Bacaan |

---

## 📱 Deskripsi Aplikasi
**BukuKu** adalah aplikasi Android berbasis **Jetpack Compose** dan **Material 3** yang dirancang untuk membantu pengguna mengelola daftar buku bacaan pribadi. Aplikasi ini memungkinkan pengguna untuk mencatat judul buku, penulis, kategori, serta memantau status bacaan (sudah dibaca atau belum dibaca). Selain itu, aplikasi ini dilengkapi dengan fitur pengaturan **Mode Tampilan Ringkas** yang disimpan secara persisten menggunakan *Preferences DataStore*.

---

## ✨ Daftar Fitur Utama
1. **Daftar Buku (Home Screen)**:
   - Menampilkan seluruh daftar buku menggunakan `LazyColumn`.
   - Menampilkan informasi judul, penulis, kategori, dan indikator status bacaan.
   - Tombol cepat (checkbox / assist chip) untuk mengubah status baca langsung dari daftar.
   - Pesan informatif (*Empty State*) ketika belum ada buku yang tersimpan.
2. **Toggle Mode Tampilan Ringkas**:
   - Mengubah kepadatan dan detail item pada daftar buku (Mode Normal vs Mode Ringkas).
   - Preferensi disimpan secara persisten menggunakan *Preferences DataStore*.
3. **Detail Buku**:
   - Menampilkan informasi lengkap buku yang dipilih.
   - Menyediakan tombol untuk mengubah status baca, tombol edit, dan tombol hapus.
4. **Form Tambah & Ubah Buku**:
   - Form input untuk judul, penulis, dan kategori.
   - **Validasi Input**: Judul buku divalidasi agar tidak boleh kosong atau hanya berisi spasi, dengan pesan error yang interaktif.
5. **Navigasi Mulus (Navigation Compose)**:
   - Perpindahan antar halaman (Daftar → Detail → Form Tambah/Ubah) berjalan lancar dengan sistem *Back Stack* yang aman tanpa *crash*.

---

## 🛠️ Perangkat & Libraries yang Digunakan
- **Bahasa**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Navigasi**: Navigation Compose (`androidx.navigation:navigation-compose`)
- **Database Lokal**: Room Database (`androidx.room`) dengan KSP (*Kotlin Symbol Processing*)
- **Penyimpanan Lokal (Key-Value)**: Preferences DataStore (`androidx.datastore:datastore-preferences`)
- **Lifecycle & State**: ViewModel, `StateFlow`, dan `collectAsState()`

---

## 🚀 Cara Menjalankan Aplikasi
1. Clone atau buka project **BukuKu** di **Android Studio**.
2. Pastikan Anda memiliki perangkat virtual (Emulator Android) atau perangkat fisik (Android Phone dengan USB Debugging aktif).
3. Lakukan **Gradle Sync** jika diperlukan.
4. Klik tombol **Run ('app')** ▶️ di Android Studio untuk melakukan build dan menjalankan aplikasi ke perangkat.

---

## 🎥 Tautan Video Demo
> [!NOTE]
> Placeholder tautan video penjelasan dan demo aplikasi (durasi 6–8 menit):
> 👉 **[Tautan Video YouTube / Google Drive (Akan Segera Diunggah)]**

---

## 🎙️ Ringkasan Penjelasan Teknis per Komponen (Persiapan Video & Verifikasi Dosen)

Berikut adalah ringkasan teknis dari setiap file utama yang dapat Anda gunakan sebagai panduan saat merekam video penjelasan (6–8 menit) maupun saat sesi tanya jawab/verifikasi dengan dosen:

### 1. Lapisan Data — Room Database & DataStore
- **[`BukuEntity.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/data/local/BukuEntity.kt)**:
  - *Penjelasan*: Model tabel SQLite menggunakan anotasi `@Entity(tableName = "buku_table")`. Berisi kolom `id` (primary key auto-generate), `judul`, `penulis`, `kategori`, dan `sudahDibaca`.
- **[`BukuDao.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/data/local/BukuDao.kt)**:
  - *Penjelasan*: Antarmuka Data Access Object (`@Dao`) yang berisi operasi SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`). Pengambilan data daftar buku menggunakan `Flow<List<BukuEntity>>` agar reaktif terhadap perubahan data.
- **[`BukuDatabase.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/data/local/BukuDatabase.kt)**:
  - *Penjelasan*: Kelas abstrak turunan `RoomDatabase` yang diinisialisasi menggunakan pola **Singleton** untuk mencegah pembuatan instans database ganda yang dapat memboroskan memori.
- **[`PreferencesManager.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/data/preferences/PreferencesManager.kt)**:
  - *Penjelasan*: Mengelola **Preferences DataStore** untuk menyimpan preferensi boolean "Mode Tampilan Ringkas" (`is_compact_view`) secara asinkron menggunakan Kotlin `Flow`.
- **[`BukuRepository.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/data/repository/BukuRepository.kt)**:
  - *Penjelasan*: Berperan sebagai **Single Source of Truth**. Repository menjembatani ViewModel dengan Room DAO dan PreferencesManager sehingga ViewModel bersih dari detail implementasi penyimpanan data.

### 2. Lapisan State & ViewModel (UDF Pattern)
- **[`BukuUiState.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/ui/BukuUiState.kt)**:
  - *Penjelasan*: Mendefinisikan struktur `BukuUiState` (menyimpan daftar buku, status tampilan, input form, pesan error validasi) dan `BukuUiEvent` (kumpulan aksi dari UI seperti `OnSaveBuku`, `OnToggleCompactView`, dll).
- **[`BukuViewModel.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/ui/BukuViewModel.kt)**:
  - *Penjelasan*: Menggabungkan data dari Room, DataStore, dan state form lokal menggunakan operator `combine` dan `stateIn` menjadi satu `StateFlow<BukuUiState>`. Berisi logika validasi input (judul tidak boleh kosong/spasi) dan pemrosesan event.

### 3. Lapisan Antarmuka (Jetpack Compose & Navigation)
- **[`Screen.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/ui/navigation/Screen.kt)**:
  - *Penjelasan*: Sealed class untuk mendefinisikan rute navigasi (`daftar_buku`, `detail_buku/{bukuId}`, dan `form_buku?bukuId={bukuId}`) beserta fungsi pembuat rute dinamis.
- **[`DaftarBukuScreen.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/ui/screens/DaftarBukuScreen.kt)**:
  - *Penjelasan*: Layar utama dengan `TopAppBar`, switch toggle Mode Ringkas, `LazyColumn` untuk daftar buku, kartu item interaktif, serta *Empty State* saat data kosong.
- **[`DetailBukuScreen.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/ui/screens/DetailBukuScreen.kt)**:
  - *Penjelasan*: Layar detail yang menampilkan informasi lengkap buku, tombol ubah status baca, tombol navigasi ke form edit, dan tombol hapus buku.
- **[`FormBukuScreen.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/ui/screens/FormBukuScreen.kt)**:
  - *Penjelasan*: Layar form input dengan validasi judul kosong/spasi, menampilkan kartu peringatan error jika validasi gagal, serta tombol simpan.
- **[`MainActivity.kt`](file:///C:/Users/ssscj/AndroidStudioProjects/BukuKu/app/src/main/java/com/example/bukuku/MainActivity.kt)**:
  - *Penjelasan*: Titik entry aplikasi. Menginisialisasi Database, Repository, dan ViewModel, serta menyiapkan `NavHost` untuk menghubungkan navigasi antar ketiga layar secara aman tanpa *crash*.
