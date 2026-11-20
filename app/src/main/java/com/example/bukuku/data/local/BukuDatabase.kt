package com.example.bukuku.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Kelas Database Room untuk aplikasi BukuKu yang bertindak sebagai titik akses utama koneksi SQLite.
 */
@Database(entities = [BukuEntity::class], version = 1, exportSchema = false)
abstract class BukuDatabase : RoomDatabase() {

    abstract fun bukuDao(): BukuDao

    companion object {
        @Volatile
        private var INSTANCE: BukuDatabase? = null

        /**
         * Mendapatkan instance singleton dari BukuDatabase.
         */
        fun getDatabase(context: Context): BukuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BukuDatabase::class.java,
                    "buku_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
