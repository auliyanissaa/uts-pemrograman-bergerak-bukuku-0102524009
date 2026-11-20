package com.example.bukuku.data.repository

import com.example.bukuku.data.local.BukuDao
import com.example.bukuku.data.local.BukuEntity
import com.example.bukuku.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.Flow

/**
 * Repository yang bertindak sebagai single source of truth,
 * menghubungkan DAO (Room Database) dan PreferencesManager (DataStore).
 */
class BukuRepository(
    private val bukuDao: BukuDao,
    private val preferencesManager: PreferencesManager
) {

    // --- Bagian Room Database (Buku) ---

    val allBuku: Flow<List<BukuEntity>> = bukuDao.getAllBuku()

    suspend fun getBukuById(id: Long): BukuEntity? {
        return bukuDao.getBukuById(id)
    }

    suspend fun insertBuku(buku: BukuEntity) {
        bukuDao.insertBuku(buku)
    }

    suspend fun updateBuku(buku: BukuEntity) {
        bukuDao.updateBuku(buku)
    }

    suspend fun deleteBuku(buku: BukuEntity) {
        bukuDao.deleteBuku(buku)
    }

    suspend fun updateStatusBaca(id: Long, sudahDibaca: Boolean) {
        bukuDao.updateStatusBaca(id, sudahDibaca)
    }

    // --- Bagian Preferences DataStore (Pengaturan) ---

    val isCompactView: Flow<Boolean> = preferencesManager.isCompactViewFlow

    suspend fun setCompactView(isCompact: Boolean) {
        preferencesManager.setCompactView(isCompact)
    }
}
