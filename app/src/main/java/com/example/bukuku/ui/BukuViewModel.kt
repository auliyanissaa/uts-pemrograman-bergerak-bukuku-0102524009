package com.example.bukuku.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bukuku.data.local.BukuEntity
import com.example.bukuku.data.repository.BukuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola state, validasi input, dan logika bisnis aplikasi BukuKu.
 */
class BukuViewModel(
    private val repository: BukuRepository
) : ViewModel() {

    // State lokal untuk input form
    private val _judulInput = MutableStateFlow("")
    private val _penulisInput = MutableStateFlow("")
    private val _kategoriInput = MutableStateFlow("")
    private val _bukuYangDiedit = MutableStateFlow<BukuEntity?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _isSaveSuccess = MutableStateFlow(false)

    /**
     * StateFlow utama yang menggabungkan aliran data dari Room (daftar buku),
     * DataStore (pengaturan tampilan ringkas), dan input form lokal
     * menjadi satu sumber kebenaran (Single Source of Truth) untuk UI.
     */
    val uiState: StateFlow<BukuUiState> = combine(
        repository.allBuku,
        repository.isCompactView,
        combine(_judulInput, _penulisInput, _kategoriInput) { judul, penulis, kategori ->
            Triple(judul, penulis, kategori)
        },
        combine(_bukuYangDiedit, _errorMessage, _isSaveSuccess) { bukuEdit, error, saveSuccess ->
            Triple(bukuEdit, error, saveSuccess)
        }
    ) { daftarBuku, isCompact, (judul, penulis, kategori), (bukuEdit, error, saveSuccess) ->
        BukuUiState(
            daftarBuku = daftarBuku,
            isCompactView = isCompact,
            judulInput = judul,
            penulisInput = penulis,
            kategoriInput = kategori,
            bukuYangDiedit = bukuEdit,
            errorMessage = error,
            isSaveSuccess = saveSuccess
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BukuUiState()
    )

    /**
     * Fungsi tunggal untuk menerima dan memproses berbagai Event dari UI.
     */
    fun onEvent(event: BukuUiEvent) {
        when (event) {
            is BukuUiEvent.OnJudulChanged -> {
                _judulInput.value = event.judul
                if (_errorMessage.value != null) _errorMessage.value = null
            }
            is BukuUiEvent.OnPenulisChanged -> _penulisInput.value = event.penulis
            is BukuUiEvent.OnKategoriChanged -> _kategoriInput.value = event.kategori
            is BukuUiEvent.OnToggleCompactView -> setCompactView(event.isCompact)
            is BukuUiEvent.OnSelectBukuForEdit -> prepareEditBuku(event.buku)
            is BukuUiEvent.OnClearError -> _errorMessage.value = null
            is BukuUiEvent.OnResetForm -> resetForm()
            is BukuUiEvent.OnClearSaveSuccess -> _isSaveSuccess.value = false
            is BukuUiEvent.OnSaveBuku -> saveOrUpdateBuku(event.bukuId)
            is BukuUiEvent.OnDeleteBuku -> deleteBuku(event.buku)
            is BukuUiEvent.OnToggleStatusBaca -> toggleStatusBaca(event.buku)
        }
    }

    private fun setCompactView(isCompact: Boolean) {
        viewModelScope.launch {
            repository.setCompactView(isCompact)
        }
    }

    private fun prepareEditBuku(buku: BukuEntity?) {
        _bukuYangDiedit.value = buku
        if (buku != null) {
            _judulInput.value = buku.judul
            _penulisInput.value = buku.penulis
            _kategoriInput.value = buku.kategori
        } else {
            resetForm()
        }
    }

    private fun resetForm() {
        _judulInput.value = ""
        _penulisInput.value = ""
        _kategoriInput.value = ""
        _bukuYangDiedit.value = null
        _errorMessage.value = null
        _isSaveSuccess.value = false
    }

    private fun saveOrUpdateBuku(bukuId: Long?) {
        val judul = _judulInput.value.trim()
        val penulis = _penulisInput.value.trim()
        val kategori = _kategoriInput.value.trim()

        // Validasi input: Judul tidak boleh kosong atau hanya berisi spasi
        if (judul.isBlank()) {
            _errorMessage.value = "⚠️ Judul buku tidak boleh kosong atau hanya berisi spasi."
            return
        }

        viewModelScope.launch {
            if (bukuId == null || bukuId == 0L) {
                // Tambah buku baru
                val bukuBaru = BukuEntity(
                    judul = judul,
                    penulis = if (penulis.isBlank()) "Tanpa Penulis" else penulis,
                    kategori = if (kategori.isBlank()) "Umum" else kategori,
                    sudahDibaca = false
                )
                repository.insertBuku(bukuBaru)
            } else {
                // Update buku yang ada
                val bukuUpdate = BukuEntity(
                    id = bukuId,
                    judul = judul,
                    penulis = if (penulis.isBlank()) "Tanpa Penulis" else penulis,
                    kategori = if (kategori.isBlank()) "Umum" else kategori,
                    sudahDibaca = _bukuYangDiedit.value?.sudahDibaca ?: false
                )
                repository.updateBuku(bukuUpdate)
            }
            _isSaveSuccess.value = true
        }
    }

    private fun deleteBuku(buku: BukuEntity) {
        viewModelScope.launch {
            repository.deleteBuku(buku)
        }
    }

    private fun toggleStatusBaca(buku: BukuEntity) {
        viewModelScope.launch {
            repository.updateStatusBaca(buku.id, !buku.sudahDibaca)
        }
    }

    /**
     * Factory untuk instansiasi BukuViewModel yang membutuhkan repository pada konstruktor.
     */
    companion object {
        fun provideFactory(repository: BukuRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(BukuViewModel::class.java)) {
                        return BukuViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
