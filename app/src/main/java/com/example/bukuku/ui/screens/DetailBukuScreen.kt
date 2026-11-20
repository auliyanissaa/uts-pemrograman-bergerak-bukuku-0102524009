package com.example.bukuku.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bukuku.ui.BukuUiState
import com.example.bukuku.ui.BukuUiEvent

/**
 * Layar Detail Buku untuk melihat informasi lengkap, mengubah status baca, edit, atau hapus buku.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBukuScreen(
    bukuId: Long,
    uiState: BukuUiState,
    onEvent: (BukuUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    // Cari buku berdasarkan id dari list di uiState
    val buku = uiState.daftarBuku.find { it.id == bukuId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Buku") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
        if (buku == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Data buku tidak ditemukan atau telah dihapus.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = buku.judul,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider()
                            Text(
                                text = "Penulis",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = buku.penulis,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Text(
                                text = "Kategori",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = buku.kategori,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Text(
                                text = "Status Baca",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Switch(
                                    checked = buku.sudahDibaca,
                                    onCheckedChange = {
                                        onEvent(BukuUiEvent.OnToggleStatusBaca(buku))
                                    }
                                )
                                Text(
                                    text = if (buku.sudahDibaca) "Sudah Dibaca" else "Belum Dibaca",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // Tombol Aksi: Edit & Hapus
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onEvent(BukuUiEvent.OnDeleteBuku(buku))
                            onNavigateBack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hapus")
                    }

                    Button(
                        onClick = { onNavigateToEdit(buku.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ubah")
                    }
                }
            }
        }
    }
}
