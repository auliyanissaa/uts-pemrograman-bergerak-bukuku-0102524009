package com.example.bukuku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bukuku.data.local.BukuDatabase
import com.example.bukuku.data.preferences.PreferencesManager
import com.example.bukuku.data.repository.BukuRepository
import com.example.bukuku.ui.BukuViewModel
import com.example.bukuku.ui.navigation.Screen
import com.example.bukuku.ui.screens.DaftarBukuScreen
import com.example.bukuku.ui.screens.DetailBukuScreen
import com.example.bukuku.ui.screens.FormBukuScreen
import com.example.bukuku.ui.theme.BukuKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi Database, Preferences, dan Repository
        val database = BukuDatabase.getDatabase(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val repository = BukuRepository(database.bukuDao(), preferencesManager)

        setContent {
            BukuKuTheme {
                val navController = rememberNavController()
                val viewModel: BukuViewModel = viewModel(
                    factory = BukuViewModel.provideFactory(repository)
                )
                val uiState by viewModel.uiState.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = Screen.DaftarBuku.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Layar Daftar Buku
                    composable(Screen.DaftarBuku.route) {
                        DaftarBukuScreen(
                            uiState = uiState,
                            onEvent = viewModel::onEvent,
                            onNavigateToDetail = { bukuId ->
                                navController.navigate(Screen.DetailBuku.createRoute(bukuId))
                            },
                            onNavigateToForm = { bukuId ->
                                navController.navigate(Screen.FormBuku.createRoute(bukuId))
                            }
                        )
                    }

                    // 2. Layar Detail Buku
                    composable(
                        route = Screen.DetailBuku.route,
                        arguments = listOf(navArgument("bukuId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val bukuId = backStackEntry.arguments?.getLong("bukuId") ?: 0L
                        DetailBukuScreen(
                            bukuId = bukuId,
                            uiState = uiState,
                            onEvent = viewModel::onEvent,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToEdit = { id ->
                                navController.navigate(Screen.FormBuku.createRoute(id))
                            }
                        )
                    }

                    // 3. Layar Form Tambah / Ubah Buku
                    composable(
                        route = Screen.FormBuku.route,
                        arguments = listOf(
                            navArgument("bukuId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        )
                    ) { backStackEntry ->
                        val argId = backStackEntry.arguments?.getLong("bukuId") ?: -1L
                        val bukuId = if (argId > 0L) argId else null
                        FormBukuScreen(
                            bukuId = bukuId,
                            uiState = uiState,
                            onEvent = viewModel::onEvent,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
