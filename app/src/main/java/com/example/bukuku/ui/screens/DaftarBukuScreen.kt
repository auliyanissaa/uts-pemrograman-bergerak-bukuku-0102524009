package com.example.bukuku.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bukuku.data.local.BukuEntity
import com.example.bukuku.ui.BukuUiState
import com.example.bukuku.ui.BukuUiEvent

/**
 * Layar Daftar Buku dengan LazyColumn, Mode Tampilan Ringkas, dan Empty State.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBukuScreen(
    uiState: BukuUiState,
    onEvent: (BukuUiEvent) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToForm: (Long?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BukuKu - Daftar Buku") },
                actions = {
                    // Toggle Mode Tampilan Ringkas
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Ringkas", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Switch(
                            checked = uiState.isCompactView,
                            onCheckedChange = { isCompact ->
                                onEvent(BukuUiEvent.OnToggleCompactView(isCompact))
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToForm(null) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Buku")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.daftarBuku.isEmpty()) {
                // Tampilan saat daftar buku kosong
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Belum ada buku di perpustakaan Anda.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ketuk tombol '+' di bawah untuk menambahkan buku baru.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            } else {
                // Daftar buku menggunakan LazyColumn
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.daftarBuku, key = { it.id }) { buku ->
                        BukuItemCard(
                            buku = buku,
                            isCompact = uiState.isCompactView,
                            onClick = { onNavigateToDetail(buku.id) },
                            onToggleReadStatus = { onEvent(BukuUiEvent.OnToggleStatusBaca(buku)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BukuItemCard(
    buku: BukuEntity,
    isCompact: Boolean,
    onClick: () -> Unit,
    onToggleReadStatus: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompact) 2.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (buku.sudahDibaca)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        if (isCompact) {
            // Mode Tampilan Ringkas (Compact)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = buku.judul,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${buku.penulis} • ${buku.kategori}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Checkbox(
                    checked = buku.sudahDibaca,
                    onCheckedChange = { onToggleReadStatus() }
                )
            }
        } else {
            // Mode Tampilan Normal (Detail lengkap)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = buku.judul,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Penulis: ${buku.penulis}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kategori: ${buku.kategori}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    AssistChip(
                        onClick = { onToggleReadStatus() },
                        label = {
                            Text(if (buku.sudahDibaca) "Sudah Dibaca" else "Belum Dibaca")
                        }
                    )
                }
            }
        }
    }
}
