package com.example.bukuku.ui

import com.example.bukuku.data.local.BukuEntity

/**
 * Representasi State UI untuk layar BukuKu.
 * Menyimpan seluruh data yang dibutuhkan oleh UI untuk dirender secara reaktif.
 */
data class BukuUiState(
    val daftarBuku: List<BukuEntity> = emptyList(),
    val isCompactView: Boolean = false,
    val judulInput: String = "",
    val penulisInput: String = "",
    val kategoriInput: String = "",
    val bukuYangDiedit: BukuEntity? = null,
    val errorMessage: String? = null,
    val isSaveSuccess: Boolean = false
)

/**
 * Representasi Event atau Aksi yang dikirim dari UI ke ViewModel.
 */
sealed interface BukuUiEvent {
    data class OnJudulChanged(val judul: String) : BukuUiEvent
    data class OnPenulisChanged(val penulis: String) : BukuUiEvent
    data class OnKategoriChanged(val kategori: String) : BukuUiEvent
    data class OnSaveBuku(val bukuId: Long?) : BukuUiEvent
    data class OnDeleteBuku(val buku: BukuEntity) : BukuUiEvent
    data class OnToggleStatusBaca(val buku: BukuEntity) : BukuUiEvent
    data class OnToggleCompactView(val isCompact: Boolean) : BukuUiEvent
    data class OnSelectBukuForEdit(val buku: BukuEntity?) : BukuUiEvent
    data object OnClearError : BukuUiEvent
    data object OnResetForm : BukuUiEvent
    data object OnClearSaveSuccess : BukuUiEvent
}
