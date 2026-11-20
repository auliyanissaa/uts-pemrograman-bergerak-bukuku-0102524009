package com.example.bukuku.ui.navigation

/**
 * Definisi rute navigasi untuk aplikasi BukuKu.
 */
sealed class Screen(val route: String) {
    object DaftarBuku : Screen("daftar_buku")
    
    object DetailBuku : Screen("detail_buku/{bukuId}") {
        fun createRoute(bukuId: Long) = "detail_buku/$bukuId"
    }
    
    object FormBuku : Screen("form_buku?bukuId={bukuId}") {
        fun createRoute(bukuId: Long? = null): String {
            return if (bukuId != null && bukuId > 0) {
                "form_buku?bukuId=$bukuId"
            } else {
                "form_buku"
            }
        }
    }
}
