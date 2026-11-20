package com.example.bukuku.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Room untuk merepresentasikan tabel buku di dalam database SQLite.
 */
@Entity(tableName = "buku_table")
data class BukuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val penulis: String,
    val kategori: String,
    val sudahDibaca: Boolean = false
)
