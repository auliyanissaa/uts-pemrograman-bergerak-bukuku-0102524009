package com.example.bukuku.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bukuku.ui.BukuUiState
import com.example.bukuku.ui.BukuUiEvent

/**
 * Layar Form untuk menambah buku baru atau mengubah buku yang sudah ada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBukuScreen(
    bukuId: Long?,
    uiState: BukuUiState,
    onEvent: (BukuUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Jika ada bukuId (Mode Edit), muat data buku ke form saat pertama kali dibuka
    LaunchedEffect(bukuId) {
        if (bukuId != null && bukuId > 0) {
            val buku = uiState.daftarBuku.find { it.id == bukuId }
            onEvent(BukuUiEvent.OnSelectBukuForEdit(buku))
        } else {
            onEvent(BukuUiEvent.OnSelectBukuForEdit(null))
        }
    }

    // Navigasi kembali secara otomatis saat penyimpanan sukses
    LaunchedEffect(uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            onEvent(BukuUiEvent.OnClearSaveSuccess)
            onNavigateBack()
        }
    }

    val isEditMode = bukuId != null && bukuId > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Ubah Buku" else "Tambah Buku Baru") },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(BukuUiEvent.OnResetForm)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pesan validasi jika ada error
            if (uiState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            OutlinedTextField(
                value = uiState.judulInput,
                onValueChange = { onEvent(BukuUiEvent.OnJudulChanged(it)) },
                label = { Text("Judul Buku *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage != null
            )

            OutlinedTextField(
                value = uiState.penulisInput,
                onValueChange = { onEvent(BukuUiEvent.OnPenulisChanged(it)) },
                label = { Text("Penulis") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.kategoriInput,
                onValueChange = { onEvent(BukuUiEvent.OnKategoriChanged(it)) },
                label = { Text("Kategori") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onEvent(BukuUiEvent.OnSaveBuku(bukuId))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = if (isEditMode) "Simpan Perubahan" else "Tambah Buku")
            }
        }
    }
}
