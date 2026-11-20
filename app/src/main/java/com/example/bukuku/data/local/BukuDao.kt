package com.example.bukuku.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) untuk mendefinisikan operasi database pada tabel buku.
 */
@Dao
interface BukuDao {

    /**
     * Mengambil semua daftar buku dari database secara reaktif menggunakan Flow.
     */
    @Query("SELECT * FROM buku_table ORDER BY id DESC")
    fun getAllBuku(): Flow<List<BukuEntity>>

    /**
     * Mengambil satu data buku berdasarkan ID uniknya.
     */
    @Query("SELECT * FROM buku_table WHERE id = :bukuId")
    suspend fun getBukuById(bukuId: Long): BukuEntity?

    /**
     * Menambahkan data buku baru ke database. Jika terjadi konflik, data akan digantikan.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuku(buku: BukuEntity)

    /**
     * Memperbarui data buku yang sudah ada.
     */
    @Update
    suspend fun updateBuku(buku: BukuEntity)

    /**
     * Menghapus data buku dari database.
     */
    @Delete
    suspend fun deleteBuku(buku: BukuEntity)

    /**
     * Memperbarui status 'sudahDibaca' pada buku tertentu berdasarkan ID.
     */
    @Query("UPDATE buku_table SET sudahDibaca = :sudahDibaca WHERE id = :bukuId")
    suspend fun updateStatusBaca(bukuId: Long, sudahDibaca: Boolean)
}
